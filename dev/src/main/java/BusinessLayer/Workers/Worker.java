package BusinessLayer.Workers;

import InterfaceLayer.Workers.ModelWorker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Worker {
    private String name;
    private String id;
    private Date start_Date;
    private double salary;
    List<String> positions;

    public Worker(String name, String id, Date startTime, double salary) {
        this.name = name;
        this.id = id;
        start_Date = startTime;
        this.salary = salary;
        positions=new ArrayList<>();
    }

    public String getLicense(){
        return null;
    }

    public String setLicense(String license){
        return "The worker is not a driver";
    }

    public String getName() {
        return name;
    }

    public ModelWorker getModel(){return new ModelWorker(this);}

    public Date getStart_Date() {
        return start_Date;
    }

    public String getId() {
        return id;
    }

    public double getSalary() {
        return salary;
    }

    public List<String> getPositions() {
        return positions;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String addPosition(String pos)
    {
        String position=pos.toLowerCase();
        if(!positions.contains(position))
            positions.add(position);
        return null;
    }

    public String removePosition(String pos)
    {
        if(!positions.remove(pos))
            return "The position is not available for this worker";
        return null;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return id.equals(worker.id);
    }

}
