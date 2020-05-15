package data_access_layer.DTO;


import bussines_layer.inventory_module.CatalogProduct;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "catalog_products_in_order")
public class catalog_product_in_orderDTO {
    //fields:
    @DatabaseField(foreign =true, columnName = "order_id",foreignColumnName = "order_id",foreignAutoRefresh = true)
    OrderDTO order;
    @DatabaseField(foreign = true, columnName = "catalog_id",foreignColumnName = "catalog_id",foreignAutoRefresh = true)
    CatalogProductDTO catalogProduct;
    @DatabaseField(columnName = "quantity")
    Integer quantity;
    @DatabaseField(columnName = "price")
    Float price;

    //Constructor
    public catalog_product_in_orderDTO(OrderDTO order, CatalogProduct catalogProduct, Integer quantity, Float price) {
        this.order = order;
        this.catalogProduct=new CatalogProductDTO(catalogProduct);
        this.quantity=quantity;
        this.price=price;
    }
    public catalog_product_in_orderDTO() {
    }

    //region Methods

    public OrderDTO getOrder() {
        return order;
    }

    public CatalogProductDTO getCatalogProduct() {
        return catalogProduct;
    }
    //endregion
}
