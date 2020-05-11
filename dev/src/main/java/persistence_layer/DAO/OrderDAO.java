package persistence_layer.DAO;

import bussines_layer.supplier_module.Order;
import java.util.WeakHashMap;import java.sql.Connection;


public class OrderDAO {
    //fields:
    private WeakHashMap<Integer, Order> identityMap;
    private Connection conn;

    //Constructor
    public OrderDAO(Connection conn) {
        this.conn = conn;
    }
}
