package persistence_layer.DAO;
import java.sql.Connection;

public class CostEngineeringDAO {
    //fields:
    private Connection conn;

    //Constructor
    public CostEngineeringDAO(Connection conn) {
        this.conn = conn;
    }
}
