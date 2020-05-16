package data_access_layer.DAO;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.supplier_module.Contract;
import bussines_layer.supplier_module.CostEngineering;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.CostEngineeringDTO;
import data_access_layer.DTO.GeneralProductDTO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CostEngineeringDAO {
    //fields:
    HashMap<Integer, CostEngineering> identityMap;
    Dao<CostEngineeringDTO,Void> dao;

    //Constructor
    public CostEngineeringDAO(ConnectionSource conn) {
        try {
            identityMap = new HashMap<>();
            dao= DaoManager.createDao(conn,CostEngineeringDTO.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public CostEngineering find(Integer contract_id , Integer branch_id){
        CostEngineering costEngineering=null;
        if(identityMap.containsKey(contract_id)){
            costEngineering=identityMap.get(contract_id);
        }
        else{
            try {
                List<CostEngineeringDTO> costEngineeringDTOS = dao.queryBuilder().where().eq("contract_id", contract_id).and().eq("branch_id",branch_id).query();
                costEngineering = new CostEngineering(costEngineeringDTOS);
                identityMap.put(costEngineering.getContract_id() , costEngineering);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return costEngineering;

    }
    /**
     * write CostEngineering to the DB
     * @param costEngineering
     */
    public void create(CostEngineering costEngineering ){

        HashMap<Integer , Integer> minQuntity = costEngineering.getMinQuntity(); // <catalogid , quantity>
        HashMap<Integer , Float> newPrice = costEngineering.getNewPrice(); // <catalogid , newPrice>

        try {
            for (Integer catalogid : minQuntity.keySet()) {
                CostEngineeringDTO costEngineeringDTO = new CostEngineeringDTO(costEngineering , catalogid , minQuntity.get(catalogid) , newPrice.get(catalogid) );
                dao.create(costEngineeringDTO);
                System.err.println(String.format("[Writing] %s", costEngineeringDTO));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update CostEngineering to the DB
     * @param costEngineering
     */
    public void update (CostEngineering costEngineering ){
        try{

            HashMap<Integer , Integer> minQuntity = costEngineering.getMinQuntity(); // <catalogid , quantity>
            HashMap<Integer , Float> newPrice = costEngineering.getNewPrice(); // <catalogid , newPrice>
            UpdateBuilder<CostEngineeringDTO, Void> updateBuilder = dao.updateBuilder();

            for (Integer catalogid : minQuntity.keySet()) {
                // set criterias
                updateBuilder.where().eq("contract_id", costEngineering.getContract_id()).and().eq("branch_id" , costEngineering.getBranch_id()).and().eq("catalog_id" , catalogid);
                // update the field(s)
                updateBuilder.updateColumnValue("min_quantity" ,minQuntity.get(catalogid));
                updateBuilder.updateColumnValue("discount_price" , newPrice.get(catalogid));
                updateBuilder.update();
            }
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete the cost engineering from the DB
     * @param costEngineering
     */
    public void delete (CostEngineering costEngineering ){
        HashMap<Integer , Integer> minQuntity = costEngineering.getMinQuntity(); // <catalogid , quantity>
        HashMap<Integer , Float> newPrice = costEngineering.getNewPrice(); // <catalogid , newPrice>

        try {
            for (Integer catalogid : minQuntity.keySet()) {
                CostEngineeringDTO costEngineeringDTO = new CostEngineeringDTO(costEngineering , catalogid , minQuntity.get(catalogid) , newPrice.get(catalogid) );
                DeleteBuilder<CostEngineeringDTO,Void> deleteBuilder = dao.deleteBuilder();
                // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
                deleteBuilder.where().eq("contract_id", costEngineering.getContract_id()).and().eq("branch_id" , costEngineering.getBranch_id()).and().eq("catalog_id" , catalogid);
                deleteBuilder.delete();
                System.err.println(String.format("[Deleting] %s", costEngineeringDTO));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //region product in cost engineering
    /**
     * add a product to CostEngineering and update the DB
     * @param product
     * @param costEngineering
     */
    public void addProductToCostEngineering(CatalogProduct product , CostEngineering costEngineering ){
        try {
            CostEngineeringDTO costEngineeringDTO = new CostEngineeringDTO(costEngineering , product.getCatalogID() , costEngineering.getMinQuntity().get(product.getCatalogID()) , costEngineering.getNewPrice().get(product.getCatalogID()) );
            dao.create(costEngineeringDTO);
            System.err.println(String.format("[Writing] %s", costEngineeringDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * delete a product from cost engineering and update the DB
     * @param product
     * @param costEngineering
     */
    public void deleteProductFromCostEngineering (CatalogProduct product , CostEngineering costEngineering ){
        try {
            DeleteBuilder<CostEngineeringDTO,Void> deleteBuilder = dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("contract_id", costEngineering.getContract_id()).and().eq("branch_id" , costEngineering.getBranch_id()).and().eq("catalog_id" , product.getCatalogID());
            deleteBuilder.delete();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //endregion
}
