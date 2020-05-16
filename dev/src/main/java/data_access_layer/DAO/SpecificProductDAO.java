package data_access_layer.DAO;

import bussines_layer.Branch;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.SpecificProduct;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.CatalogProductDTO;
import data_access_layer.DTO.SpecificProductDTO;

import java.sql.SQLException;
import java.util.HashMap;

public class SpecificProductDAO {
    //fields:
    HashMap<Integer, SpecificProduct> identityMap;
    Dao<SpecificProductDTO,Void> dao;

    //Constructor
    public SpecificProductDAO(ConnectionSource conn)
    {
        try {
            this.identityMap=new HashMap<>();
            this.dao = DaoManager.createDao(conn,SpecificProductDTO.class);
            this.dao.setObjectCache(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * find and return SpecificProduct object
     * @param specific_product_id
     * @return
     */
    public SpecificProduct find(Integer specific_product_id, Integer branch_id){
        SpecificProduct specificProduct=null;
        if(identityMap.containsKey(specific_product_id)){
            specificProduct=identityMap.get(specific_product_id);
        }
        else{
            try {
                specificProduct = new SpecificProduct(dao.queryBuilder().where().eq("branch_id",branch_id).and().eq("specific_product_id",specific_product_id).queryForFirst());
                identityMap.put(specific_product_id,specificProduct);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return specificProduct;
    }
    /**
     * write specific product to the DB
     * @param specificProduct
     */
    public void create(SpecificProduct specificProduct){
        if(!identityMap.containsKey(specificProduct.getId())){identityMap.put(specificProduct.getId(),specificProduct);}
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(specificProduct);
        try {
            dao.create(specificProductDTO);
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update specific product to the DB
     * @param specificProduct
     */
    public void update (SpecificProduct specificProduct){
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(specificProduct);
        if(identityMap.containsKey(specificProduct.getId())){identityMap.replace(specificProduct.getId(),specificProduct);}

        try {
            UpdateBuilder<SpecificProductDTO, Void> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("GPID", specificProduct.getGpId()).and().eq("branch_id" , specificProduct.getBranch_id()).and().eq("specific_product_id" , specificProduct.getId());
            updateBuilder.updateColumnValue("location" ,specificProduct.getLocation());
            updateBuilder.updateColumnValue("expiration_date" ,specificProduct.getExpiration_date());
            updateBuilder.updateColumnValue("flaw_flag" ,specificProduct.getFlaw_flag());
            updateBuilder.update();
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * delete specific product to the DB
     * @param specificProduct
     */
    public void delete (SpecificProduct specificProduct) {
        SpecificProductDTO specificProductDTO = new SpecificProductDTO(specificProduct);
        if(identityMap.containsKey(specificProduct.getId())){identityMap.remove(specificProduct.getId(),specificProduct);}
        try {
            DeleteBuilder<SpecificProductDTO,Void> deleteBuilder = dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("GPID", specificProduct.getGpId()).and().eq("branch_id" , specificProduct.getBranch_id()).and().eq("specific_product_id" , specificProduct.getId());
            deleteBuilder.delete();
            System.err.println(String.format("[Writing] %s", specificProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //region Utilities
    public void clearCache(){
        this.identityMap.clear();
    }
    //endregion
}
