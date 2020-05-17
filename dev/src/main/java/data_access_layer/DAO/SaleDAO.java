package data_access_layer.DAO;

import bussines_layer.Branch;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.Sale;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.*;
import data_access_layer.Mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SaleDAO {
    //fields:
    HashMap<Integer, Sale> identityMap;
    public Dao<SaleDTO,Void> dao;
    Dao<general_product_on_saleDTO,Void> general_product_on_sale_dao;
    //Constructor

    public SaleDAO(ConnectionSource conn) {
        try {
            this.identityMap=new HashMap<>();
            this.dao= DaoManager.createDao(conn,SaleDTO.class);
            this.general_product_on_sale_dao = DaoManager.createDao(conn,general_product_on_saleDTO.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public Sale find(Integer sale_id, Integer branch_id){
        Sale sale=null;
        if(identityMap.containsKey(sale_id)){
            sale=identityMap.get(sale_id);
        }
        else{
            try {
                sale = new Sale(dao.queryBuilder().where().eq("sale_id",sale_id).and().eq("branch_id",branch_id).queryForFirst());
                identityMap.put(sale_id,sale);
                List<general_product_on_saleDTO> general_product_on_saleDTOS = general_product_on_sale_dao.queryBuilder().where().eq("sale_id",sale_id).and().eq("branch_id",branch_id).query();
                LinkedList<GeneralProduct> generalProducts=new LinkedList<>();
                for(general_product_on_saleDTO dto:general_product_on_saleDTOS){
                    generalProducts.add(Mapper.getInstance().find_GeneralProduct(dto.getGPID(),branch_id));
                }
                sale.setProducts_on_sale(generalProducts);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return sale;
    }
    /**
     * write Sale object to the DB and all its general product associate to it
     * @param sale
     */
    public void create(Sale sale) {
        try {
            if (!identityMap.containsKey(sale.getSale_id())){identityMap.put(sale.getSale_id(),sale);}
            SaleDTO saleDTO = new SaleDTO(sale);
            LinkedList<general_product_on_saleDTO> general_product_on_sale = new LinkedList<>();
            for (GeneralProduct generalProduct : sale.getProducts_on_sale()) {
                general_product_on_sale.add(new general_product_on_saleDTO(sale, generalProduct));
            }
            dao.create(saleDTO);
            System.err.println(String.format("[Writing] %s", saleDTO));
            general_product_on_sale_dao.create(general_product_on_sale);
            System.err.println(String.format("[Writing] %s", concatObjectList(general_product_on_sale)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update Sale object to the DB and all its general product associate to it
     * @param sale
     */
    public void update(Sale sale){
        try{
            if (identityMap.containsKey(sale.getSale_id())){identityMap.replace(sale.getSale_id(),sale);}
            SaleDTO saleDTO = new SaleDTO(sale);
            UpdateBuilder<SaleDTO, Void> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("sale_id",sale.getSale_id()).and().eq("branch_id" ,sale.getBranch_id());
            // update the field(s)
            updateBuilder.updateColumnValue("type" ,sale.getType());
            updateBuilder.updateColumnValue("start" ,sale.getStart());
            updateBuilder.updateColumnValue("end" ,sale.getEnd());
            updateBuilder.updateColumnValue("active" ,sale.getActive());
            updateBuilder.update();
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete Sale object to the DB and all its general product associate to it
     * @param sale
     */
    public void delete(Sale sale){
        try{
            if (identityMap.containsKey(sale.getSale_id())){identityMap.remove(sale.getSale_id(),sale);}
            //SaleDTO saleDTO = new SaleDTO(sale);
            DeleteBuilder<SaleDTO,Void> deleteBuilder = dao.deleteBuilder();
            // only delete the rows on "contract_id" and "branch_id" and "catalog_id"
            deleteBuilder.where().eq("sale_id",sale.getSale_id()).and().eq("branch_id" ,sale.getBranch_id());
            deleteBuilder.delete();
            cascadeDeleteGeneralProductsOnSale(sale);
            //System.err.println(String.format("[Delete] %s", saleDTO));
            //catalog_product_in_order are deleted due to cascade
        }catch (Exception e){e.printStackTrace();}
    }

    private void cascadeDeleteGeneralProductsOnSale(Sale sale) {
            DeleteBuilder<general_product_on_saleDTO,Void> deleteBuilder=general_product_on_sale_dao.deleteBuilder();
        try {
            deleteBuilder.where().eq("sale_id",sale.getSale_id()).and().eq("branch_id" ,sale.getBranch_id());
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //region Utilities
    private String concatObjectList(List list){
        String string="";
        for (Object object:list){string=string.concat(object.toString().concat("\n"));}
        return string;
    }
    public void clearCache(){this.identityMap.clear();}

    public void deleteByBranch(Branch branch) {
        try {
            List<SaleDTO> list = dao.queryBuilder().where().eq("branch_id",branch.getBranchId()).query();
            for(SaleDTO dto:list){
                delete(find(dto.getSale_id(),branch.getBranchId()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //endregion
}
