package bussines_layer;

import bussines_layer.inventory_module.Inventory;
import bussines_layer.supplier_module.SupplierModule;

public class Branch {
    //fields:
    private Integer branch_id;
    private Inventory inventory;
    private SupplierModule supplierModule;

    //Constructor
    public Branch(Integer branch_id) {
        this.branch_id = branch_id;
    }
}


