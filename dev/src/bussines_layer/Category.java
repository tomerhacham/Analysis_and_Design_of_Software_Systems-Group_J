package bussines_layer;

import java.util.LinkedList;
import java.util.List;

public class Category {
    //fields:
    private String name;
    private Integer id;
    private List<Category> sub_categories;
    private final Integer level;
    private List<GeneralProduct> generalProducts;

    public Category(String name, Integer id,Integer level) {
        this.name = name;
        this.id = id;
        this.level=level;
        this.sub_categories = new LinkedList<>();
        if(level==3){this.generalProducts =new LinkedList<>();}
    }
    //region Setters - Getters
    public String getName() {
        return name;
    }

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

    public void setSub_categories(List<Category> sub_categories) {
        this.sub_categories = sub_categories;
    }
    //endregion

    //region Methods
    //TODO: add remove function and not allow deletion if there is any SpecificProduct associate with this category
    public Result addSubCategory(String name,Integer  id){
        Category  new_category = new Category(name,id,level+1);
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
            result=new Result(res,new_category,"New category has been added");
        }
        else{
            result=new Result(res,new_category,msg);
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
    public Result addGeneralProduct(GeneralProduct generalProduct){
        boolean res=false;
        Result<GeneralProduct> result;
        if(level==3){
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
    @Override
    public String toString() {
        String toReturn="-".concat(name).concat("\n");
        for (Category category:sub_categories) {
            toReturn=toReturn.concat("\t-").concat(category.name).concat("\n");
        }
        return toReturn;
    }
    //endregion
}
