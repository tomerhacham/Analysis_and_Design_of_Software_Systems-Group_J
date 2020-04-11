package bussines_layer;

import java.util.Date;
import java.util.List;

enum discountType
{fix,precentage;}

public class Sale {
    //fields
    private Integer sale_id;
    private List<GeneralProduct> products_on_sale;
    private Date start;
    private Date end;
    private discountType type;
    private Boolean active;

    //Constructor
    public Sale(Integer sale_id, List<GeneralProduct> products_on_sale,discountType type) {
        this.sale_id=sale_id;
        this.products_on_sale = products_on_sale;
        this.type=type;
        this.start=null;
        this.end=null;
        active = true;
    }
    public Sale(Integer sale_id, List<GeneralProduct> products_on_sale, discountType type,Date start, Date end) {
        if(start.before(end)) {
            this.sale_id=sale_id;
            this.type=type;
            this.products_on_sale = products_on_sale;
            this.start = start;
            this.end = end;
            active = true;
        }
    }

    public Result setDiscount(Float number){
        String msg="";
        Result result=null;
        for (GeneralProduct product:products_on_sale) {
            Float sale_price=number;
            if (type.equals(discountType.precentage)){
                sale_price=product.getRetail_price()*(1-(number/100));
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
            Date current = new Date();
            active = current.before(end);
        }
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
