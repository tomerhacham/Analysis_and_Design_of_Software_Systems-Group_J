package InterfaceLayer.Workers;

import BusinessLayer.Workers.Roster;
import BusinessLayer.Workers.Worker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RosterController {
    Roster roster;

    public RosterController() {
        roster=Roster.getInstance();
    }
    public List<ModelWorker> displayWorkers()
    {
        List<Worker> workers=roster.getWorkers();
        List<ModelWorker> output=new ArrayList<>();
        for(Worker w:workers)
        {
            output.add(new ModelWorker(w));
        }
        return output;
    }
    public ModelWorker displaySingleWorker(String id)
    {
        return new ModelWorker(roster.findWorker(id));
    }
    public String editName(String newName,String id)
    {
        return roster.editName(newName,id);
    }
    public String editSalry(double newSalary,String id)
    {
        return roster.editSalary(newSalary,id);
    }
    public String addPosition(String pos,String id)
    {
        return roster.addPosition(pos,id);
    }
    public String removePosition(String pos,String id)
    {
        return roster.removePosition(pos,id);
    }
    public String addWorker(String name, double salary, Date startDate,List<String>positions)
    {
        return roster.addWorker(name,salary,startDate,positions);
    }
    public String removeWorker(String id)
    {
        return roster.removeWorker(id);
    }
}
