package data_access_layer.DAO;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.SpecificProduct;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.GeneralProductDTO;
import data_access_layer.DTO.SpecificProductDTO;
import data_access_layer.DTO.catalog_product_in_general_productDTO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class GeneralProductDAO {
    //fields:
    HashMap<Integer, GeneralProduct> identityMap;
    public Dao<GeneralProductDTO,Void> dao;
    //Constructor

    public GeneralProductDAO(ConnectionSource conn) {
        try {
            this.identityMap=new HashMap<>();
            this.dao= DaoManager.createDao(conn,GeneralProductDTO.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public GeneralProduct find(Integer general_product_id, Integer branch_id){
        GeneralProduct generalProduct=null;
        if(identityMap.containsKey(general_product_id)){
            generalProduct=identityMap.get(general_product_id);
        }
        else{
            try {
                generalProduct = new GeneralProduct(dao.queryBuilder().where().eq("GPID",general_product_id).and().eq("branch_id",branch_id).queryForFirst());
                identityMap.put(general_product_id,generalProduct);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return generalProduct;
    }
    public GeneralProduct find(Integer general_product_id, Integer category_id,Integer branch_id){
        GeneralProduct generalProduct=null;
        if(identityMap.containsKey(general_product_id)){
            generalProduct=identityMap.get(general_product_id);
        }
        else{
            try {
                generalProduct = new GeneralProduct(dao.queryBuilder().where().eq("GPID",general_product_id).and().eq("branch_id",branch_id).and().eq("category_id",category_id).queryForFirst());
                identityMap.put(general_product_id,generalProduct);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return generalProduct;
    }
    /**
     * write general product to the DB
     * @param generalProduct
     */
    public void create(GeneralProduct generalProduct){
        if(!identityMap.containsKey(generalProduct.getGpID())){identityMap.put(generalProduct.getGpID() , generalProduct);}
        GeneralProductDTO generalProductDTO = new GeneralProductDTO(generalProduct);
        try {
            dao.create(generalProductDTO);
            System.err.println(String.format("[Writing] %s", generalProductDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /**
     * update general product at the DB
     * @param generalProduct
     */
    public void update(GeneralProduct generalProduct ){
        if(identityMap.containsKey(generalProduct.getGpID())){identityMap.replace(generalProduct.getGpID() , generalProduct);}
        try{
            GeneralProductDTO generalProductDTO = new GeneralProductDTO(generalProduct);
            UpdateBuilder<GeneralProductDTO, Void> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("GPID", generalProduct.getGpID()).and().eq("branch_id" , generalProduct.getBranch_id());
            updateBuilder.updateColumnValue("manufacture" ,generalProduct.getManufacture());
            updateBuilder.updateColumnValue("name" , generalProduct.getName());
            updateBuilder.updateColumnValue("retail_price" , generalProduct.getRetail_price());
            updateBuilder.updateColumnValue("quantity" , generalProduct.getQuantity());
            updateBuilder.updateColumnValue("min_quantity" , generalProduct.getMin_quantity());
            updateBuilder.updateColumnValue("sale_price" , generalProduct.getSale_price());
            updateBuilder.updateColumnValue("category_id" , generalProduct.getCategory_id());
            updateBuilder.update();

            /*          for(SpecificProduct specificProduct:generalProduct.getProducts()){
                specificProductDAO.update(specificProduct);
            }
            for(CatalogProduct catalogProduct:generalProduct.getCatalog_products()){
                catalog_product_in_general_products_dao.update(new catalog_product_in_general_productDTO(generalProductDTO,catalogProduct));
            }*/
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete general product from the DB
     * @param generalProduct
     */
    public void delete(GeneralProduct generalProduct){
        if (identityMap.containsKey(generalProduct.getGpID())){identityMap.remove(generalProduct.getGpID(),generalProduct);}
        try{
            GeneralProductDTO generalProductDTO = new GeneralProductDTO(generalProduct);
            DeleteBuilder<GeneralProductDTO,Void> deleteBuilder = dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("GPID", generalProduct.getGpID()).and().eq("branch_id" , generalProduct.getBranch_id());
            deleteBuilder.delete();
            System.err.println(String.format("[Writing] %s", generalProductDTO));
            //all specific products and category are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }


}
