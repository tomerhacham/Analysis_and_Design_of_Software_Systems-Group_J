package data_access_layer;

import bussines_layer.Branch;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.*;
import bussines_layer.supplier_module.Contract;
import bussines_layer.supplier_module.CostEngineering;
import bussines_layer.supplier_module.Order;
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

    private Dao<CatalogProductDTO,Integer> catalog_product_dao;
    private Dao<GeneralProductDTO,Integer> general_product_dao;
    private Dao<SpecificProductDTO,Integer> specific_product_dao;
    private Dao<CategoryDTO,Integer> category_dao;
    private Dao<SaleDTO,Integer> sale_dao;
    private Dao<OrderDTO,Integer> order_dao;
    private Dao<ContractDTO,Integer> contract_dao;
    private Dao<SupplierDTO,Integer> supplier_dao;
    private Dao<BranchDTO,Integer> branch_dao;

    private Dao<general_product_on_saleDTO,Void> general_product_on_sale_dao;           //will not support cache
    private Dao<catalog_product_in_orderDTO,Void> catalog_product_in_order_dao;         //will not support cache
    private Dao<CostEngineeringDTO,Void> cost_engineering_dao;                          //will not support cache
    private Dao<catalog_product_in_contractDTO,Void> catalog_product_in_contract_dao;   //will not support cache
    private Dao<IDsDTO,Void> ids_dao;                                                   //will not support cache
    private Dao<categories_in_contractDTO,Void> categories_in_contract_dao;             //will not support cache
    private Dao<catalog_product_in_general_productDTO,Void> catalog_product_in_general_products_dao;
    private Dao<contact_of_supplierDTO,Void> contacts_of_supplier_dao;

    //Constructor
    public Mapper() {
        String databaseUrl = "jdbc:sqlite:SuperLi.db";
        try (ConnectionSource conn = new JdbcConnectionSource(databaseUrl)) {
            this.conn=conn;
            //region setting up DAOs with cache functionality
            this.catalog_product_dao=DaoManager.createDao(conn,CatalogProductDTO.class);
            this.catalog_product_dao.setObjectCache(true);

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
            this.catalog_product_in_order_dao = DaoManager.createDao(conn, catalog_product_in_orderDTO.class);
            this.cost_engineering_dao=DaoManager.createDao(conn,CostEngineeringDTO.class);
            this.catalog_product_in_contract_dao=DaoManager.createDao(conn,catalog_product_in_contractDTO.class);
            this.ids_dao = DaoManager.createDao(conn,IDsDTO.class);
            this.categories_in_contract_dao = DaoManager.createDao(conn,categories_in_contractDTO.class);
            this.catalog_product_in_general_products_dao=DaoManager.createDao(conn,catalog_product_in_general_productDTO.class);
            this.contacts_of_supplier_dao = DaoManager.createDao(conn,contact_of_supplierDTO.class);
            //endregion
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region Methods

    //region Creates
    /**
     * write Branch to the DB
     * @param branch
     */
    public void create(Branch branch){
        BranchDTO branchDTO=new BranchDTO(branch);
        try{branch_dao.create(branchDTO);}
        catch(Exception e){e.printStackTrace();}
    }
    /**
     * writing the provided category to the DB
     * @param category
     * @param super_category_id
     */
    public void create(Category category,Integer super_category_id){
        CategoryDTO categoryDTO = new CategoryDTO(category,super_category_id);
        try {
            category_dao.create(categoryDTO);
            System.err.println(String.format("[Writing] %s", categoryDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /**
     * write general product to the DB
     * @param generalProduct
     */
    public void create(GeneralProduct generalProduct){
        GeneralProductDTO generalProductDTO = new GeneralProductDTO(generalProduct);
        try {
            general_product_dao.create(generalProductDTO);
            System.err.println(String.format("[Writing] %s", generalProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /**
     * write contract to the DB
     * @param contract
     */
    public void create(Contract contract){
        ContractDTO contractDTO = new ContractDTO(contract);
        try {
            contract_dao.create(contractDTO);
            System.err.println(String.format("[Writing] %s", contractDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * write single order and it associate classes to the DB
     * @param order
     */
    public void create(Order order,Integer branch_id){
        OrderDTO orderDTO = new OrderDTO(order,branch_id);
        LinkedList<catalog_product_in_orderDTO> catalog_product_in_order = new LinkedList<>();
        for(CatalogProduct product:order.getProductsAndPrice().keySet()){
            catalog_product_in_order.add(new catalog_product_in_orderDTO(orderDTO,product,order.getProductsAndQuantity().get(product),order.getProductsAndPrice().get(product)));
        }
        try {
            order_dao.create(orderDTO);
            System.err.println(String.format("[Writing] %s", orderDTO));
            catalog_product_in_order_dao.create(catalog_product_in_order);
            System.err.println(String.format("[Writing] %s", concatObjectList(catalog_product_in_order)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * writing supplierCard  and all its contactList to the DB
     * @param supplier
     */
    public void create(SupplierCard supplier){
        SupplierDTO supplierDTO = new SupplierDTO(supplier);
        List<String> contactList = supplier.getContactsName();
        LinkedList<contact_of_supplierDTO> contact_of_supplierDTOS = new LinkedList<>();
        for(String contact:contactList){contact_of_supplierDTOS.add(new contact_of_supplierDTO(supplierDTO,contact));}
        try {
            supplier_dao.create(supplierDTO);
            System.err.println(String.format("[Writing] %s", supplierDTO));
            contacts_of_supplier_dao.create(contact_of_supplierDTOS);
            System.err.println(String.format("[Writing] %s", concatObjectList(contact_of_supplierDTOS)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * write Sale object to the DB and all its general product associate to is
     * @param sale
     */
    public void create(Sale sale) {
        SaleDTO saleDTO = new SaleDTO(sale);
        LinkedList<general_product_on_saleDTO> general_product_on_sale = new LinkedList<>();
        for (GeneralProduct generalProduct : sale.getProducts_on_sale()) {
            general_product_on_sale.add(new general_product_on_saleDTO(saleDTO, generalProduct));
        }
        try {
            sale_dao.create(saleDTO);
            System.err.println(String.format("[Writing] %s", saleDTO));
            general_product_on_sale_dao.create(general_product_on_sale);
            System.err.println(String.format("[Writing] %s", concatObjectList(general_product_on_sale)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * write CostEngineering to the DB
     * @param costEngineering
     */
    //TODO:4. create CostEngineering
    //TODO - do we need to get as arguments the contractDTO ? or contract?
    public void create(CostEngineering costEngineering ,Contract contract){

        HashMap<Integer , Integer> minQuntity = costEngineering.getMinQuntity(); // <catalogid , quantity>
        HashMap<Integer , Float> newPrice = costEngineering.getNewPrice(); // <catalogid , newPrice>

        try {
            for (Integer catalogid : minQuntity.keySet()) {
                CostEngineeringDTO costEngineeringDTO = new CostEngineeringDTO(contract , catalogid , minQuntity.get(catalogid) , newPrice.get(catalogid) );
                cost_engineering_dao.create(costEngineeringDTO);
                System.err.println(String.format("[Writing] %s", costEngineeringDTO));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * write the connection between a catalog product to its contract in the DB
     * @param catalogProduct
     * @param contract
     */
    //TODO:1. create catalog_product_in_contract
    //TODO - do we need to get as arguments the contractDTO ? or contract?
    public void create(CatalogProduct catalogProduct , Contract contract){
        catalog_product_in_contractDTO catalog_product_in_contractDTO = new catalog_product_in_contractDTO(contract , catalogProduct);
        try {
            catalog_product_in_contract_dao.create(catalog_product_in_contractDTO);
            System.err.println(String.format("[Writing] %s", catalog_product_in_contractDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * write the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     * @param generalProduct
     */
    //TODO:2. create catalog_product_in_generalProduct
    public void create(CatalogProduct catalogProduct , GeneralProduct generalProduct){
        catalog_product_in_general_productDTO catalog_product_in_general_productDTO = new catalog_product_in_general_productDTO(generalProduct , catalogProduct);
        try {
            catalog_product_in_general_products_dao.create(catalog_product_in_general_productDTO);
            System.err.println(String.format("[Writing] %s", catalog_product_in_general_productDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * write the contracts category in the DB
     * @param contract
     * @param category
     */
    //TODO:3. create categories_in_contract
    public void create(Contract contract , String category){
        categories_in_contractDTO categories_in_contractDTO = new categories_in_contractDTO(contract , category);
        try {
            categories_in_contract_dao.create(categories_in_contractDTO);
            System.err.println(String.format("[Writing] %s", categories_in_contractDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void create(SpecificProduct specificProduct , GeneralProduct generalProduct){
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(generalProduct , specificProduct);
        try {
            specific_product_dao.create(specificProductDTO);
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //TODO:5. create ID's

    //region Not Active Function
    /*public void createNOTACTIVE(GeneralProduct generalProduct){
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
    public void createNOTACTIVE(Contract contract){
        //todo: create DTO for contract
        ContractDTO contractDTO = new ContractDTO(contract);
        //todo:create DTO for each category in contract
        LinkedList<categories_in_contractDTO> categories = new LinkedList<>();
        for(String category:contract.getCategories()){
            categories.add(new categories_in_contractDTO(contractDTO,category));
        }
        //todo: create DT for each catalog product in category
        LinkedList<catalog_product_in_contractDTO> catalog_products = new LinkedList<>();
        for(CatalogProduct catalogProduct: contract.getProducts().getData()){
            try {
                List<GeneralProductDTO> generalProduct =  general_product_dao.queryBuilder().where().eq("GPID",catalogProduct.getGpID()).and().eq("branch_id",contract.getBranchID()).query();
                catalog_products.add(new catalog_product_in_contractDTO(contractDTO,catalogProduct,generalProduct.get(0)));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //todo: create DTO for each costEngineering product;
        LinkedList<CostEngineeringDTO> costEngineeringDTOS = new LinkedList<>();
        for(Integer catalogId:contract.getCostEngineering().getMinQuntity().keySet()){
            Integer min_quantity = contract.getCostEngineering().getMinQuntity().get(catalogId);
            Float newPrice = contract.getCostEngineering().getNewPrice().get(catalogId);
            costEngineeringDTOS.add(new CostEngineeringDTO(contractDTO,catalogId,min_quantity,newPrice));
        }
        try{
            contract_dao.create(contractDTO);
            categories_in_contract_dao.create(categories);
            catalog_product_in_contract_dao.create(catalog_products);
            cost_engineering_dao.create(costEngineeringDTOS);
        }
        catch(Exception e){e.printStackTrace();}
    }*/
    //endregion
    //endregion

    //region Updates
    public void update(Branch branch){
        try{
            BranchDTO branchDTO=new BranchDTO(branch);
            branch_dao.update(branchDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    public void update(Category category){
        try{
            CategoryDTO categoryDTO=new CategoryDTO(category);
            category_dao.update(categoryDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    public void update(GeneralProduct generalProduct){
        try{
            GeneralProductDTO generalProductDTO = new GeneralProductDTO(generalProduct);
            LinkedList<SpecificProductDTO> specific_products=new LinkedList<>();
            general_product_dao.update(generalProductDTO);

            for(SpecificProduct specificProduct:generalProduct.getProducts()){
                SpecificProductDTO spDTO = new SpecificProductDTO(generalProductDTO,specificProduct);
                specific_product_dao.update(spDTO);
            }
            for(CatalogProduct catalogProduct:generalProduct.getCatalog_products()){
                catalog_product_in_general_products_dao.update(new catalog_product_in_general_productDTO(generalProductDTO,catalogProduct));
                /*UpdateBuilder<catalog_product_in_general_productDTO,Void> updateBuilder = catalog_product_in_general_products_dao.updateBuilder();
                // set the criteria like you would a QueryBuilder
                updateBuilder.where().eq("catalogID", catalogProduct.getCatalogID()).and().eq("branch_id" , generalProduct.getBranch_id());
                // update the value of your field(s)
                updateBuilder.updateColumnValue("name" ,catalogProduct.getName());
                updateBuilder.updateColumnValue("supplier_price" , catalogProduct.getSupplierPrice());
                updateBuilder.updateColumnValue("supplier_category" , catalogProduct.getSupplierCategory());
                updateBuilder.update();*/
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void update(Contract contract){
        try{
            ContractDTO contractDTO = new ContractDTO(contract);
            contract_dao.update(contractDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    public void update(Order order, Integer branchId){
        try{
            OrderDTO orderDTO = new OrderDTO(order , branchId);
            order_dao.update(orderDTO);

            HashMap<CatalogProduct , Integer> productAndQuantity = order.getProductsAndQuantity();
            HashMap<CatalogProduct , Float> productAndPrice = order.getProductsAndPrice();
            UpdateBuilder<catalog_product_in_orderDTO, Void> updateBuilder = catalog_product_in_order_dao.updateBuilder();

            for(CatalogProduct product:order.getProductsAndPrice().keySet()){
                // set criterias
                updateBuilder.where().eq("order_id", order.getOrderID()).and().eq("catalog_id" , product.getCatalogID());
                // update the field(s)
                updateBuilder.updateColumnValue("quantity" ,productAndQuantity.get(product));
                updateBuilder.updateColumnValue("price" , productAndPrice.get(product));
                updateBuilder.update();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void update(SupplierCard supplier){
        try{
            SupplierDTO supplierDTO = new SupplierDTO(supplier);
            supplier_dao.update(supplierDTO);

            UpdateBuilder<contact_of_supplierDTO, Void> updateBuilder = contacts_of_supplier_dao.updateBuilder();

            for(String contactNamde : supplier.getContactsName()){
                // set criterias
                updateBuilder.where().eq("supplier_id", supplier.getId());
                // update the field(s)
                updateBuilder.updateColumnValue("name" ,contactNamde);
                updateBuilder.update();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void update(Sale sale){
        try{
            SaleDTO saleDTO = new SaleDTO(sale);
            sale_dao.update(saleDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    public void update (CostEngineering costEngineering ,Contract contract){
        try{

            HashMap<Integer , Integer> minQuntity = costEngineering.getMinQuntity(); // <catalogid , quantity>
            HashMap<Integer , Float> newPrice = costEngineering.getNewPrice(); // <catalogid , newPrice>
            UpdateBuilder<CostEngineeringDTO, Void> updateBuilder = cost_engineering_dao.updateBuilder();

            for (Integer catalogid : minQuntity.keySet()) {
                // set criterias
                updateBuilder.where().eq("contract_id", contract.getContractID()).and().eq("branch_id" , contract.getBranchID()).and().eq("catalog_id" , catalogid);
                // update the field(s)
                updateBuilder.updateColumnValue("min_quantity" ,minQuntity.get(catalogid));
                updateBuilder.updateColumnValue("discount_price" , newPrice.get(catalogid));
                updateBuilder.update();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void update (SpecificProduct specificProduct , GeneralProduct generalProduct){
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(generalProduct , specificProduct);
        try {
            specific_product_dao.update(specificProductDTO);
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //endregion

    //region Deletes

    public void delete(Branch branch){
        try{
            BranchDTO branchDTO=new BranchDTO(branch);
            branch_dao.delete(branchDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    public void delete(Category category){
        try{
            CategoryDTO categoryDTO=new CategoryDTO(category);
            category_dao.delete(categoryDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    public void delete(GeneralProduct generalProduct){
        try{
            GeneralProductDTO generalProductDTO = new GeneralProductDTO(generalProduct);
            general_product_dao.delete(generalProductDTO);
            //all specific products and category are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    public void delete(Contract contract){
        try{
            ContractDTO contractDTO = new ContractDTO(contract);
            contract_dao.delete(contractDTO);
            //catalog_product and product_in_cost_engineering and categories_in_contract are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * delete the order from the DB
     * @param order
     */
    public void delete(Order order , Integer branchId){
        try{
            OrderDTO orderDTO = new OrderDTO(order , branchId);
            order_dao.delete(orderDTO);
            //catalog_product_in_order are deleted due to cascade

        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * delete a specific product from the order and update the DB
     * @param order
     */
    public void delete(CatalogProduct product , Order order , Integer branchId){
        try{


        }catch (Exception e){e.printStackTrace();}
    }




    public void delete(SupplierCard supplier){
        try{
            SupplierDTO supplierDTO = new SupplierDTO(supplier);
            supplier_dao.delete(supplierDTO);
            //contact_list are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    public void delete(Sale sale){
        try{
            SaleDTO saleDTO = new SaleDTO(sale);
            sale_dao.delete(saleDTO);
            //general_product_on_sale are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * delete the cost engineering from the DB
     * @param costEngineering
     * @param contract
     */
    public void delete (CostEngineering costEngineering ,Contract contract){
        HashMap<Integer , Integer> minQuntity = costEngineering.getMinQuntity(); // <catalogid , quantity>
        HashMap<Integer , Float> newPrice = costEngineering.getNewPrice(); // <catalogid , newPrice>

        try {
            for (Integer catalogid : minQuntity.keySet()) {
                CostEngineeringDTO costEngineeringDTO = new CostEngineeringDTO(contract , catalogid , minQuntity.get(catalogid) , newPrice.get(catalogid) );
                cost_engineering_dao.delete(costEngineeringDTO);
                System.err.println(String.format("[Writing] %s", costEngineeringDTO));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * delete a product from cost engineering and update the DB
     * @param product
     * @param costEngineering
     * @param contract
     */
    public void delete (CatalogProduct product , CostEngineering costEngineering ,Contract contract){
        try {
            CostEngineeringDTO costEngineeringDTO = new CostEngineeringDTO(contract , product.getCatalogID() , costEngineering.getMinQuntity().get(product.getCatalogID()) , costEngineering.getNewPrice().get(product.getCatalogID()) );
            cost_engineering_dao.delete(costEngineeringDTO);
            System.err.println(String.format("[Writing] %s", costEngineeringDTO));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void delete (CatalogProduct catalogProduct , Contract contract){

        //TODO : update Tomer - there is nothing to update in this table - there are only id's here
    }

    public void delete (CatalogProduct catalogProduct , GeneralProduct generalProduct){

        //TODO : update Tomer - there is nothing to update in this table - there are only id's here
    }

    public void delete (Contract contract , String category){

        //TODO - update Tomer : there is no need to update the category name - you can only delete or add
    }

    public void delete (SpecificProduct specificProduct , GeneralProduct generalProduct){
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(generalProduct , specificProduct);
        try {
            specific_product_dao.update(specificProductDTO);
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


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
     * @return list of Pair<ID,name>
     */
    public LinkedList<Pair<Integer,String>> loadBranches(){
        LinkedList<Pair<Integer,String>> branches = new LinkedList<>();
        try {
            List<BranchDTO> result = branch_dao.queryForAll();
            for (BranchDTO branch:result){branches.add(new Pair<>(branch.getBranch_id(),branch.getName()));}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return branches;
    }

    public void loadSuppliers(){
        //todo:load all supplierCards
    }
    public void loadCategories(){
        //todo:load all Categories
    }
        //endregion
        //region After Selection of branch
         public void loadGeneralProductsinBranch(Integer branch_id){//
            // todo:load generalProduct in branch
            //todo: load specific products (done alreadt with the library(?)
            //todo: load catalog products_in_general_product
            //todo: load all CatalogProducts
         }
         public void loadSalesinBranch(Integer branch_id){
            //todo:load all the sale associate with the branch
             //todo:load all general_product_on_sale
         }
         public void loadContractinBranch(Integer branch_id){
            //todo: load all contract in the branch
            //todo: load all costEngenieering in contract
             //todo:load all catalog product in contract
             //todo: load all categories in contract
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



