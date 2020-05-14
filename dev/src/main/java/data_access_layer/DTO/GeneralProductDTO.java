package data_access_layer.DTO;

import bussines_layer.inventory_module.GeneralProduct;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "GeneralProduct")
public class GeneralProductDTO {
    //fields:
    @DatabaseField(id=true, columnName = "GPID")
    Integer GPID;
    @DatabaseField(columnName = "branch_id",foreign =true, foreignColumnName = "branch_id",foreignAutoRefresh = true)
    BranchDTO branch_id;
    @DatabaseField(columnName = "manufacture")
    String manufacture;
    @DatabaseField(columnName = "name")
    String name;
    @DatabaseField(columnName = "quantity")
    Integer quantity;
    @DatabaseField(columnName = "min_quantity")
    Integer min_quantity;
    @DatabaseField(columnName = "sale_price")
    Float sale_price;
    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignColumnName = "category_id",columnName = "category_id")
    CategoryDTO category_id;
    @DatabaseField(columnName = "retail_price")
    Float retail_price;
    @ForeignCollectionField(eager = false)
    ForeignCollection<SpecificProductDTO> specific_products;

    //Constructor
    public GeneralProductDTO(Integer GPID, BranchDTO branch_id, CategoryDTO category,String manufacture,
                             String name, Float retail_price, Integer quantity, Integer min_quantity,
                             Float sale_price) {
        this.GPID = GPID;
        this.branch_id = branch_id;
        this.manufacture = manufacture;
        this.name = name;
        this.quantity = quantity;
        this.min_quantity = min_quantity;
        this.sale_price = sale_price;
        this.retail_price=retail_price;
        this.category_id = category;
    }
    public GeneralProductDTO(GeneralProduct generalProduct){
        this.GPID = generalProduct.getGpID();
        this.branch_id = new BranchDTO(generalProduct.getBranch_id());
        this.manufacture = generalProduct.getManufacture();
        this.name = generalProduct.getName();
        this.quantity = generalProduct.getQuantity();
        this.min_quantity = generalProduct.getMinQuantity();
        this.sale_price = generalProduct.getSale_price();
        this.retail_price=generalProduct.getRetailPrice();
        this.category_id = getCategory_id();
    }
    public GeneralProductDTO() {}

    //region Methods


    public Integer getGPID() {
        return GPID;
    }

    public BranchDTO getBranch_id() {
        return branch_id;
    }

    public String getManufacture() {
        return manufacture;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getMin_quantity() {
        return min_quantity;
    }

    public Float getSale_price() {
        return sale_price;
    }

    public CategoryDTO getCategory_id() {
        return category_id;
    }

    public Float getRetail_price() {
        return retail_price;
    }

    public ForeignCollection<SpecificProductDTO> getSpecific_products() {
        return specific_products;
    }

    @Override
    public String toString() {
        return "GeneralProductDAO{" +
                "GPID=" + GPID +
                ", branch_id=" + branch_id +
                ", manufacture='" + manufacture + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", min_quantity=" + min_quantity +
                ", sale_price=" + sale_price +
                ", category_id=" + category_id +
                '}';
    }
    //endregion
}
