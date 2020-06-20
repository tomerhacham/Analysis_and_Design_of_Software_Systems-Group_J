package data_access_layer.DTO;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "pending_orders")
public class pending_orders_dto {

    @DatabaseField(columnName = "orderID", uniqueCombo = true, canBeNull = false)
    private Integer orderID;

    @DatabaseField(columnName = "branchID", uniqueCombo = true, canBeNull = false)
    private Integer branchID;

    public pending_orders_dto() {
    }

    public pending_orders_dto(Integer orderID, Integer branchID ) {
        this.orderID=orderID;
        this.branchID=branchID;
    }

    public int getBranchID() {
        return branchID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setBranchID(Integer branchID) {
        this.branchID = branchID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }
}
