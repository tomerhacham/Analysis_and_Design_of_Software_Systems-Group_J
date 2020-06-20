package data_access_layer.DTO;

import bussines_layer.inventory_module.GeneralProduct;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "GeneralProduct")
public class GeneralProductDTO {
    //fields:
    @DatabaseField(columnName = "GPID")
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
    @DatabaseField(columnName = "category_id")
    Integer category_id;
    @DatabaseField(columnName = "retail_price")
    Float retail_price;
    @DatabaseField(columnName = "weight")
    Float weight;
    /*
    @ForeignCollectionField(eager = false)
    ForeignCollection<SpecificProductDTO> specific_products;
    @ForeignCollectionField(eager=false)
    ForeignCollection<catalog_product_in_general_productDTO> catalog_products;*/

    //Constructor
    public GeneralProductDTO(GeneralProduct generalProduct){
        this.GPID = generalProduct.getGpID();
        this.branch_id = new BranchDTO(generalProduct.getBranch_id());
        this.manufacture = generalProduct.getManufacture();
        this.name = generalProduct.getName();
        this.quantity = generalProduct.getQuantity();
        this.min_quantity = generalProduct.getMinQuantity();
        this.sale_price = generalProduct.getSale_price();
        this.retail_price=generalProduct.getRetailPrice();
        this.category_id = generalProduct.getCategory_id();
        this.weight=generalProduct.getWeight();
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

    public Integer getCategory_id() {
        return category_id;
    }

    public Float getRetail_price() {
        return retail_price;
    }

    public Float getWeight() {
        return weight;
    }
    /* public ForeignCollection<SpecificProductDTO> getSpecific_products() {
        return specific_products;
    }

    public ForeignCollection<catalog_product_in_general_productDTO> getCatalog_products() {
        return catalog_products;
    }*/

   /* public void setSpecific_products(ForeignCollection<SpecificProductDTO> specific_products) {
        this.specific_products = specific_products;
    }

    public void setCatalog_products(ForeignCollection<catalog_product_in_general_productDTO> catalog_products) {
        this.catalog_products = catalog_products;
    }
*/
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
