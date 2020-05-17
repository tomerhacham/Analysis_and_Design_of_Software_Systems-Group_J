package data_access_layer.DTO;


import bussines_layer.enums.discountType;
import bussines_layer.inventory_module.Sale;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Sale")
public class SaleDTO {
    //fields:
    @DatabaseField(columnName = "sale_id")
    Integer sale_id;
    @DatabaseField(columnName = "type",dataType = DataType.ENUM_TO_STRING)
    discountType type;
    @DatabaseField(columnName = "start", dataType=DataType.DATE_STRING)
    Date start;
    @DatabaseField(columnName = "end", dataType=DataType.DATE_STRING)
    Date end;
    @DatabaseField(columnName = "active")
    Boolean active;
    @DatabaseField(columnName = "branch_id",foreign =true, foreignColumnName = "branch_id",foreignAutoRefresh = true)
    BranchDTO branch;


    public SaleDTO(Sale sale){
        this.sale_id=sale.getSale_id();
        this.type=convertStringTOEnum(sale.getType().name());
        this.start=sale.getStart();
        this.end=sale.getEnd();
        this.active=sale.getActive();
        this.branch=new BranchDTO(sale.getBranch_id());
    }

    public SaleDTO() {
    }

    //region Methods

    public Integer getSale_id() {
        return sale_id;
    }

    public discountType getType() {
        return type;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Boolean getActive() {
        return active;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    /*
    public ForeignCollection<general_product_on_saleDTO> getGeneral_product_on_sale() {
        return general_product_on_sale;
    }*/ //TODO update Tomer

    public discountType convertStringTOEnum(String location){
        if (location.equals("fix")){
            return discountType.fix;
        }
        else{
            return discountType.precentage;
        }
    }

    @Override
    public String toString() {
        return "SaleDTO{" +
                "sale_id=" + sale_id +
                ", type=" + type +
                ", start=" + start +
                ", end=" + end +
                ", active=" + active +
                ", branch=" + branch.getBranch_id() +
                '}';
    }
    //endregion
}
