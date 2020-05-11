package persistence_layer.DAO;

import bussines_layer.inventory_module.Category;

import java.util.WeakHashMap;
import java.sql.Connection;


public class CategoryDAO {
    //fields:
    private WeakHashMap<Integer, Category> identityMap;
    private Connection conn;

    //Constructor
    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }
}
