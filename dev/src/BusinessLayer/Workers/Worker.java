package BusinessLayer.Workers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public String getName() {
        return name;
    }

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
    public void addPosition(String pos)
    {
        if(!positions.contains(pos))
            positions.add(pos);
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
}
