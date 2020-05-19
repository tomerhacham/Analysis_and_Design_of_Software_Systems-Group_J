package bussines_layer.inventory_module;

import bussines_layer.BranchController;
import bussines_layer.Result;
import bussines_layer.enums.discountType;
import data_access_layer.DTO.SaleDTO;

import java.util.Date;
import java.util.List;

public class Sale {
    //fields
    private Integer sale_id;
    private discountType type;
    private Date start;
    private Date end;
    private Boolean active;
    private Integer branchId;
    private List<GeneralProduct> products_on_sale;


    //Constructor
    public Sale(Integer sale_id, List<GeneralProduct> products_on_sale,discountType type , Integer branchId) {
        this.sale_id=sale_id;
        this.products_on_sale = products_on_sale;
        this.type=type;
        this.start=null;
        this.end=null;
        active = true;
        this.branchId = branchId;
    }
    public Sale(Integer sale_id, List<GeneralProduct> products_on_sale, discountType type,Date start, Date end  , Integer branchId) {
        if(start.before(end)) {
            this.sale_id=sale_id;
            this.type=type;
            this.products_on_sale = products_on_sale;
            this.start = start;
            this.end = end;
            active = true;
            this.branchId = branchId;
        }
    }

    public Sale(SaleDTO saleDTO) {
        this.sale_id=saleDTO.getSale_id();
        this.type=saleDTO.getType();
        this.start=saleDTO.getStart();
        this.end=saleDTO.getEnd();
        active = saleDTO.getActive();
        this.branchId =saleDTO.getBranch().getBranch_id();
    }

    public void setProducts_on_sale(List<GeneralProduct> products_on_sale) {
        this.products_on_sale = products_on_sale;
    }

    public Result setDiscount(Float number){
        String msg="";
        Result result=null;
        for (GeneralProduct product:products_on_sale) {
            Float sale_price=number;
            if (type.equals(discountType.precentage)){
                sale_price=product.getRetailPrice()*(1-(number/100));
            }
            result = product.setSale(sale_price);
            msg=msg.concat(result.getMessage().concat("\n"));
        }
        result.setMessage(msg);
        return result;
    }

    public Integer getSale_id() {
        return sale_id;
    }

    public Result removeSale(){
        String msg="";
        Result result=null;
        for (GeneralProduct product:products_on_sale) {
            result = product.cancelSale();
            msg=msg.concat(result.getMessage().concat("\n"));
        }
        active = false;
        return result;
    }

    public boolean isActive() {
        if (end != null) {
            Date current = BranchController.system_curr_date;
            active = current.before(end);
        }
        return active;
    }

    public Integer getBranch_id(){
        return branchId;
    }

    public List<GeneralProduct> getProducts_on_sale() {
        return products_on_sale;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Enum<discountType> getType() {
        return type;
    }

    public Boolean getActive() {
        return active;
    }

    @Override
    public String toString() {
        String toReturn= ""+
                "sale id:" + sale_id +
                ", active:" + active +
                ", start:" + start +
                ", end:" + end +
                ", type:" + type.name() +
                "products:{";
        for(GeneralProduct product:products_on_sale){
            toReturn=toReturn.concat(product.getName()+",");
        }
        toReturn=toReturn.concat(")");
        return toReturn;
    }
}
