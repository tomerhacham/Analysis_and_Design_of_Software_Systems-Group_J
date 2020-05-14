package bussines_layer.inventory_module;

import bussines_layer.Result;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SaleController {
    //fields
    private Integer next_id;
    private List<Sale> sales;
    private Integer branchId;

    //Constructor
    public SaleController(Integer branchId) {
        this.next_id=1;
        this.sales =new LinkedList<>();
        this.branchId = branchId;
    }

    /**
     * add sale on all general product on category
     * @param category - any kind of category to apply on
     * @param type - enum discountType [fix,percentage]
     * @param amount - if fix, set the sale_price=amount. percentage set sale_price to *1(1-amount/100)
     * @return
     */
    public Result addSale(Category category, discountType type, Float amount){
        if(category!=null){
        List list = category.getAllGeneralProduct();
        Sale new_sale = new Sale(getNext_id(),list, type , branchId);
        sales.add(new_sale);
        return new_sale.setDiscount(amount);
        }
        else{
            return new Result<Boolean>(false,false,"category could not be found");
        }
    }
    /**
     * add sale on all general product on category
     * @param generalProduct - any kind of general product
     * @param type - enum discountType [fix,percentage]
     * @param amount - if fix, set the sale_price=amount. percentage set sale_price to *1(1-amount/100)
     * @return
     */
    public Result addSale(GeneralProduct generalProduct, discountType type, Float amount){
        if(generalProduct!=null) {
            List<GeneralProduct> dummy_list = new LinkedList<>();
            dummy_list.add(generalProduct);
            Sale new_sale = new Sale(getNext_id(), dummy_list, type , branchId);
            sales.add(new_sale);
            return new_sale.setDiscount(amount);
        }
        else{
            return new Result<Boolean>(false,false,"general product could not be found");
        }
    }
    /**
     * add sale on all general product on category
     * @param category - any kind of category to apply on
     * @param type - enum discountType [fix,percentage]
     * @param amount - if fix, set the sale_price=amount. percentage set sale_price to *1(1-amount/100)
     * @param start - Date type. the starting date of the sale
     * @param end - Date type. the ending date of the sale
     * @return
     */
    public Result addSale(Category category, discountType type, Float amount, Date start, Date end){
        if(category!=null) {
            List list = category.getAllGeneralProduct();
            Sale new_sale = new Sale(getNext_id(), list, type, start, end , branchId);
            sales.add(new_sale);
            return new_sale.setDiscount(amount);
        }
        else{
            return new Result<Boolean>(false,false,"category could not be found");
        }
    }
    /**
     * add sale on all general product on category
     * @param generalProduct - any kind of general product
     * @param type - enum discountType [fix,percentage]
     * @param amount - if fix, set the sale_price=amount. percentage set sale_price to *1(1-amount/100)
     * @param start - Date type. the starting date of the sale
     * @param end - Date type. the ending date of the sale
     * @return
     */
    public Result addSale(GeneralProduct generalProduct, discountType type, Float amount, Date start, Date end){
        if(generalProduct!=null) {
            List<GeneralProduct> dummy_list = new LinkedList<>();
            dummy_list.add(generalProduct);
            Sale new_sale = new Sale(getNext_id(), dummy_list, type, start, end , branchId);
            sales.add(new_sale);
            return new_sale.setDiscount(amount);
        }
        else{
            return new Result<Boolean>(false,false,"general product could not be found");
        }
    }
    /**
     * deactivate the sale and return the retail prices
     * @param sale_id - id of the sale (allocated by the controller)
     * @return
     */
    public Result removeSale(Integer sale_id){
        Sale sale = searchSalebyId(sale_id);
        if (sale!=null){
            return sale.removeSale();
        }
        else{
            return new Result<Integer>(false,sale_id,"Could not find sale ID:"+sale_id);
        }
    }
    /**
     * iterate on each sale and modify the activity flag in condition of the date
     * @return
     */
    public Result CheckSalesStatus(){
        List<Sale> active_sales= new LinkedList<>();
        for(Sale sale:sales){
            if (sale.isActive()){
                active_sales.add(sale);
            }
        }
        return new Result<List<Sale>>(true,active_sales,"Checked sales activation status");
    }
    /**
     * allocate the free next id available
     * @return
     */
    private Integer getNext_id() {
        Integer ret = next_id;
        this.next_id++;
        return ret;
    }
    /**
     * searching a sale by its ID
     * @param sale_id - id of the sale (allocated by the Sale Controller)
     * @return
     */
    private Sale searchSalebyId(Integer sale_id){
        for (Sale sale: sales) {
            if(sale.getSale_id()==sale_id){
                return sale;
            }
        }
        return null;
    }

    public Integer getBranch_id() { return branchId;}



    @Override
    public String toString(){
        String toReturn = "Sales:\n";
        for (Sale sale: sales){
            toReturn=toReturn.concat("\t"+sale.toString()+"\n");
        }
        return toReturn;
    }
}
