package bussines_layer;

import java.util.LinkedList;
import java.util.List;

public class BranchController {

    private List<Branch> branches;
    private SupplierController supplierController;

    public BranchController (){
        branches= new LinkedList<>();
        supplierController = SupplierController.getInstance();
    }
}
