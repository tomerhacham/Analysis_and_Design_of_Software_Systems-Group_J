package BusinessLayer.Workers;

import DataAccessLayer.DTO.Worker_DTO;
import DataAccessLayer.Mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Roster {
    private List<Worker>workers;
    private static Roster roster;
    private Mapper mapper = Mapper.getInstance();

    private Roster() {
        workers=new ArrayList<>();
    }

    public static Roster getInstance()
    {
      if(roster==null)
          roster=new Roster();
      return roster;
    }

    public String addDriver(String name, double salary, Date startDate,String license)
    {
        UUID uuid = UUID.randomUUID();
        String output = checkNewWorkerInputValidity(name, salary, startDate);
        if (output != null)
            return output;
        if(license==null||license.length()==0)
            return "Invalid license input";
        Driver driver= new Driver(uuid.toString(),license,name,startDate,salary);
        workers.add(driver);
        Mapper.getInstance().addDriver(driver);
        return null;
    }

    public String addWorker(String name, double salary, Date startDate, List<String> positions)
    {
        UUID uuid = UUID.randomUUID();
        String output = checkNewWorkerInputValidity(name, salary, startDate);
        if (output != null) return output;
        Worker w=new Worker(name,uuid.toString(),startDate,salary);
        workers.add(w);
        if(positions!=null) {
            for (String pos : positions) {
                if(!pos.toLowerCase().equals("driver"))
                    w.addPosition(pos);
            }
        }
        mapper.addWorker(w);
        return null;
    }

    private String checkNewWorkerInputValidity(String name, double salary, Date startDate)
    {
        if(name==null||name.length()==0)
            return "Illegal name";
        if(salary<0)
            return "Illegal salary";
        if(startDate==null)
            return "Invalid date";
        return null;
    }

    public String removeWorker(String id)
    {
        if(id==null)
            return "Invalid ID";
        Worker searched = findWorker(id);
        if(searched==null)
            return "The worker does not exist";
        workers.remove(searched);
        if(searched.getLicense()!=null) {
            mapper.deleteDriver(id);
        }
        else {
            mapper.deleteWorker(searched.getId());
        }
        return null;
    }
     
    public List<Worker> getWorkers() {
        List<Worker> exisitingWorkers=mapper.getAllWorkers();
        if(exisitingWorkers!=null)
        {
            for(Worker ew:exisitingWorkers) {
                boolean found=false;
                Worker temp=null;
                for (Worker w : workers) {
                    if(ew.getId().equals(w.getId())) {
                        found = true;
                        break;
                    }
                }
                if(!found)
                    workers.add(ew);
            }
        }
        return workers;
    }

    public String editName(String newName, String id)
    {
        if(id==null)
            return "Invalid ID";
        if(newName==null||newName.length()==0)
            return "Invalid name";
        Worker searched = findWorker(id);
        if(searched==null)
            return "The worker does not exist";
        searched.setName(newName);
        mapper.updateWorker(searched);
        return null;
    }
     
    public String editSalary(double newSalary, String id)
    {
        if(id==null)
            return "Invalid ID";
        if(newSalary<0)
            return "Invalid salary";
        Worker searched = findWorker(id);
        if(searched==null)
            return "The worker does not exist";
        searched.setSalary(newSalary);
        mapper.updateWorker(searched);
        return null;
    }
     
    public String addPosition(String pos, String id)
    {
        if(id==null)
            return "Invalid ID";
        if(pos==null||pos.length()==0)
            return "Invalid Position";
        Worker searched = findWorker(id);
        if(searched==null)
            return "The worker does not exist";
        searched.addPosition(pos);
        mapper.addPosition(pos,searched.getId());
        return null;
    }
     
    public String removePosition(String pos, String id)
    {
        if(id==null)
            return "Invalid ID";
        if(pos==null||pos.length()==0)
            return "Invalid Position";
        Worker worker = findWorker(id);
        if(worker==null)
            return "The worker does not exist";
        String position = pos.toLowerCase();
        String output=Scheduler.getInstance().isWorkerScheduled(worker,position);
        if(output.length()>0)
            return  "unable to remove the position because the worker is scheduled to fill it on:\n"+output+"\n";
        worker.removePosition(position);
        mapper.deletePosition(pos,worker.getId());
        return null;
    }

    public Worker findWorker(String id)
    {
        Worker searched=null;
        for(Worker w:workers)
        {
            if(w.getId()==id)
            {
                searched=w;
                break;
            }
        }
        if(searched==null)
        {
            searched=mapper.getWorker(id);
            if (searched!=null) {
                workers.add(searched);
            }
        }
        return searched;
    }


    public void removeExistingWorkers() {
        List<Worker>existingWorkers=mapper.getAllWorkers();
        if(existingWorkers!=null) {
            for (Worker w : existingWorkers) {
                mapper.deleteWorker(w.getId());
            }
        }
    }
}
