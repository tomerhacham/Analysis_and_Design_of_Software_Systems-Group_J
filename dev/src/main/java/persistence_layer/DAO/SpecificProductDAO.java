package persistence_layer.DAO;

import bussines_layer.inventory_module.SpecificProduct;

import java.sql.Connection;
import java.util.WeakHashMap;

public class SpecificProductDAO {
    //fields:
    private WeakHashMap<Integer, SpecificProduct> identityMap;
    private Connection conn;

    //Constructor
    public SpecificProductDAO(Connection conn) {
        this.conn = conn;
    }
}
