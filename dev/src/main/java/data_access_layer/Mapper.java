package data_access_layer;

import bussines_layer.Branch;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.*;
import bussines_layer.supplier_module.Contract;
import bussines_layer.supplier_module.CostEngineering;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import data_access_layer.DAO.*;
import data_access_layer.DTO.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    private CostEngineeringDAO cost_engineering_dao;                                    //will not support cache
    private Dao<IDsDTO,Void> ids_dao;                                                   //will not support cache
    private Dao<catalog_product_in_general_productDTO,Void> catalog_product_in_general_products_dao;
    private Dao<contact_of_supplierDTO,Void> contacts_of_supplier_dao;

    //Constructor
    public Mapper() {
        String databaseUrl = "jdbc:sqlite:SuperLi.db";
        try (ConnectionSource conn = new JdbcConnectionSource(databaseUrl)) {
            this.conn=conn;
            //region setting up DAOs without cache functionality
            this.cost_engineering_dao=DaoManager.createDao(conn,CostEngineeringDTO.class);
            this.ids_dao = DaoManager.createDao(conn,IDsDTO.class);
            this.catalog_product_in_general_products_dao=DaoManager.createDao(conn,catalog_product_in_general_productDTO.class);
            this.contacts_of_supplier_dao = DaoManager.createDao(conn,contact_of_supplierDTO.class);
            //endregion
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedList<SupplierCard> loadSuppliers(){
        LinkedList<SupplierCard> supplierCards=new LinkedList<>();
        try {
            List<SupplierDTO> supplierDTOS = supplier_dao.dao.queryForAll();
            for(SupplierDTO dto:supplierDTOS){
            supplierCards.add(supplier_dao.find(dto.getSupplier_id()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return supplierCards;
    }
    public LinkedList<Branch> loadBranches(){
        LinkedList<Branch> branches = new LinkedList<>();
        try {
            List<BranchDTO> branchDTOS = branch_dao.dao.queryForAll();
            for (BranchDTO dto:branchDTOS){
                branches.add(branch_dao.find(dto.getBranch_id()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return branches;
    }

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
    //endregion

    //region General Product Management
    //endregion

    //region Order Management
    //endregion

    //region Sale Management
    //endregion

    //region Specific Product Management
    //endregion

    //region Supplier Management
    //endregion

    //region Loads
        //region First Loads

    /**
     * load all the next IDS of the system
     * @return
     */
    public IDsDTO loadIDs(){
        IDsDTO ids=null;
        try {
            List<IDsDTO> result =  ids_dao.queryForAll();
            ids = result.get(0);
            return ids;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ids;
    }

    /**
     * load all the branches to the system
     * @return list of Branches
     */


    public LinkedList<Category> loadCategories(Integer branch_id){
        LinkedList<Category> main_categories = new LinkedList<>();
        try {
            CategoryDTO super_category = category_dao.queryForId(0);
            return loadAllsubCategories(super_category,branch_id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return new LinkedList<>();
    }
    private LinkedList<Category> loadAllsubCategories(CategoryDTO categoryDTO,Integer branch_id){
        LinkedList<Category> sub_categories = new LinkedList<>();
        if(categoryDTO.getLevel()==2){
            for (CategoryDTO sub_dto:categoryDTO.getSub_categories()){
                    List<GeneralProduct> generalProducts = loadAllGeneralProductInCategory(sub_dto.getGeneralProducts(),branch_id);
                    Category lowest_cat = new Category(sub_dto,null,generalProducts);
                    sub_categories.add(lowest_cat);
            }
        }
        else{
            for (CategoryDTO sub_dto:categoryDTO.getSub_categories()){
                Category new_cat = new Category(sub_dto,loadAllsubCategories(sub_dto,branch_id),null);
                sub_categories.add(new_cat);
            }
        }
        return sub_categories;
    }
    private List<GeneralProduct> loadAllGeneralProductInCategory(ForeignCollection<GeneralProductDTO> generalProducts, Integer branch_id) {
        LinkedList<GeneralProduct> generalProducts1 = new LinkedList<>();
        for (GeneralProductDTO gpDTO:generalProducts){
            if(gpDTO.getBranch_id().getBranch_id()==branch_id){
                generalProducts1.add(loadGeneralProductinBranch(gpDTO));
            }
        }
        return generalProducts1;
    }

    //endregion
        //region After Selection of branch
         public GeneralProduct loadGeneralProductinBranch(GeneralProductDTO dto){//
             GeneralProduct generalProduct = new GeneralProduct(dto);
             LinkedList<SpecificProduct> specificProducts = new LinkedList<>();
             LinkedList<CatalogProduct> catalogProducts = new LinkedList<>();
             for (SpecificProductDTO specificProductDTO:dto.getSpecific_products()){
                 specificProducts.add(new SpecificProduct(specificProductDTO));
             }
             for(catalog_product_in_general_productDTO connection:dto.getCatalog_products()){
                 catalogProducts.add(new CatalogProduct(connection.getCatalogID()));
             }
             generalProduct.setCatalog_products(catalogProducts);
             generalProduct.setProducts(specificProducts);
             return generalProduct;
         }
         public LinkedList<Sale> loadSalesinBranch(Integer branch_id){
            LinkedList<Sale> sales = new LinkedList<>();
             try {
                 List<SaleDTO> saleDTOS = sale_dao.queryBuilder().where().eq("branch_id",branch_id).query();
                 for (SaleDTO saleDTO:saleDTOS){
                     LinkedList<GeneralProduct> generalProducts = new LinkedList<>();
                     for (general_product_on_saleDTO general_product_on_saleDTO: saleDTO.getGeneral_product_on_sale()){
                         generalProducts.add(new GeneralProduct(general_product_on_saleDTO.getGeneralProduct()));
                     }
                    sales.add(new Sale(saleDTO, generalProducts));
                 }
             } catch (SQLException throwables) {
                 throwables.printStackTrace();
             }
            return sales;
         }
         public LinkedList<Contract> loadContractinBranch(Integer branch_id){
        LinkedList<Contract> contracts = new LinkedList<>();
             try {
                 List<ContractDTO> contractDTOS = contract_dao.queryBuilder().where().eq("branch_id",branch_id).query();
                 for(ContractDTO contractDTO:contractDTOS){
                     CostEngineering costEngineering = loadCostEngineering(contractDTO.getProduct_in_cost_engineering());
                     LinkedList<CatalogProduct> catalogProducts = loadCatalogProducts(contractDTO.getCatalog_product());
                     LinkedList<String> categories = loadCategoriesinContract(contractDTO.getCategories_in_contract());
                     //SupplierCard supplierCard = new SupplierCard(contractDTO.getSupplier());
                     contracts.add(new Contract(contractDTO,,costEngineering,catalogProducts,categories));
                 }
             } catch (SQLException throwables) {
                 throwables.printStackTrace();
             }
             //todo: load all contract in the branch
            //todo: load all costEngenieering in contract
             //todo:load all catalog product in contract
             //todo: load all categories in contract
             return contracts;
         }

    private CostEngineering loadCostEngineering(ForeignCollection<CostEngineeringDTO> product_in_cost_engineering) {
        LinkedList<CostEngineeringDTO> linker = new LinkedList<>();
        for (CostEngineeringDTO costEngineeringDTO:product_in_cost_engineering){linker.add(costEngineeringDTO);}
        return new CostEngineering(linker);
    }

    private LinkedList<CatalogProduct> loadCatalogProducts(ForeignCollection<catalog_product_in_contractDTO> catalog_products_in_contract) {
        LinkedList<CatalogProduct> catalogProducts = new LinkedList<>();
        for(catalog_product_in_contractDTO catalog_product_in_contrac:catalog_products_in_contract){
            catalogProducts.add(new CatalogProduct(catalog_product_in_contrac.getCatalog_id()));
        }
        return catalogProducts
    }

    private LinkedList<String> loadCategoriesinContract(ForeignCollection<categories_in_contractDTO> categories_in_contract) {
        LinkedList<String> categories = new LinkedList<>();
        for (categories_in_contractDTO category: categories_in_contract){categories.add(category.getCategory());}
        return categories;
    }

    public void loadOrdersinBranch(Integer branch_id){
            //todo:load all the order associate to the branch;
             //todo: load all the catalog_product_in_order
         }

        //endregion


    //endregion

    //endregion

    //region Utilities
    private String concatObjectList(List list){
        String string="";
        for (Object object:list){string=string.concat(object.toString().concat("\n"));}
        return string;
    }
    //endregion

}



