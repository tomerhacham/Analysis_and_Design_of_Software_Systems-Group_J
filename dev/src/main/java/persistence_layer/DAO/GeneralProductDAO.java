package persistence_layer.DAO;

import bussines_layer.inventory_module.GeneralProduct;

import java.util.WeakHashMap;
import java.sql.Connection;

public class GeneralProductDAO {
    //fields:
    private WeakHashMap<Integer, GeneralProduct> identityMap;
    private Connection conn;

    //Constructor
    public GeneralProductDAO(Connection conn) {
        this.conn = conn;
    }
}
