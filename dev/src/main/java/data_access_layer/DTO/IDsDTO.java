package DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "IDs")
public class IDsDTO {
    //fields
    @DatabaseField(columnName = "category_next_id")
    Integer category_next_id;
    @DatabaseField(columnName = "product_next_id")
    Integer product_next_id;
    @DatabaseField(columnName = "sale_next_id")
    Integer sale_next_id;
    @DatabaseField(columnName = "contract_next_id")
    Integer contract_next_id;
    @DatabaseField(columnName = "order_next_id")
    Integer order_next_id;
    @DatabaseField(columnName = "branch_next_id")
    Integer branch_next_id;
    @DatabaseField(columnName = "supplier_next_id")
    Integer supplier_next_id;

    //Constructor
    public IDsDTO(Integer category_next_id, Integer product_next_id, Integer sale_next_id, Integer contract_next_id, Integer order_next_id, Integer branch_next_id, Integer supplier_next_id) {
        this.category_next_id = category_next_id;
        this.product_next_id = product_next_id;
        this.sale_next_id = sale_next_id;
        this.contract_next_id = contract_next_id;
        this.order_next_id = order_next_id;
        this.branch_next_id = branch_next_id;
        this.supplier_next_id = supplier_next_id;
    }
    public IDsDTO() {
    }
    //region Methods

    public Integer getCategory_next_id() {
        return category_next_id;
    }

    public Integer getProduct_next_id() {
        return product_next_id;
    }

    public Integer getSale_next_id() {
        return sale_next_id;
    }

    public Integer getContract_next_id() {
        return contract_next_id;
    }

    public Integer getOrder_next_id() {
        return order_next_id;
    }

    public Integer getBranch_next_id() {
        return branch_next_id;
    }

    public Integer getSupplier_next_id() {
        return supplier_next_id;
    }

    //endregion
}
