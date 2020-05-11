package persistence_layer.DAO;
import java.sql.Connection;

public class IDsDAO {
    //fields:
    private Connection conn;

    //Constructors
    public IDsDAO(Connection conn) {
        this.conn = conn;
    }
}
