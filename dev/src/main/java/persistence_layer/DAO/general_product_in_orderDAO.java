package persistence_layer.DAO;
import java.sql.Connection;

public class general_product_in_orderDAO {
    //fields:
    private Connection conn;

    //Constructor
    public general_product_in_orderDAO(Connection conn) {
        this.conn = conn;
    }


}
