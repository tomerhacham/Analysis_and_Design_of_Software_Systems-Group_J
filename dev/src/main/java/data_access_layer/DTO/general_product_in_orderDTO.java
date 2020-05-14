package DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "general_products_in_order")
public class general_product_in_orderDTO {
    //fields:
    @DatabaseField(foreign =true, columnName = "order_id",foreignColumnName = "order_id",foreignAutoRefresh = true)
    OrderDTO order;
    @DatabaseField(foreign = true, columnName = "GPID",foreignColumnName = "GPID",foreignAutoRefresh = true)
    GeneralProductDTO generalProduct;
    @DatabaseField(columnName = "branch_id")
    Integer branch_id;

    //Constructor
    public general_product_in_orderDTO( OrderDTO order,GeneralProductDTO generalProduct) {
        this.order = order;
        this.generalProduct = generalProduct;
        this.branch_id = generalProduct.getBranch_id().branch_id;
    }
    public general_product_in_orderDTO() {
    }

    //region Methods

    public OrderDTO getOrder() {
        return order;
    }

    public GeneralProductDTO getGeneralProduct() {
        return generalProduct;
    }

    public Integer getBranch_id() {
        return branch_id;
    }

    //endregion
}
