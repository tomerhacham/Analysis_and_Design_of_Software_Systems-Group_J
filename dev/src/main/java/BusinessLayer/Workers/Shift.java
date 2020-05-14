package BusinessLayer.Workers;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Shift {
    private HashMap<String, FixedSizeList<Worker>> occupation;
    private Date date;
    private boolean timeOfDay;
    private List<Worker> availableWorkers;
    private List<Driver> scheduledDrivers;

    public void setTimeOfDay(boolean timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Shift(List<Worker>availableWorkers, Date date, boolean timeOfday)
    {
        scheduledDrivers=new ArrayList<>();
        this.timeOfDay=timeOfday;
        this.date=date;
        occupation=new HashMap<>();
        this.availableWorkers=availableWorkers;
        occupation.put("manager",new FixedSizeList<>(1));
    }

    public Shift(Shift other)
    {
        this.timeOfDay=other.timeOfDay;
        this.date=other.date;
        this.availableWorkers=new ArrayList<>();
        this.availableWorkers.addAll(other.availableWorkers);
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

    public List<Worker> getAvailableWorkers() {
        return availableWorkers;
    }

    /*
        public String addAvailableWorker(Worker worker)
        {
            if(getAllAvailableWorkers().contains(worker))
                return "The worker is already registered as available";
            availableWorkers.add(worker);
            return null;
        }
        public List<Worker> getAllAvailableWorkers()
        {
            List<Worker>allWorkers=new ArrayList<>();
            allWorkers.addAll(availableWorkers);
            for(FixedSizeList<Worker> fsl:occupation.values())
                allWorkers.addAll(fsl);
            return allWorkers;
        }

     */

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

    public void setAvailableWorkers(List<Worker> availableWorkers) {
        this.availableWorkers = availableWorkers;
    }

    public List<Driver> getScheduledDrivers() {
        return scheduledDrivers;
    }

    public String removePosition(String pos)
    {
        if(!occupation.containsKey(pos))
            return "The shift does not contain this position";
        availableWorkers.addAll(occupation.get(pos));
        occupation.remove(pos);
        return null;
    }

    public String removeWorkerFromPosition(String position,String id) {
        if(position.equals("driver"))
            return replaceDriver(id);
        if(!occupation.containsKey(position))
            return "The position does not exist";
        Worker removed=occupation.get(position).findAndRemove((w)->
                w.getId()==id);
        if(removed!=null)
        {
            availableWorkers.add(removed);
            return null;
        }
        return "The worker is not scheduled for this shift";
    }

    private String replaceDriver(String id) {
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
                availableWorkers.add(removedDriver);
                scheduledDrivers.add(replacementDriver);
                availableWorkers.remove(replacementDriver);
                return null;
                }
            else
                return "Driver was not removed-There is no replacement for the wanted driver";
        }
        return "The worker is not scheduled for this shift";
    }

    public String addWorkerToPosition(String position,String id)
    {
        Worker w=null;
        if(!occupation.containsKey(position))
            return "The position does not exist";
        else if(occupation.get(position).isFull())
            return "The position is Full";
        else {
            w=findIfAvailable(id);
            if(w==null)
                return "The worker is not available";
            if(!w.getPositions().contains(position)) {
                return "The worker can't work in this position";
            }
            availableWorkers.remove(w);
            occupation.get(position).add(w);
        }
            return null;
    }

    public void addDriverToShift(Driver driver)
    {
        scheduledDrivers.add(driver);
        availableWorkers.remove(driver);
    }

    public Driver removeDriver(String id)
    {
        for(Driver d:scheduledDrivers)
        {
            if(d.getId().equals(id))
                return d;
        }
        return null;
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

    private Worker findIfAvailable(String id) {
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

}
