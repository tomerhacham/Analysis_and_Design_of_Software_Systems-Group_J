package data_access_layer.DAO;

import bussines_layer.Branch;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.SpecificProduct;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.javafx.image.impl.General;
import data_access_layer.DTO.*;
import data_access_layer.Mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GeneralProductDAO {
    //fields:
    HashMap<Integer, GeneralProduct> identityMap;
    public Dao<GeneralProductDTO,Void> dao;
    public Dao<catalog_product_in_general_productDTO,Void> catalog_product_in_general_products_dao;

    //Constructor

    public GeneralProductDAO(ConnectionSource conn) {
        try {
            this.identityMap=new HashMap<>();
            this.dao= DaoManager.createDao(conn,GeneralProductDTO.class);
            this.catalog_product_in_general_products_dao=DaoManager.createDao(conn,catalog_product_in_general_productDTO.class);
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
                GeneralProductDTO dto =  dao.queryBuilder().where().eq("GPID",general_product_id).and().eq("branch_id",branch_id).queryForFirst();
                if(dto != null) {
                    GeneralProduct gp = new GeneralProduct(dto);
                    gp.setProducts(loadSpecifics(general_product_id,branch_id));
                    gp.setCatalog_products(loadCatalogs(general_product_id,branch_id));
                    generalProduct=gp;
                    identityMap.put(general_product_id, generalProduct);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return generalProduct;
    }
/*    public GeneralProduct find(Integer general_product_id, Integer category_id,Integer branch_id){
        GeneralProduct generalProduct=null;
        if(identityMap.containsKey(general_product_id)){
            generalProduct=identityMap.get(general_product_id);
        }
        else{
            try {
                GeneralProductDTO dto =  dao.queryBuilder().where().eq("GPID",general_product_id).and().eq("branch_id",branch_id).and().eq("category_id",category_id).queryForFirst();
                if(dto != null) {
                    GeneralProduct gp = new GeneralProduct(dto);
                    gp.setProducts(loadSpecifics(general_product_id,branch_id));
                    gp.setCatalog_products(loadCatalogs(general_product_id,branch_id));
                    identityMap.put(general_product_id, generalProduct);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return generalProduct;
    }
 */
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
            DeleteBuilder<GeneralProductDTO,Void> deleteBuilder = dao.deleteBuilder();
            cascadeDeleteSpecific(generalProduct);
            cascadeDeleteoOnSale(generalProduct);
            cascadeDeleteCatalogProducts(generalProduct);
            //cascadeDeleteCatalogProductinGenneralProduct(generalProduct);
            deleteBuilder.where().eq("GPID", generalProduct.getGpID()).and().eq("branch_id" , generalProduct.getBranch_id());
            deleteBuilder.delete();
           // System.err.println(String.format("[Writing] %s", generalProductDTO));
            //all specific products and category are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    private void cascadeDeleteCatalogProductinGenneralProduct(GeneralProduct generalProduct) {
        Mapper mapper = Mapper.getInstance();
        DeleteBuilder<catalog_product_in_general_productDTO,Void> deleteBuilder = mapper.catalog_product_dao.catalog_product_in_general_products_dao.deleteBuilder();
        try {
            deleteBuilder.where().eq("GPID",generalProduct.getGpID()).and().eq("branch_id",generalProduct.getBranch_id());
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void cascadeDeleteCatalogProducts(GeneralProduct generalProduct) {
        try {
            List<catalog_product_in_general_productDTO> dtos = catalog_product_in_general_products_dao.queryBuilder().where().eq("GPID",generalProduct.getGpID()).and().eq("branch_id",generalProduct.getBranch_id()).query();
            if(dtos!=null && !dtos.isEmpty()){
                DeleteBuilder<catalog_product_in_general_productDTO,Void> deleteBuilder = catalog_product_in_general_products_dao.deleteBuilder();
                deleteBuilder.where().eq("GPID",generalProduct.getGpID()).and().eq("branch_id",generalProduct.getBranch_id());
                deleteBuilder.delete();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void cascadeDeleteoOnSale(GeneralProduct generalProduct) {
        Mapper mapper = Mapper.getInstance();
        DeleteBuilder<general_product_on_saleDTO,Void> deleteBuilder = mapper.sale_dao.general_product_on_sale_dao.deleteBuilder();
        try {
            deleteBuilder.where().eq("GPID",generalProduct.getGpID()).and().eq("branch_id",generalProduct.getBranch_id());
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void cascadeDeleteSpecific(GeneralProduct generalProduct) {
        Mapper mapper = Mapper.getInstance();
        if(generalProduct.getProducts()!=null){
        for(SpecificProduct specificProduct:generalProduct.getProducts()){
                mapper.delete(specificProduct);
            }
        }
    }

    private LinkedList<SpecificProduct> loadSpecifics(Integer gpid, Integer branch_id){
        LinkedList<SpecificProduct> specificProducts=new LinkedList<>();
        try {
            List<SpecificProductDTO> dtos = Mapper.getInstance().specific_product_dao.dao.queryBuilder().where().eq("GPID",gpid).and().eq("branch_id",branch_id).query();
            if(dtos!=null && !dtos.isEmpty()){
                for (SpecificProductDTO dto:dtos){
                    SpecificProduct specificProduct=Mapper.getInstance().find_SpecificProduct(dto.getId(),branch_id);
                    if (specificProduct!=null){specificProducts.add(specificProduct);}
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return specificProducts;
    }

    private LinkedList<CatalogProduct> loadCatalogs(Integer gpid, Integer branch_id){
        LinkedList<CatalogProduct> catalogProducts=new LinkedList<>();
        try {
            List<catalog_product_in_general_productDTO> dtos = this.catalog_product_in_general_products_dao.queryBuilder().where().eq("GPID",gpid).and().eq("branch_id",branch_id).query();
            if(dtos!=null && !dtos.isEmpty()){
                for(catalog_product_in_general_productDTO dto:dtos){
                    CatalogProduct catalogProduct = Mapper.getInstance().find_CatalogProduct(dto.getCatalogID(),branch_id);
                    if(catalogProduct!=null){ catalogProducts.add(catalogProduct);}
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return catalogProducts;
    }


    public void clearCache() {
        this.identityMap.clear();
    }

    public void deleteByBranch(Integer branch_id) {
        try {
            List<GeneralProductDTO> list = dao.queryBuilder().where().eq("branch_id",branch_id).query();
            if(list != null && !list.isEmpty()) {
                for (GeneralProductDTO dto : list) {
                    GeneralProduct generalProduct= find(dto.getGPID(),branch_id);
                    if (generalProduct!=null) {
                        delete(generalProduct);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
