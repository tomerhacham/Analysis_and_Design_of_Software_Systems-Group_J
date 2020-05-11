package persistence_layer;

import persistence_layer.DAO.*;

import java.sql.Connection;
import java.sql.DriverManager;

public class Mapper {
    //fields:
    private GeneralProductDAO general_product_dao;
    private SpecificProductDAO specific_product_dao;
    private CategoryDAO category_dao;
    private SaleDAO sale_dao;
    private OrderDAO order_dao;
    private ContractDAO contract_dao;
    private SupplierDAO supplier_dao;
    private BranchDAO branch_dao;
    private general_products_on_saleDAO general_products_on_sale_dao;
    private general_product_in_orderDAO general_product_in_order_dao;
    private CostEngineeringDAO cost_engineering_dao;
    private catalog_product_in_contractDAO catalog_product_in_contract_dao;
    private IDsDAO ids_dao;

    //Constructor
    public Mapper() {
        String userName = "root";
        String password = "";
        String userDirectory = System.getProperty("user.dir");
        //TODO: change the url of the connection to the path of the DB
        String url = "jdbc:mysql://localhost:3306/library";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            this.general_product_dao = new GeneralProductDAO(conn);
            this.specific_product_dao = new SpecificProductDAO(conn);
            this.category_dao=new CategoryDAO(conn);
            this.sale_dao = new SaleDAO(conn);
            this.order_dao = new OrderDAO(conn);
            this.contract_dao = new ContractDAO(conn);
            this.supplier_dao = new SupplierDAO(conn);
            this.branch_dao = new BranchDAO(conn);
            this.general_products_on_sale_dao = new general_products_on_saleDAO(conn);
            this.general_product_in_order_dao = new general_product_in_orderDAO(conn);
            this.cost_engineering_dao = new CostEngineeringDAO(conn);
            this.catalog_product_in_contract_dao = new catalog_product_in_contractDAO(conn);
            this.ids_dao = new IDsDAO(conn);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


}


