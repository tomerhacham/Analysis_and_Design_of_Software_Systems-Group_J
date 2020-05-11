package persistence_layer.DAO;
import java.sql.Connection;

public class general_products_on_saleDAO {
    //fields:
    private Connection conn;

    //Constructor
    public general_products_on_saleDAO(Connection conn) {
        this.conn = conn;
    }
}
