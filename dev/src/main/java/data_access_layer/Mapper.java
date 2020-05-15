package data_access_layer;

import bussines_layer.Branch;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.*;
import bussines_layer.supplier_module.Contract;
import bussines_layer.supplier_module.CostEngineering;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.ForeignCollection;
import com.sun.org.apache.xml.internal.resolver.Catalog;
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

    //region branch
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
     * change the Branch name and update the DB
     * @param branch
     */
    public void update(Branch branch){
        try{
            BranchDTO branchDTO=new BranchDTO(branch);
            branch_dao.update(branchDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * delete Branch from the DB
     * @param branch
     */
    public void delete(Branch branch){
        try{
            BranchDTO branchDTO=new BranchDTO(branch);
            branch_dao.delete(branchDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    //endregion

    //region contract

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
     * update contract and save changes at the DB
     * @param contract
     */
    public void update(Contract contract){
        try{
            ContractDTO contractDTO = new ContractDTO(contract);
            contract_dao.update(contractDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * delete contract from DB
     * @param contract
     */
    public void delete(Contract contract){
        try{
            ContractDTO contractDTO = new ContractDTO(contract);
            contract_dao.delete(contractDTO);
            //catalog_product and product_in_cost_engineering and categories_in_contract are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    //region product in contract
    /**
     * write the connection between a catalog product to its contract in the DB
     * @param catalogProduct
     * @param contract
     */
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
     * delete the product from the contract and update the DB
     * @param catalogProduct
     * @param contract
     */
    public void delete (CatalogProduct catalogProduct , Contract contract){
        try {
            DeleteBuilder<catalog_product_in_contractDTO,Void> deleteBuilder = catalog_product_in_contract_dao.deleteBuilder();
            // only delete the rows on "contract_id" and "catalog_id" and "branch_id"
            deleteBuilder.where().eq("contract_id" , contract.getContractID()).and().eq("branch_id" , contract.getBranchID()).and().eq("catalog_id" , catalogProduct.getCatalogID());
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //endregion

    //region categories in contract
    /**
     * write the contracts category in the DB
     * @param contract
     * @param category
     */
    public void create(Contract contract , String category){
        categories_in_contractDTO categories_in_contractDTO = new categories_in_contractDTO(contract , category);
        try {
            categories_in_contract_dao.create(categories_in_contractDTO);
            System.err.println(String.format("[Writing] %s", categories_in_contractDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * delete a contracts category in the DB
     * @param contract
     * @param category
     */
    public void delete (Contract contract , String category){
        try {
            DeleteBuilder<categories_in_contractDTO,Void> deleteBuilder = categories_in_contract_dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and category
            deleteBuilder.where().eq("contract_id" , contract.getContractID()).and().eq("branch_id" , contract.getBranchID()).and().eq("category" , category);
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //endregion

    //region Supplier
    /**
     * writing supplierCard  and all its contactList to the DB
     * @param supplier
     */
    public void create(SupplierCard supplier){
        SupplierDTO supplierDTO = new SupplierDTO(supplier);
        List<String> contactList = supplier.getContactsName();
        LinkedList<contact_of_supplierDTO> contact_of_supplierDTOS = new LinkedList<>();
        for(String contact:contactList){
            contact_of_supplierDTOS.add(new contact_of_supplierDTO(supplierDTO,contact));
        }
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
     * update supplierCard  and all its contactList to the DB
     * @param supplier
     */
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
    /**
     * delete a the supplier and update the DB
     * @param supplier
     */
    public void delete(SupplierCard supplier){
        try{
            SupplierDTO supplierDTO = new SupplierDTO(supplier);
            supplier_dao.delete(supplierDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    //region contact of supplier
    /**
     * add a contact to the supplierCard and update the DB
     * @param supplier
     * @param contactName
     */
    public void create(String contactName ,SupplierCard supplier){
        contact_of_supplierDTO contact_of_supplierDTO = new contact_of_supplierDTO(supplier,contactName);
        try {
            contacts_of_supplier_dao.create(contact_of_supplierDTO);
            //System.err.println(String.format("[Writing] %s", concatObjectList(contact_of_supplierDTO)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * delete a contact from the supplier card and update the DB
     * @param supplier
     * @param contact
     */
    public void delete(String contact , SupplierCard supplier){
        try {
            DeleteBuilder<contact_of_supplierDTO,Void> deleteBuilder = contacts_of_supplier_dao.deleteBuilder();
            // only delete the rows on "order_id" and "catalog_id"
            deleteBuilder.where().eq("supplier_id" , supplier.getId()).and().eq("name" , contact);
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //endregion

    //endregion

    //region cost engineering

    /**
     * write CostEngineering to the DB
     * @param costEngineering
     * @param contract
     */
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
     * update CostEngineering to the DB
     * @param costEngineering
     * @param contract
     */
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


    //region product in cost engineering
    /**
     * add a product to CostEngineering and update the DB
     * @param product
     * @param costEngineering
     * @param contract
     */
    public void create(CatalogProduct product , CostEngineering costEngineering ,Contract contract){
        try {
            CostEngineeringDTO costEngineeringDTO = new CostEngineeringDTO(contract , product.getCatalogID() , costEngineering.getMinQuntity().get(product.getCatalogID()) , costEngineering.getNewPrice().get(product.getCatalogID()) );
            cost_engineering_dao.create(costEngineeringDTO);
            System.err.println(String.format("[Writing] %s", costEngineeringDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update the product quantity or price in CostEngineering and update the DB
     * @param product
     * @param costEngineering
     * @param contract
     */
    public void update(CatalogProduct product , CostEngineering costEngineering ,Contract contract){
        try {
            UpdateBuilder<CostEngineeringDTO,Void> updateBuilder = cost_engineering_dao.updateBuilder();
            updateBuilder.where().eq("contract_id", contract.getContractID()).and().eq("branch_id" , contract.getBranchID()).and().eq("catalog_id" , product.getCatalogID());
            // update the field(s)
            updateBuilder.updateColumnValue("min_quantity" ,costEngineering.getMinQuntity().get(product.getCatalogID()));
            updateBuilder.updateColumnValue("discount_price" , costEngineering.getNewPrice().get(product.getCatalogID()));
            updateBuilder.update();
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
            DeleteBuilder<CostEngineeringDTO,Void> deleteBuilder = cost_engineering_dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("contract_id" , contract.getContractID()).and().eq("branch_id" , contract.getBranchID()).and().eq("catalog_id" , product.getCatalogID());
            deleteBuilder.delete();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //endregion

    //endregion

    //endregion

    //region general product
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
     * update general product at the DB
     * @param generalProduct
     */
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
            }
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete general product from the DB
     * @param generalProduct
     */
    public void delete(GeneralProduct generalProduct){
        try{
            GeneralProductDTO generalProductDTO = new GeneralProductDTO(generalProduct);
            general_product_dao.delete(generalProductDTO);
            //all specific products and category are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    //region category
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
     * update the provided category from the DB
     * @param category
     */
    public void update(Category category){
        try{
            CategoryDTO categoryDTO=new CategoryDTO(category);
            category_dao.update(categoryDTO);
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete the provided category from the DB
     * @param category
     */
    public void delete(Category category){
        try{
            CategoryDTO categoryDTO=new CategoryDTO(category);
            category_dao.delete(categoryDTO);
        }catch (Exception e){e.printStackTrace();}
    }
    //endregion

    //region catalog product
    /**
     * write the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     * @param generalProduct
     */
    public void create(CatalogProduct catalogProduct , GeneralProduct generalProduct){
        catalog_product_in_general_productDTO catalog_product_in_general_productDTO = new catalog_product_in_general_productDTO(generalProduct , catalogProduct);
        CatalogProductDTO catalogProductDTO = new CatalogProductDTO(catalogProduct);
        try {
            catalog_product_in_general_products_dao.create(catalog_product_in_general_productDTO);
            catalog_product_dao.create(catalogProductDTO);
            System.err.println(String.format("[Writing] %s", catalog_product_in_general_productDTO));
            System.err.println(String.format("[Writing] %s", catalogProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     * @param generalProduct
     */
    public void update (CatalogProduct catalogProduct , GeneralProduct generalProduct){
        CatalogProductDTO catalogProductDTO = new CatalogProductDTO(catalogProduct);
        try {
            catalog_product_dao.update(catalogProductDTO);
            System.err.println(String.format("[Writing] %s", catalogProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     * @param generalProduct
     */
    public void delete (CatalogProduct catalogProduct , GeneralProduct generalProduct){
        try {

        CatalogProductDTO catalogProductDTO = new CatalogProductDTO(catalogProduct);
        catalog_product_dao.delete(catalogProductDTO);
        System.err.println(String.format("[Writing] %s", catalogProductDTO));
//CASCADE?
//            DeleteBuilder<catalog_product_in_general_productDTO,Void> deleteBuilder = catalog_product_in_general_products_dao.deleteBuilder();
//            // only delete the rows on "catalog_id" and "GPID" and "branch_id"
//            deleteBuilder.where().eq("catalog_id" , catalogProduct.getCatalogID()).and().eq("GPID" , generalProduct.getGpID()).and().eq("branch_id" , generalProduct.getBranch_id());
//            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //endregion

    //region sale
    /**
     * write Sale object to the DB and all its general product associate to it
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
     * update Sale object to the DB and all its general product associate to it
     * @param sale
     */
    public void update(Sale sale){
        try{
            SaleDTO saleDTO = new SaleDTO(sale);
            sale_dao.update(saleDTO);
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete Sale object to the DB and all its general product associate to it
     * @param sale
     */
    public void delete(Sale sale){
        try{
            SaleDTO saleDTO = new SaleDTO(sale);
            sale_dao.delete(saleDTO);
            //general_product_on_sale are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }
    //endregion

    //region specific product
    /**
     * write specific product to the DB
     * @param specificProduct
     * @param generalProduct
     */
    public void create(SpecificProduct specificProduct , GeneralProduct generalProduct){
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(generalProduct , specificProduct);
        try {
            specific_product_dao.create(specificProductDTO);
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update specific product to the DB
     * @param specificProduct
     * @param generalProduct
     */
    public void update (SpecificProduct specificProduct , GeneralProduct generalProduct){
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(generalProduct , specificProduct);
        try {
            specific_product_dao.update(specificProductDTO);
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * delete specific product to the DB
     * @param specificProduct
     * @param generalProduct
     */
    public void delete (SpecificProduct specificProduct , GeneralProduct generalProduct) {
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(generalProduct , specificProduct);
        try {
            specific_product_dao.delete(specificProductDTO);
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //endregion
    //endregion

    //region order
    /**
     * write single order and it associate classes to the DB
     * @param order
     * @param branch_id
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
     * update single order and all its products to the DB
     * @param order
     * @param branchId
     */
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
     * add a specific product to a single order ant update the DB
     * @param product
     * @param order
     * @param quantity
     * @param price
     * @param branchId
     */
    public void create(CatalogProduct product ,Order order,Integer quantity , Float price , Integer branchId){
        try {
            catalog_product_in_orderDTO catalog_product_in_orderDTO = new catalog_product_in_orderDTO(order, product, quantity, price , branchId);
            catalog_product_in_order_dao.create(catalog_product_in_orderDTO);
            System.err.println(String.format("[Writing] %s", catalog_product_in_orderDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * delete a specific product from the order and update the DB
     * @param product
     * @param order
     * @param branchId
     */
    public void delete(CatalogProduct product , Order order , Integer branchId){
        try{
            DeleteBuilder<catalog_product_in_orderDTO,Void> deleteBuilder = catalog_product_in_order_dao.deleteBuilder();
            // only delete the rows on "order_id" and "catalog_id"
            deleteBuilder.where().eq("order_id" , order.getOrderID()).and().eq("catalog_id" , product.getCatalogID());
            deleteBuilder.delete();
        }catch (Exception e){e.printStackTrace();}
    }
    //endregion


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
    public LinkedList<Branch> loadBranches(){
        LinkedList<Branch> branches = new LinkedList<>();
        try {
            List<BranchDTO> result = branch_dao.queryForAll();
            for (BranchDTO branch:result){branches.add(new Branch(branch.getBranch_id(),branch.getName()));}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return branches;
    }
    public LinkedList<SupplierCard> loadSuppliers(){
        LinkedList<SupplierCard> suppliers = new LinkedList<>();
        try {
            List<SupplierDTO> suppliersDTOs =supplier_dao.queryForAll();
            //creating the supplier object
            for(SupplierDTO dto:suppliersDTOs){
                LinkedList<String> contactNames = new LinkedList<>();
                //assign contact list to the supplier
                for(contact_of_supplierDTO contact:dto.getContact_list()){contactNames.add(contact.getName());}
                suppliers.add(new SupplierCard(dto,contactNames));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return suppliers;
    }
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



