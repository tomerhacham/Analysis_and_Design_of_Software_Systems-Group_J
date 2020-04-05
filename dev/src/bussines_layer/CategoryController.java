package bussines_layer;

import java.util.LinkedList;
import java.util.List;

public class CategoryController {
    //fields
    private final GIDAllocator GIDAllocator;
    private List<Category> main_categories;

    public CategoryController(GIDAllocator GIDAllocator) {
        this.GIDAllocator = GIDAllocator;
        this.main_categories=new LinkedList<>();
    }
    //region Methods
    public Result addNewCategory(String name){
        Category new_category = new Category(name,GIDAllocator.getNext_id(),1);
        boolean res=main_categories.add(new_category);
        Result<Category> result;
        if (res){
            result=new Result(res,new_category,"New category has been added");
        }
        else{
            result=new Result(res,new_category,"There was a problem with adding new category");
        }
        return result;
    }
    public Category getCategorybyName(String name){
        Category toReturn=null;
        for (Category category:main_categories) {
            if (category.getName().equals(name)){
                toReturn=category;
            }
        }
        return toReturn;
    }
    @Override
    public String toString() {
        String toReturn="Main Categories:\n";
        for (Category category:main_categories) {
            toReturn=toReturn.concat("\t-").concat(category.getName()).concat("\n");
        }
        return toReturn;
    }
    //endregion
}
