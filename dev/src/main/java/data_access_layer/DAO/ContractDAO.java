package data_access_layer.DAO;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.supplier_module.Contract;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.ContractDTO;
import data_access_layer.DTO.OrderDTO;
import data_access_layer.DTO.catalog_product_in_contractDTO;
import data_access_layer.DTO.categories_in_contractDTO;

import java.sql.SQLException;
import java.util.HashMap;

public class ContractDAO {
    //fields:
    HashMap<Integer, Contract> identityMap;
    public Dao<ContractDTO,Void> dao;
    public Dao<categories_in_contractDTO,Void> categories_in_contract_dao;
    public Dao<catalog_product_in_contractDTO,Void> catalog_product_in_contract_dao;
    //Constructor

    public ContractDAO(ConnectionSource conn) {
        try {
            this.identityMap=new HashMap<>();
            this.dao= DaoManager.createDao(conn,ContractDTO.class);
            this.catalog_product_in_contract_dao=DaoManager.createDao(conn,catalog_product_in_contractDTO.class);
            this.categories_in_contract_dao = DaoManager.createDao(conn,categories_in_contractDTO.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Contract find(Integer contract_id, Integer branch_id){
        Contract contract=null;
        if(identityMap.containsKey(contract_id)){
            contract=identityMap.get(contract_id);
        }
        else{
            try {
                contract = new Contract(dao.queryBuilder().where().eq("contract_id",contract_id).and().eq("branch_id",branch_id).queryForFirst());
                identityMap.put(contract_id,contract);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return contract;
    }

    /**
     * write contract to the DB
     * @param contract
     */
    public void create(Contract contract){
        ContractDTO contractDTO = new ContractDTO(contract);
        try {
            if (!identityMap.containsKey(contract.getBranchID())){identityMap.put(contract.getContractID(),contract);}
            dao.create(contractDTO);
            for (CatalogProduct catalogProduct:contract.getProducts().getData()){addCatalogProduct(contract,catalogProduct);}
            System.err.println(String.format("[Writing] %s", contractDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * delete contract from DB
     * @param contract
     */
    public void delete(Contract contract){
        try{
            if (identityMap.containsKey(contract.getContractID())){identityMap.remove(contract.getContractID(),contract);}
            ContractDTO contractDTO = new ContractDTO(contract);
            DeleteBuilder<ContractDTO,Void> deleteBuilder = dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("contract_id",contract.getContractID()).and().eq("branch_id" ,contract.getBranchID()).and().eq("supplier_id",contract.getSupplierID());
            deleteBuilder.delete();
            System.err.println(String.format("[Delete] %s", contractDTO));
            //catalog_product_in_contract are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    //region catalog products management
    /**
     * write the connection between a catalog product to its contract in the DB
     * @param catalogProduct
     * @param contract
     */
    public void addCatalogProduct( Contract contract,CatalogProduct catalogProduct){
        catalog_product_in_contractDTO catalog_product_in_contractDTO = new catalog_product_in_contractDTO(contract , catalogProduct);
        try {
            if (identityMap.containsKey(contract.getContractID())){identityMap.replace(contract.getContractID(),contract);}
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
    public void deleteCatalogProduct(Contract contract,CatalogProduct catalogProduct){
        try {
            if (identityMap.containsKey(contract.getContractID())){identityMap.replace(contract.getContractID(),contract);}
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
    public void addCategoryToContract(Contract contract , String category){
        categories_in_contractDTO categories_in_contractDTO = new categories_in_contractDTO(contract , category);
        try {
            if (identityMap.containsKey(contract.getContractID())){identityMap.replace(contract.getContractID(),contract);}
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
    public void deleteCategoryFromContract(Contract contract , String category){
        try {
            if (identityMap.containsKey(contract.getContractID())){identityMap.replace(contract.getContractID(),contract);}
            DeleteBuilder<categories_in_contractDTO,Void> deleteBuilder = categories_in_contract_dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and category
            deleteBuilder.where().eq("contract_id" , contract.getContractID()).and().eq("branch_id" , contract.getBranchID()).and().eq("category" , category);
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void clearCache() {
        this.identityMap.clear();
    }
    //endregion
}
