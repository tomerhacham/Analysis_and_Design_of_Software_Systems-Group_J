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

    public List<Worker> getAvailableWorkers() {
        return availableWorkers;
    }


    public Shift(List<Worker>availableWorkers, Date date, boolean timeOfday)
    {
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
        occupation=new HashMap<>();
        for(String pos:other.occupation.keySet())
        {
            FixedSizeList<Worker> temp=new FixedSizeList<>(other.occupation.get(pos).capacity());
            temp.addAll(other.occupation.get(pos));
            this.occupation.put(pos,temp);
        }
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
        if(pos==null||pos.length()==0)
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
    public String removePosition(String pos)
    {
        if(pos==null)
            return "Invalid position";
        if(!occupation.containsKey(pos))
            return "The shift does not contain this position";
        availableWorkers.addAll(occupation.get(pos));
        occupation.remove(pos);
        return null;
    }
    public String removeWorkerFromPosition(String position,String id) {
        if(!occupation.containsKey(position))
            return "The position does not exist";
        Worker removed=occupation.get(position).findAndRemove((w)->
                w.getId()==id);
        if(removed!=null)
        {
            availableWorkers.add(removed);
            return null;
        }
        else
            return "The worker wasn't scheduled";
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
            if(!w.getPositions().contains(position))
                return "The worker can't work in this position";
            occupation.get(position).add(w);
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
        if(avail!=null) {
            availableWorkers.remove(avail);
        }
        return avail;
    }

}
