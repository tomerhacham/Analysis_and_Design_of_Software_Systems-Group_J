package data_access_layer;

import bussines_layer.Branch;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.*;
import bussines_layer.supplier_module.Contract;
import bussines_layer.supplier_module.CostEngineering;
import bussines_layer.supplier_module.Order;
import data_access_layer.DAO.*;
import data_access_layer.DTO.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import sun.java2d.loops.FillRect;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Singleton class to communicate with the DB
 */
public class Mapper {
    //fields:
    private ConnectionSource conn;
    private CatalogProductDAO catalog_product_dao;
    private GeneralProductDAO general_product_dao;
    private SpecificProductDAO specific_product_dao;
    private CategoryDAO category_dao;
    private SaleDAO sale_dao;
    private OrderDAO order_dao;
    private ContractDAO contract_dao;
    private SupplierDAO supplier_dao;
    private BranchDAO branch_dao;
    private CostEngineeringDAO cost_engineering_dao;
    private Dao<IDsDTO, Integer> ids_dao;

    private static Mapper instance=null;

    //Constructor
    private Mapper() {
        String databaseUrl = "jdbc:sqlite:src/main/java/data_access_layer/SuperLi.db";
        try (ConnectionSource conn = new JdbcConnectionSource(databaseUrl)) {
            this.conn = conn;
            this.catalog_product_dao = new CatalogProductDAO(conn);
            this.general_product_dao = new GeneralProductDAO(conn);
            this.specific_product_dao = new SpecificProductDAO(conn);
            this.category_dao = new CategoryDAO(conn);
            this.sale_dao = new SaleDAO(conn);
            this.order_dao=new OrderDAO(conn);
            this.contract_dao = new ContractDAO(conn);
            this.supplier_dao=new SupplierDAO(conn);
            this.branch_dao=new BranchDAO(conn);
            this.cost_engineering_dao=new CostEngineeringDAO(conn);
            this.ids_dao = DaoManager.createDao(conn, IDsDTO.class);

            //endregion
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Mapper getInstance(){
        if(instance==null){instance=new Mapper();}
        return instance;
    }

    //region Methods
    public Integer loadID(String controller_name){
        Integer id=null;
        try {
            IDsDTO iDs = ids_dao.queryForId(1);
            switch(controller_name) {
                case "category":
                    id=iDs.getCategory_next_id();
                    break;
                case "product":
                    id=iDs.getProduct_next_id();
                    break;
                case "sale":
                    id=iDs.getSale_next_id();
                    break;
                case "contract":
                    id= iDs.getContract_next_id();
                    break;
                case "order":
                    id= iDs.getOrder_next_id();
                    break;
                case "branch":
                    id= iDs.getBranch_next_id();
                    break;
                case "supplier":
                    id=iDs.getSupplier_next_id();
                    break;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    public void writeID(String controller_name,Integer id){
        try {
            IDsDTO iDs = ids_dao.queryForId(1);
            switch(controller_name) {
                case "category":
                    iDs.setCategory_next_id(id);
                    break;
                case "product":
                    iDs.setProduct_next_id(id);
                    break;
                case "sale":
                    iDs.setSale_next_id(id);
                    break;
                case "contract":
                    iDs.setContract_next_id(id);
                    break;
                case "order":
                    iDs.setOrder_next_id(id);
                    break;
                case "branch":
                    iDs.setBranch_next_id(id);
                    break;
                case "supplier":
                    iDs.setSupplier_next_id(id);
                    break;
            }
            ids_dao.createOrUpdate(iDs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public LinkedList<SupplierCard> loadSuppliers() {
        LinkedList<SupplierCard> supplierCards = new LinkedList<>();
        try {
            List<SupplierDTO> supplierDTOS = supplier_dao.dao.queryForAll();
            for (SupplierDTO dto : supplierDTOS) {
                supplierCards.add(find_Supplier(dto.getSupplier_id()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return supplierCards;
    }

    public LinkedList<Branch> loadBranches() {
        LinkedList<Branch> branches = new LinkedList<>();
        try {
            List<BranchDTO> branchDTOS = branch_dao.dao.queryForAll();
            for (BranchDTO dto : branchDTOS) {
                branches.add(find_Branch(dto.getBranch_id()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return branches;
    }

    public LinkedList<Category> loadCategories(Integer branch_id) {
        LinkedList<Category> main_categories = new LinkedList<>();
        try {
            List<CategoryDTO> dto_main_categories = category_dao.dao.queryBuilder().where().eq("branch_id", branch_id).and().eq("super_category_id", 0).query();
            for (CategoryDTO main_cat_dto : dto_main_categories) {
                Category category = find_Category(main_cat_dto.getId(),branch_id);
                category.setSub_categories(LoadSubCategories(branch_id, category.getId()));
                main_categories.add(category);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return main_categories;
    }
    //region load categories

    private LinkedList<Category> LoadSubCategories(Integer branch_id, Integer id) {
        LinkedList<Category> sub_categories = new LinkedList<>();
        try {
            List<CategoryDTO> dto_sub_categories = category_dao.dao.queryBuilder().where().eq("branch_id", branch_id).and().eq("super_category_id", id).query();
            for (CategoryDTO sub_cat_dto : dto_sub_categories) {
                Category category = find_Category(sub_cat_dto.getId(),branch_id);
                category.setSub_categories(LoadSubSubCategories(branch_id, category.getId()));
                sub_categories.add(category);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sub_categories;
    }

    private LinkedList<Category> LoadSubSubCategories(Integer branch_id, Integer category_id) {
        LinkedList<Category> sub_sub_categories = new LinkedList<>();
        try {
            List<CategoryDTO> dto_sub_sub_categories = category_dao.dao.queryBuilder().where().eq("branch_id", branch_id).and().eq("super_category_id", category_id).query();
            for (CategoryDTO sub_sub_cat_dto : dto_sub_sub_categories) {
                Category category = find_Category(sub_sub_cat_dto.getId(),branch_id);
                category.setGeneralProducts(loadGeneralProduct(branch_id,category_id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sub_sub_categories;
    }

    private LinkedList<GeneralProduct> loadGeneralProduct(Integer branch_id, Integer category_id){
        LinkedList<GeneralProduct> generalProducts = new LinkedList<>();
        try {
            List<GeneralProductDTO> generalProductDTOS = general_product_dao.dao.queryBuilder().where().eq("branch_id",branch_id).and().eq("category_id",category_id).query();
            for(GeneralProductDTO dto:generalProductDTOS){
                GeneralProduct generalProduct= find_GeneralProductbyCategory(dto.getGPID(),category_id,branch_id);
                generalProduct.setCatalog_products(loadCatalogProducts(generalProduct.getGpID(),generalProduct.getBranch_id()));
                generalProduct.setProducts(loadSpecificProducts(generalProduct.getGpID(),generalProduct.getBranch_id()));
                generalProducts.add(generalProduct);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return generalProducts;
    }

    private LinkedList<SpecificProduct> loadSpecificProducts(Integer gpID, Integer branch_id) {
        LinkedList<SpecificProduct> specificProducts = new LinkedList<>();
        try {
            List<SpecificProductDTO> DTos  = specific_product_dao.dao.queryBuilder().where().eq("GPID",gpID).and().eq("branch_id",branch_id).query();
            for(SpecificProductDTO sp:DTos){
                specificProducts.add(find_SpecificProduct(sp.getId(),branch_id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return specificProducts;
    }

    private LinkedList<CatalogProduct> loadCatalogProducts(Integer gpID, Integer branch_id) {
        LinkedList<CatalogProduct> catalogProducts = new LinkedList<>();
        try {
            List<catalog_product_in_general_productDTO> binding  = catalog_product_dao.catalog_product_in_general_products_dao.queryBuilder().where().eq("GPID",gpID).and().eq("branch_id",branch_id).query();
            for(catalog_product_in_general_productDTO cpg:binding){
                catalogProducts.add(find_CatalogProduct(cpg.getCatalogID(),branch_id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return catalogProducts;
    }
    //endregion

    public LinkedList<Sale> loadSales(Integer branch_id){
        LinkedList<Sale> sales = new LinkedList<>();
        try {
            List<SaleDTO> saleDTOS = sale_dao.dao.queryBuilder().where().eq("branch_id",branch_id).query();
            for(SaleDTO saleDTO:saleDTOS){
                sales.add(find_Sale(saleDTO.getSale_id(),branch_id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sales;
    }

    public LinkedList<Order> loadOrders(Integer branch_id){
        LinkedList<Order> orders = new LinkedList<>();
        try {
            List<OrderDTO> orderDTOS = order_dao.dao.queryBuilder().where().eq("branch_id",branch_id).query();
            for(OrderDTO orderDTO:orderDTOS){
                orders.add(find_Order(orderDTO.getOrder_id(),branch_id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return orders;
    }

    public LinkedList<Contract> loadContracts(Integer branch_id){
        LinkedList<Contract> contracts = new LinkedList<>();
        try {
            List<ContractDTO> contractDTOS = contract_dao.dao.queryBuilder().where().eq("branch_id",branch_id).query();
            for(ContractDTO contractDTO:contractDTOS){
                Contract contract = find_Contract(contractDTO.getContract_id(),branch_id);
                contract.setCategories(loadContractCategories(contract.getContractID(),branch_id));
                contract.setCostEngineering(loadCostEngineering(contract.getContractID(),branch_id));
                contract.setProducts(loadCatalogProductsInContract(contract.getContractID(),branch_id));
                contract.setSupplier(find_Supplier(contractDTO.getSupplier_id()));
                contracts.add(contract);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contracts;
    }
    //region load contract

    private HashMap<Integer, CatalogProduct> loadCatalogProductsInContract(Integer contractID, Integer branch_id) {
        HashMap<Integer,CatalogProduct> catalogProducts = new HashMap<>();
        try {
            List<catalog_product_in_contractDTO> catalog_product_in_contractDTOS = contract_dao.catalog_product_in_contract_dao.queryBuilder().where().eq("branch_id",branch_id).and().eq("contract_id",contractID).query();
            for (catalog_product_in_contractDTO dto:catalog_product_in_contractDTOS){
                catalogProducts.put(dto.getCatalog_id(),find_CatalogProduct(dto.getCatalog_id(),branch_id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return catalogProducts;
    }

    private CostEngineering loadCostEngineering(Integer contract_id, Integer branch_id) {
        return find_CostEngineering(contract_id,branch_id);
    }

    private LinkedList<String> loadContractCategories(Integer contractID, Integer branch_id) {
        LinkedList<String> categories = new LinkedList<>();
        try {
            List<categories_in_contractDTO> categories_in_contractDTOS = contract_dao.categories_in_contract_dao.queryBuilder().where().eq("contract_id",contractID).and().eq("branch_id",branch_id).query();
            for(categories_in_contractDTO dto:categories_in_contractDTOS){
                categories.add(dto.getCategory());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return categories;
    }
    //endregion

    //TODO: Warning! the CategoryController must be instate before the ProductController
    public LinkedList<GeneralProduct> loadGeneralProducts(Integer branch_id){
        LinkedList<GeneralProduct> generalProducts=new LinkedList<>();
        try {
            List<GeneralProductDTO>  generalProductDTOS = general_product_dao.dao.queryBuilder().where().eq("branch_id",branch_id).query();
            for(GeneralProductDTO dto:generalProductDTOS){
                generalProducts.add(find_GeneralProduct(dto.getGPID(),branch_id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return generalProducts;
    }

    //region CRUD section
    //region Branch Management
    /**
     * find and return Branch object
     * @param branch_id
     * @return
     */
    public Branch find_Branch(Integer branch_id){
        return branch_dao.find(branch_id);
    }
    /**
     * write Branch to the DB
     * @param branch
     */
    public void create(Branch branch){
        branch_dao.create(branch);
    }
    /**
     * change the Branch name and update the DB
     * @param branch
     */
    public void update(Branch branch){
        branch_dao.update(branch);
    }
    /**
     * delete Branch from the DB
     * @param branch
     */
    public void delete(Branch branch){
        branch_dao.delete(branch);
    }
    //endregion

    //region CatalogProduct Management
    public CatalogProduct find_CatalogProduct(Integer catalog_id,Integer branch_id){
        return catalog_product_dao.find(catalog_id,branch_id);
    }
    /**
     * write the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     */
    public void create(CatalogProduct catalogProduct){
        catalog_product_dao.create(catalogProduct);
    }
    /**
     * update the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     */
    public void update (CatalogProduct catalogProduct){
        catalog_product_dao.update(catalogProduct);
    }
    /**
     * update the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     */
    public void delete (CatalogProduct catalogProduct){
        catalog_product_dao.delete(catalogProduct);
    }
    //endregion

    //region Category Management
    public Category find_Category(Integer category_id,Integer branch_id){
        return  category_dao.find(category_id,branch_id);
    }
    /**
     * writing the provided category to the DB
     * @param category
     */
    public void create(Category category){
        category_dao.create(category);
    }
    /**
     * update the provided category from the DB
     * @param category
     */
    public void update(Category category){
        category_dao.update(category);
    }
    /**
     * delete the provided category from the DB
     * @param category
     */
    public void delete(Category category){
        category_dao.delete(category);
    }
    //endregion

    //region Contract Management
    public Contract find_Contract(Integer contract_id, Integer branch_id){
        return contract_dao.find(contract_id,branch_id);
    }

    /**
     * write contract to the DB
     * @param contract
     */
    public void create(Contract contract){
        contract_dao.create(contract);
    }

    /**
     * delete contract from DB
     * @param contract
     */
    public void delete(Contract contract){
        contract_dao.delete(contract);
    }

    //region catalog products management
    /**
     * write the connection between a catalog product to its contract in the DB
     * @param catalogProduct
     * @param contract
     */
    public void addCatalogProduct( Contract contract,CatalogProduct catalogProduct){
        contract_dao.addCatalogProduct(contract,catalogProduct);
    }

    /**
     * delete the product from the contract and update the DB
     * @param catalogProduct
     * @param contract
     */
    public void deleteCatalogProduct(Contract contract,CatalogProduct catalogProduct){
        contract_dao.deleteCatalogProduct(contract,catalogProduct);
    }
    //endregion

    //region categories in contract
    /**
     * write the contracts category in the DB
     * @param contract
     * @param category
     */
    public void addCategoryToContract(Contract contract , String category){
        contract_dao.addCategoryToContract(contract,category);
    }

    /**
     * delete a contracts category in the DB
     * @param contract
     * @param category
     */
    public void deleteCategoryFromContract(Contract contract , String category){
        contract_dao.deleteCategoryFromContract(contract,category);
    }
    //endregion
    //endregion

    //region Cost Engineering Management
    public CostEngineering find_CostEngineering(Integer contract_id , Integer branch_id){
        return cost_engineering_dao.find(contract_id,branch_id);
    }
    /**
     * write CostEngineering to the DB
     * @param costEngineering
     */
    public void create(CostEngineering costEngineering ){
        cost_engineering_dao.create(costEngineering);
    }
    /**
     * update CostEngineering to the DB
     * @param costEngineering
     */
    public void update (CostEngineering costEngineering ){
        cost_engineering_dao.update(costEngineering);
    }
    /**
     * delete the cost engineering from the DB
     * @param costEngineering
     */
    public void delete (CostEngineering costEngineering ){
        cost_engineering_dao.delete(costEngineering);
    }
    //region product in cost engineering
    /**
     * add a product to CostEngineering and update the DB
     * @param product
     * @param costEngineering
     */
    public void addProductToCostEngineering(CatalogProduct product , CostEngineering costEngineering ){
        cost_engineering_dao.addProductToCostEngineering(product,costEngineering);
    }

    /**
     * delete a product from cost engineering and update the DB
     * @param product
     * @param costEngineering
     */
    public void deleteProductFromCostEngineering (CatalogProduct product , CostEngineering costEngineering ){
        cost_engineering_dao.deleteProductFromCostEngineering(product,costEngineering);
    }
    //endregion
    //endregion

    //region General Product Management
    public GeneralProduct find_GeneralProduct(Integer general_product_id, Integer branch_id){
        return general_product_dao.find(general_product_id,branch_id);
    }
    public GeneralProduct find_GeneralProductbyCategory(Integer general_product_id, Integer category_id,Integer branch_id){
        return general_product_dao.find(general_product_id,category_id,branch_id);
    }
    /**
     * write general product to the DB
     * @param generalProduct
     */
    public void create(GeneralProduct generalProduct){
        general_product_dao.create(generalProduct);

    }
    /**
     * update general product at the DB
     * @param generalProduct
     */
    public void update(GeneralProduct generalProduct ){
        general_product_dao.update(generalProduct);
    }
    /**
     * delete general product from the DB
     * @param generalProduct
     */
    public void delete(GeneralProduct generalProduct){
        general_product_dao.delete(generalProduct);
    }
    //endregion

    //region Order Management
    public Order find_Order(Integer order_id, Integer branch_id){
        return order_dao.find(order_id,branch_id);
    }
    /**
     * write single order and it associate classes to the DB
     * @param order
     */
    public void create(Order order){
        order_dao.create(order);
    }
    /**
     * update single order and all its products to the DB
     * @param order
     */
    public void update(Order order){
       order_dao.update(order);
    }
    /**
     * delete the order from the DB
     * @param order
     */
    public void delete(Order order){
       order_dao.delete(order);
    }
    /**
     * add a specific product to a single order ant update the DB
     * @param product
     * @param order
     * @param quantity
     * @param price
     */
    public void addCatalogProductToOrder(CatalogProduct product ,Order order,Integer quantity , Float price){
        order_dao.addCatalogProductToOrder(product,order,quantity,price);
    }
    /**
     * delete a specific product from the order and update the DB
     * @param product
     * @param order
     */
    public void deleteCatalogProductFromOrder(Order order,CatalogProduct product){
        order_dao.delete(order,product);
    }
    //endregion

    //region Sale Management
    public Sale find_Sale(Integer sale_id, Integer branch_id){
        return sale_dao.find(sale_id,branch_id);
    }
    /**
     * write Sale object to the DB and all its general product associate to it
     * @param sale
     */
    public void create(Sale sale) {
        sale_dao.create(sale);
    }
    /**
     * update Sale object to the DB and all its general product associate to it
     * @param sale
     */
    public void update(Sale sale){
        sale_dao.update(sale);
    }
    /**
     * delete Sale object to the DB and all its general product associate to it
     * @param sale
     */
    public void delete(Sale sale){
        sale_dao.delete(sale);
    }
    //endregion

    //region Specific Product Management
    /**
     * find and return SpecificProduct object
     * @param specific_product_id
     * @return
     */
    public SpecificProduct find_SpecificProduct(Integer specific_product_id, Integer branch_id){
        return specific_product_dao.find(specific_product_id,branch_id);
    }
    /**
     * write specific product to the DB
     * @param specificProduct
     */
    public void create(SpecificProduct specificProduct){
        specific_product_dao.create(specificProduct);
    }
    /**
     * update specific product to the DB
     * @param specificProduct
     */
    public void update (SpecificProduct specificProduct){
        specific_product_dao.update(specificProduct);
    }
    /**
     * delete specific product to the DB
     * @param specificProduct
     */
    public void delete (SpecificProduct specificProduct) {
        specific_product_dao.delete(specificProduct);
    }
    //endregion

    //region Supplier Management
    /**
     * find and returns the supplierCard object in the repository
     * @param supplier_id
     * @return
     */
    public SupplierCard find_Supplier(Integer supplier_id){
        return supplier_dao.find(supplier_id);
    }
    /**
     * writing supplierCard  and all its contactList to the DB
     * @param supplier
     */
    public void create(SupplierCard supplier){
        supplier_dao.create(supplier);
    }
    /**
     * update supplierCard  and all its contactList to the DB
     * @param supplier
     */
    public void update(SupplierCard supplier){
        supplier_dao.update(supplier);
    }
    /**
     * add a contact to the supplierCard and update the DB
     * @param supplier
     * @param contactName
     */
    public void create_contact(String contactName ,SupplierCard supplier){
        supplier_dao.create_contact(contactName,supplier);
    }
    /**
     * delete a contact from the supplier card and update the DB
     * @param supplier
     * @param contact
     */
    public void delete_contact(String contact , SupplierCard supplier){
        supplier_dao.delete_contact(contact,supplier);
    }
    /**
     * delete a the supplier and update the DB
     * @param supplier
     */
    public void delete(SupplierCard supplier){
        supplier_dao.delete(supplier);
    }
    //endregion
    //endregion

    public void clearCache(){
        catalog_product_dao.clearCache();
        general_product_dao.clearCache();
        specific_product_dao.clearCache();
        category_dao.clearCache();
        sale_dao.clearCache();
        order_dao.clearCache();
        contract_dao.clearCache();
        supplier_dao.clearCache();
        branch_dao.clearCache();
        cost_engineering_dao.clearCache();

    }
    //endregion
}



