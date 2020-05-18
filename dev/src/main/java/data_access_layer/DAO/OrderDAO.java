package data_access_layer.DAO;

import bussines_layer.Branch;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.CatalogProductDTO;
import data_access_layer.DTO.ContractDTO;
import data_access_layer.DTO.OrderDTO;
import data_access_layer.DTO.catalog_product_in_orderDTO;
import data_access_layer.Mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class OrderDAO {
    //feilds:
    HashMap<Integer, Order> identityMap;
    public Dao<OrderDTO,Void> dao;
    Dao<catalog_product_in_orderDTO,Void>catalog_product_in_order_dao;

    //Constructor
    public OrderDAO(ConnectionSource conn) {
        try {
            this.identityMap=new HashMap<>();
            this.dao = DaoManager.createDao(conn,OrderDTO.class);
            this.catalog_product_in_order_dao=DaoManager.createDao(conn,catalog_product_in_orderDTO.class);
            //this.dao.setObjectCache(true); TODO update Tomer
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Order find(Integer order_id, Integer branch_id){
        Order order=null;
        if(identityMap.containsKey(order_id)){
            order=identityMap.get(order_id);
        }
        else{
            try {
                OrderDTO dto = dao.queryBuilder().where().eq("order_id",order_id).and().eq("branch_id",branch_id).queryForFirst();
                if(dto!=null) {
                    order = new Order(dto);
                    identityMap.put(order_id, order);
                    List<catalog_product_in_orderDTO> catalog_product_in_orderDTOS = catalog_product_in_order_dao.queryBuilder().where().eq("order_id", order_id).and().eq("branch_id", branch_id).query();
                    if (catalog_product_in_orderDTOS != null && !catalog_product_in_orderDTOS.isEmpty()) {
                        HashMap<CatalogProduct, Integer> productsAndQuantity = new HashMap<>(); // <product , quantity>
                        HashMap<CatalogProduct, Float> productsAndPrice = new HashMap<>(); //<product, price>
                        for (catalog_product_in_orderDTO catalog_dto : catalog_product_in_orderDTOS) {
                            CatalogProduct catalogProduct = Mapper.getInstance().find_CatalogProduct(catalog_dto.getCatalog_id(), catalog_dto.getBranch_id());
                            if(catalogProduct!=null){
                                productsAndQuantity.put(catalogProduct, catalog_dto.getQuantity());
                                productsAndPrice.put(catalogProduct, catalog_dto.getPrice());
                            }
                        }
                        order.setSupplier(Mapper.getInstance().find_Supplier(dto.getSupplier()));
                        order.setProductsAndPrice(productsAndPrice);
                        order.setProductsAndQuantity(productsAndQuantity);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return order;
    }
    /**
     * write single order and it associate classes to the DB
     * @param order
     */
    public void create(Order order){
        OrderDTO orderDTO = new OrderDTO(order);
        if(!identityMap.containsKey(order.getOrderID())){identityMap.put(order.getOrderID(),order);}
        LinkedList<catalog_product_in_orderDTO> catalog_product_in_order = new LinkedList<>();
        for(CatalogProduct product:order.getProductsAndPrice().keySet()){
            catalog_product_in_order.add(new catalog_product_in_orderDTO(order,product,order.getProductsAndQuantity().get(product),order.getProductsAndPrice().get(product)));
        }
        try {
            dao.create(orderDTO);
            catalog_product_in_order_dao.create(catalog_product_in_order);
            //System.err.println(String.format("[Writing] %s", orderDTO));
            //System.err.println(String.format("[Writing] %s", concatObjectList(catalog_product_in_order)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update single order and all its products to the DB
     * @param order
     */
    public void update(Order order){
        try{
            if (identityMap.containsKey(order.getOrderID())){identityMap.replace(order.getOrderID(),order);}
            OrderDTO orderDTO = new OrderDTO(order);
            UpdateBuilder<OrderDTO, Void> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("order_id", order.getOrderID()).and().eq("branch_id" ,order.getBranch_id());
            // update the field(s)
            updateBuilder.updateColumnValue("supplier_id" ,order.getSupplierID());
            updateBuilder.updateColumnValue("issue_date" ,order.getIssuedDate());
            updateBuilder.updateColumnValue("day_to_deliver" ,order.getDayToDeliver());
            updateBuilder.updateColumnValue("status" ,order.getStatus());
            updateBuilder.updateColumnValue("type" ,order.getType());
            updateBuilder.update();

            HashMap<CatalogProduct , Integer> productAndQuantity = order.getProductsAndQuantity();
            HashMap<CatalogProduct , Float> productAndPrice = order.getProductsAndPrice();
            UpdateBuilder<catalog_product_in_orderDTO, Void> updateBuilder_catalogProduct = catalog_product_in_order_dao.updateBuilder();

            for(CatalogProduct product:order.getProductsAndPrice().keySet()){
                // set criterias
                updateBuilder_catalogProduct.where().eq("order_id", order.getOrderID()).and().eq("catalog_id" , product.getCatalogID()).and().eq("GPID",product.getGpID()).and().eq("branch_id",order.getBranch_id());
                // update the field(s)
                updateBuilder_catalogProduct.updateColumnValue("quantity" ,productAndQuantity.get(product));
                updateBuilder_catalogProduct.updateColumnValue("price" , productAndPrice.get(product));
                updateBuilder_catalogProduct.update();
            }
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete the order from the DB
     * @param order
     */
    public void delete(Order order){
        try{
            if (identityMap.containsKey(order.getOrderID())){identityMap.remove(order.getOrderID(),order);}
            OrderDTO orderDTO = new OrderDTO(order);
            for(CatalogProduct catalogProduct:order.getProductsAndPrice().keySet()){
                delete(order,catalogProduct);
            }
            DeleteBuilder<OrderDTO,Void> deleteBuilder = dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("order_id", order.getOrderID()).and().eq("branch_id" ,order.getBranch_id());
            deleteBuilder.delete();
            //System.err.println(String.format("[Delete] %s", orderDTO));
            //catalog_product_in_order are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * add a specific product to a single order ant update the DB
     * @param product
     * @param order
     * @param quantity
     * @param price
     */
    public void addCatalogProductToOrder(CatalogProduct product ,Order order,Integer quantity , Float price){
        try {
            catalog_product_in_orderDTO catalog_product_in_orderDTO = new catalog_product_in_orderDTO(order, product, quantity, price);
            catalog_product_in_order_dao.create(catalog_product_in_orderDTO);
            //System.err.println(String.format("[Writing] %s", catalog_product_in_orderDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * delete a specific product from the order and update the DB
     * @param product
     * @param order
     */
    public void delete(Order order,CatalogProduct product){
        try{
            DeleteBuilder<catalog_product_in_orderDTO,Void> deleteBuilder = catalog_product_in_order_dao.deleteBuilder();
            // only delete the rows on "order_id" and "catalog_id"
            deleteBuilder.where().eq("order_id", order.getOrderID()).and().eq("catalog_id" , product.getCatalogID()).and().eq("GPID",product.getGpID()).and().eq("branch_id",order.getBranch_id());
            deleteBuilder.delete();
        }catch (Exception e){e.printStackTrace();}
    }

    //region Utilities
    public void clearCache(){
        this.identityMap.clear();
    }
    private String concatObjectList(List list){
        String string="";
        for (Object object:list){string=string.concat(object.toString().concat("\n"));}
        return string;
    }

    public void deleteByBranch(Integer branch_id) {
        try {
            List<OrderDTO> list = dao.queryBuilder().where().eq("branch_id",branch_id).query();
            if (list !=null && !list.isEmpty()) {
                for (OrderDTO dto : list) {
                    Order order = find(dto.getOrder_id(),branch_id);
                    if (order!=null){
                        delete(order);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //endregion
}
