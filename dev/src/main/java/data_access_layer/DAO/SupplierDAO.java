package data_access_layer.DAO;

import bussines_layer.SupplierCard;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.SupplierDTO;
import data_access_layer.DTO.contact_of_supplierDTO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SupplierDAO {
    //fields
    HashMap<Integer, SupplierCard> identityMap;
    public Dao<SupplierDTO,Integer> dao;
    private Dao<contact_of_supplierDTO,Void> contacts_of_supplier_dao;

    //Constructor
    public SupplierDAO(ConnectionSource conn) {
        try {
            this.dao = DaoManager.createDao(conn,SupplierDTO.class);
            this.contacts_of_supplier_dao = DaoManager.createDao(conn,contact_of_supplierDTO.class);
            this.dao.setObjectCache(true);
            this.identityMap=new HashMap<>();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * find and returns the supplierCard object in the repository
     * @param supplier_id
     * @return
     */
    public SupplierCard find(Integer supplier_id){
        SupplierCard supplierCard=null;
        if(identityMap.containsKey(supplier_id)){
            supplierCard=identityMap.get(supplier_id);
        }
        else{
            try {
                SupplierDTO dto = dao.queryForId(supplier_id);
                LinkedList<String> contactNames = new LinkedList<>();
                for(contact_of_supplierDTO contact:dto.getContact_list()){
                    contactNames.add(contact.getName());
                }
                SupplierCard _supplierCard = new SupplierCard(dto,contactNames);
                identityMap.put(supplier_id,_supplierCard);
                supplierCard=_supplierCard;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    return supplierCard;
    }
    /**
     * writing supplierCard  and all its contactList to the DB
     * @param supplier
     */
    public void create(SupplierCard supplier){
        if(!identityMap.containsKey(supplier.getId())){identityMap.put(supplier.getId(),supplier);}
        SupplierDTO supplierDTO = new SupplierDTO(supplier);
        List<String> contactList = supplier.getContactsName();
        LinkedList<contact_of_supplierDTO> contact_of_supplierDTOS = new LinkedList<>();
        for(String contact:contactList){
            contact_of_supplierDTOS.add(new contact_of_supplierDTO(supplierDTO,contact));
        }
        try {
            dao.create(supplierDTO);
            System.err.println(String.format("[Writing] %s", supplierDTO));
            contacts_of_supplier_dao.create(contact_of_supplierDTOS);
            System.err.println(String.format("[Writing] %s", concatObjectList(contact_of_supplierDTOS)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * update supplierCard  and all its contactList to the DB
     * @param supplier
     */
    public void update(SupplierCard supplier){
        try{
            SupplierDTO supplierDTO = new SupplierDTO(supplier);
            dao.update(supplierDTO);
            identityMap.replace(supplier.getId(),supplier);
            UpdateBuilder<contact_of_supplierDTO, Void> updateBuilder = contacts_of_supplier_dao.updateBuilder();
            for(String contactNamde : supplier.getContactsName()){
                // set criterias
                updateBuilder.where().eq("supplier_id", supplier.getId());
                // update the field(s)
                updateBuilder.updateColumnValue("name" ,contactNamde);
                updateBuilder.update();
            }
        }
        catch (Exception e){e.printStackTrace();}
    }
    /**
     * add a contact to the supplierCard and update the DB
     * @param supplier
     * @param contactName
     */
    public void create_contact(String contactName ,SupplierCard supplier){
        if(identityMap.containsKey(supplier.getId())){identityMap.replace(supplier.getId(),supplier);}
        else{identityMap.put(supplier.getId(),supplier);}
        contact_of_supplierDTO contact_of_supplierDTO = new contact_of_supplierDTO(supplier,contactName);
        try {
            contacts_of_supplier_dao.create(contact_of_supplierDTO);
            System.err.println(String.format("[Writing] %s", contact_of_supplierDTO));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * delete a contact from the supplier card and update the DB
     * @param supplier
     * @param contact
     */
    public void delete_contact(String contact , SupplierCard supplier){
        try {
            if(identityMap.containsKey(supplier.getId())){identityMap.replace(supplier.getId(),supplier);}
            else{identityMap.put(supplier.getId(),supplier);}
            DeleteBuilder<contact_of_supplierDTO,Void> deleteBuilder = contacts_of_supplier_dao.deleteBuilder();
            // only delete the rows on "order_id" and "catalog_id"
            deleteBuilder.where().eq("supplier_id" , supplier.getId()).and().eq("name" , contact);
            deleteBuilder.delete();
            System.err.println(String.format("[DELETE] %s", contact));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * delete a the supplier and update the DB
     * @param supplier
     */
    public void delete(SupplierCard supplier){
        try{
            if(identityMap.containsKey(supplier.getId())){identityMap.remove(supplier.getId());}
            SupplierDTO supplierDTO = new SupplierDTO(supplier);
            dao.delete(supplierDTO);
            System.err.println(String.format("[DELETE] %s", supplierDTO));
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
    //endregion

}
