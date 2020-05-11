package persistence_layer.DAO;

import bussines_layer.SupplierCard;

import java.sql.Connection;
import java.util.WeakHashMap;

public class SupplierDAO {
    //fields:
    private WeakHashMap<Integer, SupplierCard> identityMap;
    private Connection conn;

    //Constructor
    public SupplierDAO(Connection conn) {
        this.conn = conn;
    }
}
