package data_access_layer;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.Category;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.SpecificProduct;
import data_access_layer.DTO.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.util.LinkedList;

public class Mapper {
    //fields:
    private ConnectionSource conn;

    private Dao<GeneralProductDTO,Integer> general_product_dao;
    private Dao<SpecificProductDTO,Integer> specific_product_dao;
    private Dao<CategoryDTO,Integer> category_dao;
    private Dao<SaleDTO,Integer> sale_dao;
    private Dao<OrderDTO,Integer> order_dao;
    private Dao<ContractDTO,Integer> contract_dao;
    private Dao<SupplierDTO,Integer> supplier_dao;
    private Dao<BranchDTO,Integer> branch_dao;

    private Dao<general_product_on_saleDTO,Void> general_product_on_sale_dao;           //will not support cache
    private Dao<general_product_in_orderDTO,Void> general_product_in_order_dao;         //will not support cache
    private Dao<CostEngineeringDTO,Void> cost_engineering_dao;                          //will not support cache
    private Dao<catalog_product_in_contractDTO,Void> catalog_product_in_contract_dao;   //will not support cache
    private Dao<IDsDTO,Void> ids_dao;                                                   //will not support cache
    private Dao<categories_in_contractDTO,Void> categories_in_contract_dao;             //will not support cache
    private Dao<catalog_product_in_general_productDTO,Void> catalog_product_in_general_products_dao;

    //Constructor
    public Mapper() {
        String databaseUrl = "jdbc:sqlite:SuperLi.db";
        try (ConnectionSource conn = new JdbcConnectionSource(databaseUrl)) {
            this.conn=conn;
            //region setting up DAOs with cache functionality
            this.general_product_dao = DaoManager.createDao(conn,GeneralProductDTO.class);
            this.general_product_dao.setObjectCache(true);

            this.specific_product_dao = DaoManager.createDao(conn,SpecificProductDTO.class);
            this.specific_product_dao.setObjectCache(true);

            this.category_dao = DaoManager.createDao(conn,CategoryDTO.class);
            this.category_dao.setObjectCache(true);

            this.sale_dao = DaoManager.createDao(conn,SaleDTO.class);
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
            this.general_product_on_sale_dao = DaoManager.createDao(conn,general_product_on_saleDTO.class);
            this.general_product_in_order_dao = DaoManager.createDao(conn,general_product_in_orderDTO.class);
            this.cost_engineering_dao=DaoManager.createDao(conn,CostEngineeringDTO.class);
            this.catalog_product_in_contract_dao=DaoManager.createDao(conn,catalog_product_in_contractDTO.class);
            this.ids_dao = DaoManager.createDao(conn,IDsDTO.class);
            this.categories_in_contract_dao = DaoManager.createDao(conn,categories_in_contractDTO.class);
            this.catalog_product_in_general_products_dao=DaoManager.createDao(conn,catalog_product_in_general_productDTO.class);
            //endregion
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region Methods
    //TODO: generate DTO from provided class
    //TODO: load cross-referenced data (e.g catalog product in order)

    /**
     * write general product and all associate objects to DB
     * @param generalProduct
     */
    public void create(GeneralProduct generalProduct){
        GeneralProductDTO generalProductDTO = new GeneralProductDTO(generalProduct);
        LinkedList<SpecificProductDTO> specific_products=new LinkedList<>();
        for(SpecificProduct specificProduct:generalProduct.getProducts()){
            specific_products.add(new SpecificProductDTO(generalProductDTO,specificProduct));
        }
        LinkedList<catalog_product_in_general_productDTO> catalog_products=new LinkedList<>();
        for(CatalogProduct catalogProduct:generalProduct.getCatalog_products()){
            catalog_products.add(new catalog_product_in_general_productDTO(generalProductDTO,catalogProduct));
        }
        try{
            general_product_dao.create(generalProductDTO);
            specific_product_dao.create(specific_products);
            catalog_product_in_general_products_dao.create(catalog_products);
        }
        catch(Exception e){e.printStackTrace();}
    }

    /**
     * save main category ONLY; in order to the regular category you need to provide the super_category
     * @param category
     */
    public void create(Category category){
        //todo: create DTO for category
        CategoryDTO categoryDTO=new CategoryDTO(category);
        for (Category sub_category:category.getSub_categories()){create(categoryDTO,sub_category);}
        //todo:create DTO for each sub_category
        //todo: create GeneralProductDTO for each generalProduct in generalproduct list

    }

    /**
     * recursive function to the all sub-categories and its associate classes
     * @param super_category - DTO class of the super category
     * @param category
     */
    private void create(CategoryDTO super_category, Category category){
        CategoryDTO categoryDTO = new CategoryDTO(super_category,category);
        for (Category sub_category:category.getSub_categories()){
            create(categoryDTO,category);
        }
        if (!category.getAllGeneralProduct().isEmpty()){
            for (GeneralProduct generalProduct:category.getAllGeneralProduct()){
                create(generalProduct);
            }
        }
        try {category_dao.create(categoryDTO);}
        catch(Exception e){e.printStackTrace();}
    }
    public void create(Sale sale){
        //todo:create DTO for sale
        //todo: create GeneralProductDTO for each products_on_sale
    }
    public void create(Branch branch){
        //todo:creat DTO for branch;
    }
    public void create(Contract contract){
        //todo: create DTO for contract
        //todo:create DTO for each category in contract
        //todo: create DT for each catalog product in category
        //todo: create DTO for each costEngineering product;
    }
    public void create(Order order){
        //todo:create DTO for Order
        //todo:for each catalogProduct in productlist find the generalProduct DTO (using Doa).
        //todo: create general_product_in_orderDTO for each generalProductDTO that has been found

    }
    public void create(SupplierCard supplier){
        //todo:create DTO for supplier
        //todo: for each contact name in ContactName create contact_of_supplierDTO
    }
    //endregion


}


