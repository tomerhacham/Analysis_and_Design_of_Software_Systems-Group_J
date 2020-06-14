package data_access_layer.DTO;


import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "catalog_products_in_order")
public class catalog_product_in_orderDTO {
    //fields:
    @DatabaseField(columnName = "order_id")
    Integer order_id;
    @DatabaseField(columnName = "catalog_id")
    Integer catalog_id;
    @DatabaseField(columnName = "GPID")
    Integer GPID;
    @DatabaseField(columnName = "branch_id")
    Integer branch_id;
    @DatabaseField(columnName = "quantity")
    Integer quantity;
    @DatabaseField(columnName = "price")
    Float price;

    //Constructor
    public catalog_product_in_orderDTO(Order order, CatalogProduct catalogProduct,Integer quantity, Float price) {
    this.order_id=order.getOrderID();
    this.catalog_id=catalogProduct.getCatalogID();
    this.GPID=catalogProduct.getGpID();
    this.branch_id=catalogProduct.getBranch_id();
    this.quantity=quantity;
    this.price=price;
    }
    public catalog_product_in_orderDTO() {}

    //region Methods

    public Integer getOrder_id() {
        return order_id;
    }

    public Integer getCatalog_id() {
        return catalog_id;
    }

    public Integer getGPID() {
        return GPID;
    }

    public Integer getBranch_id() {
        return branch_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Float getPrice() {
        return price;
    }

    //endregion
}
