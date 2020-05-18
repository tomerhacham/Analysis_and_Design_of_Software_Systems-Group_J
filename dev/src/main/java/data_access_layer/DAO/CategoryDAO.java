package data_access_layer.DAO;

import bussines_layer.Branch;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.Category;
import bussines_layer.inventory_module.CategoryController;
import bussines_layer.inventory_module.GeneralProduct;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.CategoryDTO;
import data_access_layer.DTO.GeneralProductDTO;
import data_access_layer.DTO.SaleDTO;
import data_access_layer.DTO.SpecificProductDTO;
import data_access_layer.Mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
                CategoryDTO categoryDTO = dao.queryBuilder().where().eq("category_id",category_id).and().eq("branch_id",branch_id).queryForFirst();
                if(categoryDTO !=null) {
                    category = new Category(categoryDTO);
                    if (category.getLevel() == 1) {
                        category.setSub_categories(LoadSubCategories(branch_id, category_id));
                    }
                    else if (category.getLevel() == 2) {
                        category.setSub_categories(LoadSubSubCategories(branch_id, category_id));
                    }
                    else if (category.getLevel() == 3) {
                        category.setGeneralProducts(loadGeneralProduct(branch_id, category_id));
                    }
                    identityMap.put(category_id, category);
                }
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
            List<Category> sub_cat = category.getSub_categories();
            if(sub_cat!=null && !sub_cat.isEmpty()){
            for(Category sub:category.getSub_categories()){
                delete(sub);
                }
            }
            deleteBuilder.where().eq("category_id", category.getId()).and().eq("branch_id" , category.getBranch_id());
            deleteBuilder.delete();
            //System.err.println(String.format("[Delete] %s", categoryDTO));
        }catch (Exception e){e.printStackTrace();}
    }

    public void clearCache() {
        this.identityMap.clear();
    }

    public void deleteByBranch(Integer branch_id) {
        try {
            List<CategoryDTO> list = dao.queryBuilder().where().eq("branch_id",branch_id).query();
            if(list != null && !list.isEmpty()) {
                for (CategoryDTO dto : list) {
                    Category category=find(dto.getId(),branch_id);
                    if (category!=null){
                        delete(category);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public LinkedList<Category> LoadSubCategories(Integer branch_id, Integer id) {
        LinkedList<Category> sub_categories = new LinkedList<>();
        try {
            List<CategoryDTO> dto_sub_categories = dao.queryBuilder().where().eq("branch_id", branch_id).and().eq("super_category_id", id).query();
            if(dto_sub_categories!=null && !dto_sub_categories.isEmpty()) {
                for (CategoryDTO sub_cat_dto : dto_sub_categories) {
                    Category category = find(sub_cat_dto.getId(), branch_id);
                    if(category!=null) {
                        category.setSub_categories(LoadSubSubCategories(branch_id, category.getId()));
                        sub_categories.add(category);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sub_categories;
    }

    public LinkedList<Category> LoadSubSubCategories(Integer branch_id, Integer category_id) {
        LinkedList<Category> sub_sub_categories = new LinkedList<>();
        try {
            List<CategoryDTO> dto_sub_sub_categories = dao.queryBuilder().where().eq("branch_id", branch_id).and().eq("super_category_id", category_id).query();
            if(dto_sub_sub_categories!=null && !dto_sub_sub_categories.isEmpty()) {
                for (CategoryDTO sub_sub_cat_dto : dto_sub_sub_categories) {
                    Category category = find(sub_sub_cat_dto.getId(), branch_id);
                    if (category!=null) {
                        category.setGeneralProducts(loadGeneralProduct(branch_id, category_id));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sub_sub_categories;
    }

    public LinkedList<GeneralProduct> loadGeneralProduct(Integer branch_id, Integer category_id){
        LinkedList<GeneralProduct> generalProducts = new LinkedList<>();
        try {
            List<GeneralProductDTO> generalProductDTOS = Mapper.getInstance().general_product_dao.dao.queryBuilder().where().eq("branch_id",branch_id).and().eq("category_id",category_id).query();
            if(generalProductDTOS!=null && !generalProductDTOS.isEmpty()) {
                for (GeneralProductDTO dto : generalProductDTOS) {
                        GeneralProduct generalProduct = Mapper.getInstance().find_GeneralProduct(dto.getGPID(),branch_id);
                        if (generalProduct!=null){
                            generalProducts.add(generalProduct);
                        }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return generalProducts;
    }
}
