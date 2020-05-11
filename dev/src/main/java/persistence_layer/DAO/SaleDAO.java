package persistence_layer.DAO;

import bussines_layer.inventory_module.Sale;

import java.util.WeakHashMap;import java.sql.Connection;


public class SaleDAO {
    //fields:
    private WeakHashMap<Integer, Sale> identityMap;
    private Connection conn;

    //Constructor
    public SaleDAO(Connection conn) {
        this.conn = conn;
    }
}
