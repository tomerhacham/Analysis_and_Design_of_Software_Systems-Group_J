package data_access_layer.DTO;

import bussines_layer.SupplierCard;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "contact_of_supplier")
public class contact_of_supplierDTO {
    //fields:
    @DatabaseField(foreign = true, foreignColumnName = "supplier_id", foreignAutoRefresh = true, columnName = "supplier_id" )
    SupplierDTO supplier;
    @DatabaseField(columnName = "name")
    String name;

    //Constructor
    public contact_of_supplierDTO(SupplierDTO supplier, String name) {
        this.supplier = supplier;
        this.name = name;
    }
    public contact_of_supplierDTO(SupplierCard supplier, String name) {
        this.supplier = new SupplierDTO(supplier);
        this.name = name;
    }
    public contact_of_supplierDTO() {
    }

    //region Methods

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "contact{" +
                "supplier=" + supplier +
                ", name='" + name + '\'' +
                '}';
    }

    //endregion
}
