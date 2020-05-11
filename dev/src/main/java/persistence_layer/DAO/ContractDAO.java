package persistence_layer.DAO;

import bussines_layer.supplier_module.Contract;

import java.util.WeakHashMap;
import java.sql.Connection;

public class ContractDAO {
    //fields:
    private WeakHashMap<Integer, Contract> identityMap;
    private Connection conn;

    //Constructor
    public ContractDAO(Connection conn) {
        this.conn = conn;
    }
}
