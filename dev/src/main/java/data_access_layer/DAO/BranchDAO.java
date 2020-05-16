package data_access_layer.DAO;

import bussines_layer.Branch;
import bussines_layer.SupplierCard;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.java.swing.plaf.windows.WindowsDesktopIconUI;
import data_access_layer.DTO.BranchDTO;
import data_access_layer.DTO.SupplierDTO;
import data_access_layer.DTO.contact_of_supplierDTO;

import java.sql.SQLException;
import java.util.HashMap;

public class BranchDAO {
    //fields
    HashMap<Integer, Branch> identityMap;
    public Dao<BranchDTO,Integer> dao;

    //Constructor
    public BranchDAO(ConnectionSource conn){
        try {
            this.dao = DaoManager.createDao(conn,BranchDTO.class);
            this.identityMap=new HashMap<>();
            this.dao.setObjectCache(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * find and return Branch object
     * @param branch_id
     * @return
     */
    public Branch find(Integer branch_id){
        Branch branch=null;
        if(identityMap.containsKey(branch_id)){
            branch=identityMap.get(branch_id);
        }
        else{
            try {
                branch = new Branch(dao.queryForId(branch_id));
                identityMap.put(branch_id,branch);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return branch;
    }
    /**
     * write Branch to the DB
     * @param branch
     */
    public void create(Branch branch){
        BranchDTO branchDTO=new BranchDTO(branch);
        if(!identityMap.containsKey(branch.getBranchId())){identityMap.put(branch.getBranchId(),branch);}
        try{
            dao.create(branchDTO);
        }
        catch(Exception e){e.printStackTrace();}
    }
    /**
     * change the Branch name and update the DB
     * @param branch
     */
    public void update(Branch branch){
        if(identityMap.containsKey(branch.getBranchId())){identityMap.replace(branch.getBranchId(),branch);}
        try{
            BranchDTO branchDTO=new BranchDTO(branch);
            dao.update(branchDTO);
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * delete Branch from the DB
     * @param branch
     */
    public void delete(Branch branch){
        if(identityMap.containsKey(branch.getBranchId())){identityMap.remove(branch.getBranchId(),branch);}
        try{
            BranchDTO branchDTO=new BranchDTO(branch);
            dao.delete(branchDTO);
        }catch (Exception e){e.printStackTrace();}
    }

    //region Utilities
    public void clearCache(){
        this.identityMap.clear();
    }
    //endregion
}
