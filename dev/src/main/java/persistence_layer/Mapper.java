package persistence_layer;

import persistence_layer.DTO.*;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class Mapper {
    //fields:
    private ConnectionSource conn;

    private Dao<GeneralProductDTO,Integer> general_product_dao;
    private Dao<SpecificProdcutDTO,Integer> specific_product_dao;
    private Dao<CategoryDTO,Integer> category_dao;
    private Dao<SalesDTO,Integer> sale_dao;
    private Dao<OrderDTO,Integer> order_dao;
    private Dao<ContractDTO,Integer> contract_dao;
    private Dao<SupplierDTO,Integer> supplier_dao;
    private Dao<BranchDTO,Integer> branch_dao;
    private Dao<general_products_on_saleDTO,Void> general_products_on_sale_dao;   //will not support cache
    private Dao<general_product_in_orderDTO,Void> general_product_in_order_dao;   //will not support cache
    private Dao<CostEngineeringDTO,Void> cost_engineering_dao;           //will not support cache
    private Dao<catalog_product_in_contractDTO,Void> catalog_product_in_contract_dao;//will not support cache
    private Dao<IDsDTO,Void> ids_dao;                        //will not support cache

    //Constructor
    public Mapper() {
        String userDirectory = System.getProperty("user.dir");
        String databaseUrl = "jdbc:sqlite:SuperLi.db";
        try (ConnectionSource conn = new JdbcConnectionSource(databaseUrl)) {
            this.conn=conn;
            //region setting up DAOs with cache functionality
            this.general_product_dao = DaoManager.createDao(conn,GeneralProductDTO.class);
            this.general_product_dao.setObjectCache(true);

            this.specific_product_dao = DaoManager.createDao(conn,SpecificProdcutDTO.class);
            this.specific_product_dao.setObjectCache(true);

            this.category_dao = DaoManager.createDao(conn,CategoryDTO.class);
            this.category_dao.setObjectCache(true);

            this.sale_dao = DaoManager.createDao(conn,SalesDTO.class);
            this.sale_dao.setObjectCache(true);

            this.order_dao = DaoManager.createDao(conn,OrderDTO.class);
            this.order_dao.setObjectCache(true);

            this.contract_dao = DaoManager.createDao(conn,ContractDTO.class);
            this.contract_dao.setObjectCache(true);

            this.supplier_dao = DaoManager.createDao(conn,SupplierDTO.class);
            this.supplier_dao.setObjectCache(true);

            this.branch_dao = DaoManager.createDao(conn,BranchDTO.class);
            this.branch_dao.setObjectCache(true);
            //endregion
            //region setting up DAOs without cache functionality
            this.general_products_on_sale_dao = DaoManager.createDao(conn,general_products_on_saleDTO.class);
            this.general_product_in_order_dao = DaoManager.createDao(conn,general_product_in_orderDTO.class);
            this.cost_engineering_dao=DaoManager.createDao(conn,CostEngineeringDTO.class);
            this.catalog_product_in_contract_dao=DaoManager.createDao(conn,catalog_product_in_contractDTO.class);
            this.ids_dao = DaoManager.createDao(conn,IDsDTO.class);
            //endregion
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}


