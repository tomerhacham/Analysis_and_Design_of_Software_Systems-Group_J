package bussines_layer.inventory_module;

import bussines_layer.Result;
import data_access_layer.DTO.CategoryDTO;

import java.util.LinkedList;
import java.util.List;

public class Category {
    //fields:
    private Integer branch_id;
    private Integer super_category_id;
    private String name;
    private Integer id;
    private final Integer level;
    private List<Category> sub_categories;
    private List<GeneralProduct> generalProducts;

    //Constructor
    public Category(Integer branch_id,Integer super_category_id,String name, Integer id,Integer level) {
        this.branch_id=branch_id;
        this.super_category_id=super_category_id;
        this.name = name;
        this.id = id;
        this.level=level;
        this.sub_categories = new LinkedList<>();
        this.generalProducts =new LinkedList<>();
    }
    public Category(String name,List<Category> sub_categories){
        this.name=name;
        this.sub_categories=sub_categories;
        this.id=0;
        this.level=0;
    }
    public Category(CategoryDTO categoryDTO){
        this.branch_id=categoryDTO.getBranch_id().getBranch_id();
        this.super_category_id=categoryDTO.getSuper_category();
        this.name=categoryDTO.getName();
        this.level=categoryDTO.getLevel();
        this.id=categoryDTO.getId();
        this.sub_categories = new LinkedList<>();
        this.generalProducts =new LinkedList<>();
    }

    //region Setters - Getters
    public String getName() {
        return name;
    }

    public Integer getLevel() {return level;}
    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Category> getSub_categories() {
        return sub_categories;
    }

    public void setGeneralProducts(List<GeneralProduct> generalProducts) {
        this.generalProducts = generalProducts;
    }

    public void setSub_categories(List<Category> sub_categories) {
        this.sub_categories = sub_categories;
    }
    //endregion

    //region Methods
    public Result addSubCategory(String name, Integer  id){
        Category  new_category = new Category(this.branch_id,this.id,name,id,level+1);
        boolean res=false;
        String msg="";
        Result<Category> result;

        if (this.level+1<=3) {
            res = sub_categories.add(new_category);
        }
        else{
            msg = "You have reached hierarchy level";
        }
        if(res){
            result=new Result<>(res,new_category,"New category "+name+"("+id+")"+" has been added");
        }
        else{
            result=new Result<>(res,new_category,msg);
        }
        return result;
    }
    public Category getCategorybyName(String name){
        Category toReturn=null;
        for (Category category:sub_categories) {
            if (category.getName().equals(name)){
                toReturn=category;
            }
        }
        return toReturn;
    }
    public Category searchCategorybyId(Integer id) {
        Category requestedCategory;
        for (Category category:sub_categories){
            if(category.getId()==id){return category;}
            else{
                requestedCategory=category.searchCategorybyId(id);
                if(requestedCategory!=null){
                    return requestedCategory;
                }
            }
        }
        return null;
    }
    public Result addGeneralProduct(GeneralProduct generalProduct){
        boolean res=false;
        Result<GeneralProduct> result;
        if(level==3){
            generalProduct.setCategory_id(this.id);
            res=generalProducts.add(generalProduct);
            if(res){result=new Result<>(res,generalProduct,"general product has been added to the category");}
            else{result=new Result<>(res,generalProduct,"There was a problem with adding the product the the category");}
        }
        else{result = new Result<>(res,generalProduct,"Cannot add product the low-level category. adding a product have to be done in the 'Size' sub-category");}
        return result;
    }
    public List<GeneralProduct> getAllGeneralProduct(){
        if(level==3){
            return this.generalProducts;
        }
        else{
            List<GeneralProduct> allProducts = new LinkedList<>();
            for (Category category:sub_categories) {
                allProducts.addAll(category.getAllGeneralProduct());
            }
            return allProducts;
        }
    }
    public Integer getNumberOfProducts(){
        Integer total_quantity=0;
        if(level==3){
            total_quantity= generalProducts.size();
        }
        else{
            for(Category category:sub_categories){
                total_quantity=total_quantity+category.getNumberOfProducts();
            }
        }
        return total_quantity;
    }
    public Result<Category> deleteCategoryRecursive(Category toRemove){
        Result<Category> result=null;
        boolean res=false;
        if (sub_categories.contains(toRemove)){
            res=sub_categories.remove(toRemove);
            if(res){result=new Result<>(res,toRemove,"Category "+this.name+"("+id+")"+" has been deleted");}
            else{result=new Result<>(res,toRemove,"There was a problem deleting the category"+this.name+"("+id+")");}
        }
        else{
            for (Category category : sub_categories) {
                result=category.deleteCategoryRecursive(toRemove);
            }
        }
        return result;
    }
    public Result removeGeneralProduct(GeneralProduct toRemove){
        Result<GeneralProduct> result;
        boolean res=false;
        if(generalProducts.contains(toRemove)){
            res=generalProducts.remove(toRemove);
            if(res){
                result = new Result<>(res,toRemove,"General product "+toRemove.getName()+"("+toRemove.getGpID()+")"+ "has been removed from the category");
            }
            else{
                result = new Result<>(res,toRemove,"There was a problem removing the product "+toRemove.getName()+"("+toRemove.getGpID()+")"+"from the category");
            }
        }
        else{
            result=new Result<>(res,toRemove,"Could not find the general product in the category");
        }
        return result;
    }

    public Integer getBranch_id() {
        return branch_id;
    }

    public Integer getSuper_category_id() {
        return super_category_id;
    }

    public List<GeneralProduct> getGeneralProducts() {
        return generalProducts;
    }

    @Override
    public String toString() {
        String toReturn=tabs()+"-".concat(name).concat("("+id+")").concat("\n");
        if (sub_categories != null){
            for (Category category:sub_categories) {
                toReturn=toReturn.concat(category.toString());
            }
        }
        return toReturn;
    }
    private String tabs(){
        String tabs="";
        for(int i=1;i<level;i++){
            tabs=tabs.concat("\t");
        }
        return tabs;
    }

    //endregion
}
