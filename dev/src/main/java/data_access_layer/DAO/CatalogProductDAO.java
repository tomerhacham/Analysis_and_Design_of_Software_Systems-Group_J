package data_access_layer.DAO;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.SpecificProduct;
import bussines_layer.supplier_module.Contract;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.CatalogProductDTO;
import data_access_layer.DTO.CostEngineeringDTO;
import data_access_layer.DTO.catalog_product_in_general_productDTO;
import data_access_layer.DTO.categories_in_contractDTO;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.HashMap;

public class CatalogProductDAO {
    HashMap<Integer, CatalogProduct> identityMap; //assuming that all the object are relevant for the currently active branch in the business layer
    Dao<CatalogProductDTO,Void>dao;
    Dao<catalog_product_in_general_productDTO,Void> catalog_product_in_general_products_dao;

    //Constructor

    public CatalogProductDAO(ConnectionSource conn) {
        try {
            this.identityMap=new HashMap<>();
            this.dao= DaoManager.createDao(conn,CatalogProductDTO.class);
            this.dao.setObjectCache(true);
            this.catalog_product_in_general_products_dao=DaoManager.createDao(conn,catalog_product_in_general_productDTO.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public CatalogProduct find(Integer catalog_id,Integer branch_id){
        CatalogProduct catalogProduct=null;
        if(identityMap.containsKey(catalog_id)){
            catalogProduct=identityMap.get(catalog_id);
        }
        else{
            try {
                catalogProduct = new CatalogProduct(dao.queryBuilder().where().eq("catalog_id",catalog_id).and().eq("branch_id",branch_id).queryForFirst());
                identityMap.put(catalog_id,catalogProduct);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return catalogProduct;
    }
    /**
     * write the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     */
    public void create(CatalogProduct catalogProduct){
        if(!identityMap.containsKey(catalogProduct.getCatalogID())){identityMap.put(catalogProduct.getCatalogID(),catalogProduct);}
        catalog_product_in_general_productDTO catalog_product_in_general_productDTO = new catalog_product_in_general_productDTO(catalogProduct);
        CatalogProductDTO catalogProductDTO = new CatalogProductDTO(catalogProduct);
        try {
            catalog_product_in_general_products_dao.create(catalog_product_in_general_productDTO);
            dao.create(catalogProductDTO);
            System.err.println(String.format("[Writing] %s", catalog_product_in_general_productDTO));
            System.err.println(String.format("[Writing] %s", catalogProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     */
    public void update (CatalogProduct catalogProduct){
        if(identityMap.containsKey(catalogProduct.getCatalogID())){identityMap.replace(catalogProduct.getCatalogID(),catalogProduct);}
        CatalogProductDTO catalogProductDTO = new CatalogProductDTO(catalogProduct);
        try {
            UpdateBuilder<CatalogProductDTO, Void> updateBuilder = dao.updateBuilder();
                updateBuilder.where().eq("GPID", catalogProduct.getGpID()).and().eq("branch_id" , catalogProduct.getBranch_id()).and().eq("catalog_id" , catalogProduct.getCatalogID());
                updateBuilder.updateColumnValue("supplier_price" ,catalogProduct.getSupplierPrice());
                updateBuilder.updateColumnValue("supplier_id" ,catalogProduct.getSupplierId());
                updateBuilder.updateColumnValue("supplier_category" ,catalogProduct.getSupplierCategory());
                updateBuilder.updateColumnValue("name" ,catalogProduct.getName());
                updateBuilder.update();
            System.err.println(String.format("[Writing] %s", catalogProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update the connection between a catalog product to its general product in the DB
     * @param catalogProduct
     */
    public void delete (CatalogProduct catalogProduct){
        if(identityMap.containsKey(catalogProduct.getCatalogID())){identityMap.remove(catalogProduct.getCatalogID(),catalogProduct);}
        CatalogProductDTO catalogProductDTO = new CatalogProductDTO(catalogProduct);
        try {
            DeleteBuilder<CatalogProductDTO,Void> deleteBuilder = dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("GPID", catalogProduct.getGpID()).and().eq("branch_id" , catalogProduct.getBranch_id()).and().eq("catalog_id" , catalogProduct.getCatalogID());
            deleteBuilder.delete();
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
    //region Utilities
    public void clearCache(){
        this.identityMap.clear();
    }
    //endregion
}
