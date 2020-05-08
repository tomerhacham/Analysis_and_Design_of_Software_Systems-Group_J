package BusinessLayer.Workers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Roster {
    private List<Worker>workers;
    private static Roster roster;

    private Roster() {
        workers=new ArrayList<>();
    }

    public static Roster getInstance(){
      if(roster==null)
          roster=new Roster();
      return roster;
    }

    public String addDriver(String name, double salary, Date startDate,String license)
    {
        UUID uuid = UUID.randomUUID();
        String output = checkNewWorkerInputValdity(name, salary, startDate);
        if (output != null) return output;
        workers.add(new Driver(uuid.toString(),license,name,startDate,salary));
        return null;
    }

    public String addWorker(String name, double salary, Date startDate, List<String> positions)
    {
        UUID uuid = UUID.randomUUID();
        String output = checkNewWorkerInputValdity(name, salary, startDate);
        if (output != null) return output;
        Worker w=new Worker(name,uuid.toString(),startDate,salary);
        workers.add(w);
        if(positions!=null) {
            for (String pos : positions) {
                if(!pos.equals("driver"))
                    w.addPosition(pos);
            }
        }
        return null;
    }

    private String checkNewWorkerInputValdity(String name, double salary, Date startDate) {
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
        return null;
    }
     
    public List<Worker> getWorkers() {
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
        return null;
    }
     
    public String removePosition(String pos, String id){
        if(id==null)
            return "Invalid ID";
        if(pos==null||pos.length()==0)
            return "Invalid Position";
        Worker searched = findWorker(id);
        if(searched==null)
            return "The worker does not exist";
        searched.removePosition(pos);
        return null;
    }
     
    public Worker findWorker(String id) {
        Worker searched=null;
        for(Worker w:workers)
        {
            if(w.getId()==id)
            {
                searched=w;
                break;
            }
        }
        return searched;
    }

    public String changeLicencse(String license,String id) {
        Worker w=findWorker(id);
        if(license==null||license.length()==0)
            return "Not a valid license";
        if(w==null)
            return "The worker does not exist";
        return w.setLicense(license);
    }
}
