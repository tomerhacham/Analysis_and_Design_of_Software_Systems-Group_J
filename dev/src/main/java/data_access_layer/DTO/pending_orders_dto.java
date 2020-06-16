package data_access_layer.DTO;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "pending_orders")
public class pending_orders_dto {

    @DatabaseField(columnName = "orderID", uniqueCombo = true, canBeNull = false, foreignAutoRefresh = true)
    private int orderID;

    @DatabaseField(columnName = "branchID", uniqueCombo = true, canBeNull = false, foreignAutoRefresh = true)
    private int branchID;

    public pending_orders_dto(int orderID, int branchID ) {
        this.orderID=orderID;
        this.branchID=branchID;
    }

    public int getBranchID() {
        return branchID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
}
