package data_access_layer.DTO;

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
    @DatabaseField(columnName = "date", dataType = DataType.DATE_STRING)
    Date date;
    @DatabaseField(columnName = "status", dataType = DataType.ENUM_TO_STRING)
    Status status;
    @DatabaseField(columnName = "type", dataType = DataType.ENUM_TO_STRING)
    OrderType type;
    @ForeignCollectionField(eager = false)
    ForeignCollection<general_product_in_orderDTO> product_on_sale;

    //Constructor
    public OrderDTO(Integer order_id,SupplierDTO supplier, Date date, String status, String type) {
        this.order_id = order_id;
        this.supplier = supplier;
        this.date = date;
        this.status = convertOrderStatusToString(status);
        this.type = convertOrderTypeToString(type);
    }
    public OrderDTO() {
    }

    //region Methods

    public Integer getOrder_id() {
        return order_id;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public Date getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public OrderType getType() {
        return type;
    }

    public ForeignCollection<general_product_in_orderDTO> getProduct_on_sale() {
        return product_on_sale;
    }

    public OrderType convertOrderTypeToString(String type){
        if(type.equals("PeriodicOrder")){return OrderType.PeriodicOrder;}
        else{return OrderType.UpdateStockOrder;}
    }

    public Status convertOrderStatusToString(String status){
        if(status.equals("received")){return Status.received;}
        else{return Status.waiting;}
    }
    //endregion

}
