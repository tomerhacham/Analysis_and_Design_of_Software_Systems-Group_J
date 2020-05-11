package persistence_layer;

import persistence_layer.DAO.*;

public class Mapper {
    //fields:
    private GeneralProductDAO general_product_dao;
    private SpecificProductDAO specific_product_dao;
    private CategoryDAO category_dao;
    private SaleDAO sale_dao;
    private OrderDAO order_dao;
    private ContractDAO contract_dao;
    private SupplierDAO supplier_dao;
    private BranchDAO branch_dao;
    private general_products_on_saleDAO general_products_on_sale_dao;
    private general_product_in_orderDAO general_product_in_order_dao;
    private CostEngineeringDAO cost_engineering_dao;
    private catalog_product_in_contractDAO catalog_product_in_contract_dao;

    //Constructor
    public Mapper() {
    }
}

