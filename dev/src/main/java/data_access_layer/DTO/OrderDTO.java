package data_access_layer.DTO;

import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

enum OrderType
{PeriodicOrder,UpdateStockOrder;}

enum Status
{received,waiting}

@DatabaseTable(tableName = "Order")
public class OrderDTO {
    //fields
    @DatabaseField(id=true,columnName = "order_id")
    Integer order_id;
    @DatabaseField(foreign = true,columnName = "supplier_id", foreignAutoRefresh = true, foreignColumnName = "supplier_id")
    SupplierDTO supplier;
    @DatabaseField(columnName = "type", dataType = DataType.ENUM_TO_STRING)
    OrderType type;
    @DatabaseField(columnName = "status", dataType = DataType.ENUM_TO_STRING)
    Status status;
    @DatabaseField(columnName = "dayToDeliver")
    Integer daytodeliver;
    @DatabaseField(columnName = "date", dataType = DataType.DATE_STRING)
    Date issuedDate;

    @ForeignCollectionField(eager = false)
    ForeignCollection<catalog_product_in_orderDTO> catalog_product_in_order;

    //Constructor
    public OrderDTO(Integer order_id,SupplierDTO supplier, Date date, String status, String type) {
        this.order_id = order_id;
        this.supplier = supplier;
        this.issuedDate = date;
        this.status = convertOrderStatusToString(status);
        this.type = convertOrderTypeToString(type);
    }
    public OrderDTO(Order order){
        this.order_id=order.getOrderID();
        this.supplier=new SupplierDTO(order.getSupplier());
        this.type = convertOrderTypeToString(order.getType().name());
        this.status=convertOrderStatusToString(order.getStatus().name());
        this.daytodeliver= (Integer) order.getDayToDeliver().getData();
        this.issuedDate= order.getIssuedDate();
    }
    public OrderDTO() {}

    //region Methods

    public Integer getOrder_id() {
        return order_id;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public OrderType getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getDaytodeliver() {
        return daytodeliver;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public ForeignCollection<catalog_product_in_orderDTO> getCatalog_product_in_order() {
        return catalog_product_in_order;
    }

    public OrderType convertOrderTypeToString(String type){
        if(type.equals("PeriodicOrder")){return OrderType.PeriodicOrder;}
        else{return OrderType.UpdateStockOrder;}
    }

    public Status convertOrderStatusToString(String status){
        if(status.equals("received")){return Status.received;}
        else{return Status.waiting;}
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "order_id=" + order_id +
                ", supplier=" + supplier.supplier_id +
                ", type=" + type.name() +
                ", status=" + status.name() +
                ", daytodeliver=" + daytodeliver +
                ", issuedDate=" + issuedDate +
                '}';
    }
    //endregion

}
