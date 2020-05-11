package persistence_layer.DAO;
import java.sql.Connection;

public class catalog_product_in_contractDAO {
    //fields:
    private Connection conn;

    //Constructor
    public catalog_product_in_contractDAO(Connection conn) {
        this.conn = conn;
    }
}
