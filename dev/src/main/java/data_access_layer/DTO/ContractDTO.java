package data_access_layer.DTO;


import bussines_layer.supplier_module.Contract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Contract")
public class ContractDTO {
    //fields:
    @DatabaseField(columnName = "contract_id")
    Integer contract_id;
    @DatabaseField(foreign = true, columnName = "branch_id", foreignColumnName = "branch_id", foreignAutoRefresh = true)
    BranchDTO branch;
    @DatabaseField(columnName = "supplier_id")
    Integer supplier_id;
    /*@ForeignCollectionField(eager=false)
    ForeignCollection<catalog_product_in_contractDTO> catalog_product;
    @ForeignCollectionField(eager=false)
    ForeignCollection<CostEngineeringDTO> product_in_cost_engineering;
    @ForeignCollectionField(eager=false)
    ForeignCollection<categories_in_contractDTO> categories_in_contract;*/

    //Constructor
    public ContractDTO(Contract contract){
        this.contract_id=contract.getContractID();
        this.branch = new BranchDTO(contract.getBranchID());
        this.supplier_id = contract.getSupplierID();

    }
    public ContractDTO() {}

    //region Methods

    public Integer getContract_id() {
        return contract_id;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public Integer getSupplier_id() {
        return supplier_id;
    }

    /*public ForeignCollection<catalog_product_in_contractDTO> getCatalog_product() {
        return catalog_product;
    }

    public ForeignCollection<CostEngineeringDTO> getProduct_in_cost_engineering() {
        return product_in_cost_engineering;
    }
    public ForeignCollection<categories_in_contractDTO> getCategories_in_contract() {
        return categories_in_contract;
    }*/

    @Override
    public String toString() {
        return "ContractDTO{" +
                "contract_id=" + contract_id +
                ", branch_id=" + branch.branch_id +
                ", supplier_id=" + supplier_id +
                '}';
    }
    //endregion
}
