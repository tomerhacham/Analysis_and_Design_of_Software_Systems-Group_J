package bussines_layer.supplier_module;

import java.util.HashMap;
import java.util.LinkedList;

public class ContractController {

    private LinkedList<Contract> contracts;
    private Integer contractidCounter;
    private HashMap<Integer , LinkedList<Product>> SupProducts; // <supplierId , ProductsList> //TODO contract id ??
   // private HashMap<Integer , SupplierCard> supplierCardHashMap; // <supplierId , SupplierCard>


    public ContractController(){
        contracts = new LinkedList<>();
        contractidCounter = 0;
    }

}
