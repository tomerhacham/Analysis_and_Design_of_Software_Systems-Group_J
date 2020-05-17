package data_access_layer.DTO;

import bussines_layer.enums.OrderStatus;
import bussines_layer.enums.OrderType;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Order")
public class OrderDTO {
    //fields
    @DatabaseField(columnName = "order_id")
    Integer order_id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true ,foreignColumnName = "branch_id", columnName = "branch_id")
    BranchDTO branch_id;
    @DatabaseField(columnName = "supplier_id")
    Integer supplier;
    @DatabaseField(columnName = "type", dataType = DataType.ENUM_TO_STRING)
    OrderType type;
    @DatabaseField(columnName = "status", dataType = DataType.ENUM_TO_STRING)
    OrderStatus status;
    @DatabaseField(columnName = "day_to_deliver")
    Integer daytodeliver;
    @DatabaseField(columnName = "issue_date", dataType = DataType.DATE_STRING)
    Date issuedDate;

    /*@ForeignCollectionField(eager = false)
    ForeignCollection<catalog_product_in_orderDTO> catalog_product_in_order;*/

    //Constructor
    public OrderDTO(Order order){
        this.branch_id=new BranchDTO(order.getBranch_id());
        this.order_id=order.getOrderID();
        this.supplier=order.getSupplierID();
        this.type = order.getType();
        this.status=order.getStatus();
        this.daytodeliver= (Integer) order.getDayToDeliver().getData();
        this.issuedDate= order.getIssuedDate();
    }
    public OrderDTO() {}

    //region Methods

    public Integer getOrder_id() {
        return order_id;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public OrderType getType() {
        return type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Integer getDaytodeliver() {
        return daytodeliver;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public BranchDTO getBranch_id() {
        return branch_id;
    }

    /*public ForeignCollection<catalog_product_in_orderDTO> getCatalog_product_in_order() {
        return catalog_product_in_order;
    }*/

    /*public OrderType convertOrderTypeToString(String type){
        if(type.equals("PeriodicOrder")){return OrderType.PeriodicOrder;}
        else{return OrderType.UpdateStockOrder;}
    }

    public Status convertOrderStatusToString(String status){
        if(status.equals("received")){return Status.received;}
        else{return Status.waiting;}
    }*/

    @Override
    public String toString() {
        return "OrderDTO{" +
                "order_id=" + order_id +
                ", supplier=" + supplier +
                ", type=" + type.name() +
                ", status=" + status.name() +
                ", daytodeliver=" + daytodeliver +
                ", issuedDate=" + issuedDate +
                '}';
    }
    //endregion

}
