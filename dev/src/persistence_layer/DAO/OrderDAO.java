package persistence_layer.DAO;

import bussines_layer.supplier_module.Order;

import java.util.WeakHashMap;

public class OrderDAO {
    //fields:
    WeakHashMap<Integer, Order> identityMap;
}
