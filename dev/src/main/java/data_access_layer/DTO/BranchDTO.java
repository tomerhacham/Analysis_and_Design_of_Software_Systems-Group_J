package data_access_layer.DTO;

import bussines_layer.Branch;
import bussines_layer.inventory_module.SpecificProduct;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Branch")
public class BranchDTO {
    //fields:
    @DatabaseField(id=true,columnName = "branch_id")
    Integer branch_id;
    @DatabaseField(columnName = "name")
    String name;
    /*@ForeignCollectionField(eager = false,foreignFieldName = "branch_id")
    ForeignCollection<CategoryDTO> categories;
    @ForeignCollectionField(eager =false, foreignFieldName ="branch_id")
    ForeignCollection<OrderDTO> orders;
    @ForeignCollectionField(eager=false, foreignFieldName = "branch_id")
    ForeignCollection<CatalogProductDTO> catalogProducts;
    @ForeignCollectionField(eager=false, foreignFieldName = "branch_id")
    ForeignCollection<GeneralProductDTO> generalProducts;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<SaleDTO> sales;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<ContractDTO> contracts;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<CostEngineeringDTO> costEngineering;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<SpecificProductDTO> specificProducts;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<catalog_product_in_contractDTO> catalog_product_in_contracts;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<categories_in_contractDTO> categories_in_contract;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<catalog_product_in_general_productDTO> catalog_product_in_general_product;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<catalog_product_in_orderDTO> catalog_product_in_order;
    @ForeignCollectionField(eager=false,foreignFieldName = "branch_id")
    ForeignCollection<general_product_on_saleDTO> general_product_on_sale;*/
    //Constructor
    public BranchDTO(Integer branch_id) {
        this.branch_id = branch_id;
        this.name=null;
    }
    public BranchDTO(Branch branch){this.branch_id=branch.getBranchId();this.name=branch.getName();}
    public BranchDTO() {}

    //region Methods
    public Integer getBranch_id() {
        return branch_id;
    }

    public String getName() {
        return name;
    }
    //endregion
}
