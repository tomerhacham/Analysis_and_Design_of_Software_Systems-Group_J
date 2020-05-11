package persistence_layer.DAO;

import bussines_layer.Branch;
import java.sql.Connection;
import java.util.WeakHashMap;

public class BranchDAO {
    //fields:
    private WeakHashMap<Integer, Branch> identityMap;
    private Connection conn;

    //Constructor
    public BranchDAO(Connection conn) {
        this.conn = conn;
    }
}
