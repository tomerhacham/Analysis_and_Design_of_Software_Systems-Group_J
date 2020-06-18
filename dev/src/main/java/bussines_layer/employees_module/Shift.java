package bussines_layer.employees_module;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Shift {
    private HashMap<String, FixedSizeList<Worker>> occupation;
    private Date date;
    private boolean timeOfDay;
    private String id;
    private Integer branch_id;
    //private List<Worker> availableWorkers;
    private List<Driver> scheduledDrivers;


    public Shift(Date date, boolean timeOfday, String id,Integer branch_id)
    {
        this.id=id;
        scheduledDrivers=new ArrayList<>();
        this.timeOfDay=timeOfday;
        this.date=date;
        this.branch_id=branch_id;
        occupation=new HashMap<>();
        occupation.put("manager",new FixedSizeList<>(1));
    }

    public Shift(Shift other)
    {
        if (other!=null){
            this.id = other.id;
            this.timeOfDay=other.timeOfDay;
            this.date=other.date;
            scheduledDrivers=new ArrayList<>();
            scheduledDrivers.addAll(other.scheduledDrivers);
            occupation=new HashMap<>();
            for(String pos:other.occupation.keySet())
            {
                FixedSizeList<Worker> temp=new FixedSizeList<>(other.occupation.get(pos).capacity());
                temp.addAll(other.occupation.get(pos));
                this.occupation.put(pos,temp);
            }
        }
    }

    public Integer getBranchID() {
        return branch_id;
    }
    public String getId() {
        return id;
    }

    public void setTimeOfDay(boolean timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Date getDate() {
        return date;
    }

    public boolean getTimeOfDay() {
        return timeOfDay;
    }

    public String addPosition(String pos,int quantity)
    {
        if(pos==null||pos.length()==0||pos.equals("driver"))
            return "Invalid position";
        if(quantity<=0)
            return "Invalid quantity";
        if(occupation.containsKey(pos))
            return "The position already exists";
        occupation.put(pos,new FixedSizeList<>(quantity));
        return null;
    }
/*
    public void setAvailableWorkers(List<Worker> availableWorkers) {
        this.availableWorkers = availableWorkers;
    }

   */

    public List<Driver> getScheduledDrivers() {
        return scheduledDrivers;
    }

    public String removePosition(String pos)
    {
        if(!occupation.containsKey(pos))
            return "The shift does not contain this position";
        //availableWorkers.addAll(occupation.get(pos));
        occupation.remove(pos);
        return null;
    }

    public String removeWorkerFromPosition(String position,String id,List<Worker> availableWorkers) {
        if(!occupation.containsKey(position))
            return "The position does not exist";
        Worker removed=occupation.get(position).findAndRemove((w)->
                w.getId()==id);
        if(removed!=null)
        {
            //availableWorkers.add(removed);
            return null;
        }
        return "The worker is not scheduled for this shift";
    }
/*
    private String replaceDriver(String id,List<Worker> availableWorkers) {
        Driver removedDriver=null;
        for(Driver d:scheduledDrivers)
        {
            if(d.getId().equals(id)) {
                removedDriver = d;
                break;
            }
        }
        if(removedDriver!=null)
        {
            Driver replacementDriver=null;
            for(Worker w:availableWorkers) {
                if (w.positions.contains("driver") && w.getLicense().equals(removedDriver.getLicense())) {
                    replacementDriver = (Driver) w;
                    break;
                }
            }
            if(replacementDriver!=null) {
                scheduledDrivers.remove(removedDriver);
                //availableWorkers.add(removedDriver);
                scheduledDrivers.add(replacementDriver);
                //availableWorkers.remove(replacementDriver);
                return null;
                }
            else
                return "Driver was not removed-There is no replacement for the wanted driver";
        }
        return "The worker is not scheduled for this shift";
    }
*/
    public String addWorkerToPosition(String position,String id,List<Worker> availableWorkers)
    {
        Worker w=null;
        if(!occupation.containsKey(position))
            return "The position does not exist";
        else if(occupation.get(position).isFull())
            return "The position is Full";
        else {
            w=findIfAvailable(id,availableWorkers);
            if(w==null)
                return "The worker is not available";
            if(!w.getPositions().contains(position)) {
                return "The worker can't work in this position";
            }
            //availableWorkers.remove(w);
            occupation.get(position).add(w);
        }
            return null;
    }

    public void addDriverToShift(Driver driver)
    {
        scheduledDrivers.add(driver);
    }

    public Driver removeDriver(String id)
    {
        Driver toRemove=null;
        for(Driver d:scheduledDrivers) {
            if (d.getId().equals(id)) {
                toRemove = d;
            }
        }
        if(toRemove!=null)
            scheduledDrivers.remove(toRemove);
        return toRemove;
    }

    public HashMap<String, FixedSizeList<Worker>> getOccupation() {
        return occupation;
    }

    public boolean isValid()
    {
        if(!occupation.containsKey("manager"))
            return false;
        for(FixedSizeList fsl:occupation.values())
        {
            if(!fsl.isFull())
                return false;
        }
        return true;
    }

    private Worker findIfAvailable(String id,List<Worker> availableWorkers) {
        Worker avail=null;
        for(Worker w :availableWorkers)
        {
            if(w.getId().equals(id))
            {
                avail=w;
                break;
            }
        }
        return avail;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //function for mapper
    public void addARowToOcuupation(String pos, FixedSizeList<Worker> workers) {
        this.occupation.put(pos, workers);
    }

}
