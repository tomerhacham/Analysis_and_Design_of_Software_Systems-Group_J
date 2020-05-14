package data_access_layer.DTO;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "general_products_on_sale")
public class general_product_on_saleDTO {
    //fields:
    @DatabaseField(foreign = true, columnName = "GPID",foreignColumnName = "GPID",foreignAutoRefresh = true)
    GeneralProductDTO generalProduct;
    @DatabaseField(foreign =true, columnName = "sale_id",foreignColumnName = "sale_id",foreignAutoRefresh = true)
    SaleDTO sale;
    @DatabaseField(columnName = "branch_id")
    Integer branch_id;

    //Constructor
    public general_product_on_saleDTO(GeneralProductDTO generalProduct, SaleDTO sale) {
        this.generalProduct = generalProduct;
        this.sale = sale;
        this.branch_id=generalProduct.getBranch_id().branch_id;
    }
    public general_product_on_saleDTO() {}

    //region Methods


    public GeneralProductDTO getGeneralProduct() {
        return generalProduct;
    }

    public SaleDTO getSale() {
        return sale;
    }

    public Integer getBranch_id() {
        return branch_id;
    }

    @Override
    public String toString() {
        return "general_product_on_saleDTO{" +
                "generalProduct=" + generalProduct +
                ", sale=" + sale +
                ", branch_id=" + branch_id +
                '}';
    }
    //endregion
}
