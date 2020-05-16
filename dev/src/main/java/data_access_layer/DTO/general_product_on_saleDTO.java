package data_access_layer.DTO;


import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.Sale;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "general_products_on_sale")
public class general_product_on_saleDTO {
    //fields:
    @DatabaseField(columnName = "GPID")
    Integer GPID;
    @DatabaseField(columnName = "sale_id")
    Integer sale_id;
    @DatabaseField(foreign = true, foreignColumnName = "branch_id",columnName = "branch_id")
    BranchDTO branch_id;

    //Constructor
    public general_product_on_saleDTO(Sale sale, GeneralProduct generalProduct) {
        this.sale_id = sale.getSale_id();
        this.branch_id=new BranchDTO(sale.getBranch_id());
        this.GPID=generalProduct.getGpID();
    }
    public general_product_on_saleDTO() {}

    //region Methods


    public Integer getGPID() {
        return GPID;
    }

    public Integer getSale_id() {
        return sale_id;
    }

    public BranchDTO getBranch_id() {
        return branch_id;
    }

    @Override
    public String toString() {
        return "general_product_on_saleDTO{" +
                "GPID=" + GPID +
                ", sale_id=" + sale_id +
                ", branch_id=" + branch_id.branch_id +
                '}';
    }
    //endregion
}
