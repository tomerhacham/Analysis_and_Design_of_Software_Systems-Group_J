package data_access_layer.DAO;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.Category;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.CategoryDTO;
import data_access_layer.DTO.SpecificProductDTO;

import java.sql.SQLException;
import java.util.HashMap;

public class CategoryDAO {
    //fields:
    HashMap<Integer, Category> identityMap;
    public Dao<CategoryDTO,Void> dao;
    //Constructor

    public CategoryDAO(ConnectionSource conn) {
        try {
            this.identityMap=new HashMap<>();
            this.dao= DaoManager.createDao(conn,CategoryDTO.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public Category find(Integer category_id,Integer branch_id){
        Category category=null;
        if(identityMap.containsKey(category_id)){
            category=identityMap.get(category_id);
        }
        else{
            try {
                category = new Category(dao.queryBuilder().where().eq("category_id",category_id).and().eq("branch_id",branch_id).queryForFirst());
                identityMap.put(category_id,category);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return category;
    }
    /**
     * writing the provided category to the DB
     * @param category
     */
    public void create(Category category){
        if(!identityMap.containsKey(category.getId())){identityMap.put(category.getId(),category);}
        CategoryDTO categoryDTO = new CategoryDTO(category);
        try {
            dao.create(categoryDTO);
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
        if(identityMap.containsKey(category.getId())){identityMap.replace(category.getId(),category);}
        try{
            CategoryDTO categoryDTO=new CategoryDTO(category);
            UpdateBuilder<CategoryDTO, Void> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("category_id", category.getId()).and().eq("branch_id" , category.getBranch_id());
            updateBuilder.updateColumnValue("name" ,category.getName());
            updateBuilder.updateColumnValue("level" ,category.getLevel());
            updateBuilder.updateColumnValue("super_category_id" ,category.getSuper_category_id());
            updateBuilder.update();
            System.err.println(String.format("[Update] %s", categoryDTO));
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete the provided category from the DB
     * @param category
     */
    public void delete(Category category){
        try{
            CategoryDTO categoryDTO=new CategoryDTO(category);
            DeleteBuilder<CategoryDTO,Void> deleteBuilder = dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("category_id", category.getId()).and().eq("branch_id" , category.getBranch_id());
            deleteBuilder.delete();
            System.err.println(String.format("[Delete] %s", categoryDTO));
        }catch (Exception e){e.printStackTrace();}
    }

}
