package bussines_layer;

import java.util.LinkedList;
import java.util.List;

public class CategoryController {
    //fields
    private Integer next_id;
    private List<Category> main_categories;

    //Constructor
    public CategoryController() {
        this.next_id=1;
        this.main_categories=new LinkedList<>();
    }


    /**
     * adding new category to the MAIN category only
     * @param name of the new category
     * @return
     */
    public Result addNewCategory(String name){
        Category new_category = new Category(name,getNext_id(),1);
        boolean res=main_categories.add(new_category);
        Result<Category> result;
        if (res){
            result=new Result(res,new_category,"New category "+name+" has been added");
        }
        else{
            result=new Result(res,new_category,"There was a problem with adding new category: "+name);
        }
        return result;
    }

    /**
     * add new sub-category
     * @param predecessor_cat_id - id of the predecessor category
     * @param name - name for the new category
     * @return
     */
    public Result addNewCategory(Integer predecessor_cat_id, String name){
        Category pre_cat = searchCategorybyId(predecessor_cat_id);
        Result<Category> result;
        if(pre_cat!=null){
            result = pre_cat.addSubCategory(name,getNext_id());
        }
        else{
            result = new Result<>(false,pre_cat,"Could not add sub-category: "+name);
        }
        return  result;
    }

    /**
     * removing categories under the constrain that the number of
     * products needs to be 0. canot delete predecessor categories if one of the successor has products in it
     * @param category_id - the ID of the category to remove.
     * @return
     */
    public Result removeCategory(Integer category_id) {
        Category toRemove = searchCategorybyId(category_id);
        boolean res = false;
        Result<Category> result=null;
        if (toRemove.getNumberOfProducts() == 0) {
            if (toRemove.getLevel()==1){
                res = main_categories.remove(toRemove);
                if (res) {
                    result = new Result<>(res, toRemove, "Category "+toRemove.getName()+" has been deleted");
                } else {
                    result = new Result<>(res, toRemove, "There was a problem deleting the category: "+toRemove.getName());
                }
            }
            else{
                for (Category category:main_categories) {
                    result=category.deleteCategoryRecursive(toRemove);
                }
            }
        }
        else {
            result = new Result<>(res, toRemove, "Could not delete category "+ toRemove.getName()+" with product still available in it");
        }
        return result;
    }

    /**
     * NOT RECURSIVE
     * get the category by name
     * @param name
     * @return
     */
    public Category getCategorybyName(String name){
        Category toReturn=null;
        for (Category category:main_categories) {
            if (category.getName().equals(name)){
                toReturn=category;
            }
        }
        return toReturn;
    }

    /**
     * search recursively after the category using the id.
     * @param id - ID of the desired category
     * @return
     */
    public Category searchCategorybyId(Integer id){
        Category requestedCategory;
        for (Category category:main_categories){
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

    /**
     * edit the category name
     * @param category_id - id of the desire category
     * @param name - the new name
     * @return
     */
    public Result editCategoryname(Integer category_id,String name){
        Category category = searchCategorybyId(category_id);
        Result<Category> result;
        if(category!=null){
            category.setName(name);
            result = new Result<>(true,category,"Successful edit category: "+category.getName());
        }
        else{
            result = new Result<>(false,category,"THere was a problem editing the category: "+category.getName());
        }
        return result;
    }

    public Category superCategory(){
        return new Category("super",this.main_categories);
    }

    /**
     * allocate the next free ID
     * @return
     */
    private Integer getNext_id() {
        Integer ret= next_id;
        this.next_id++;
        return ret;
    }
    @Override
    public String toString() {
        String toReturn="Main Categories:\n";
        for (Category category:main_categories) {
            toReturn=toReturn.concat("\t-").concat(category.getName()).concat("\n");
        }
        return toReturn;
    }
}
