package com.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Worker {
    private String name;
    private String id;
    private Date Start_Date;
    private double salary;
    List<String> positions;

    public Worker(String name, String id, Date startTime, double salary) {
        this.name = name;
        this.id = id;
        Start_Date = startTime;
        this.salary = salary;
        positions=new ArrayList<>();
    }

    public String getName() {
        return name;
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
        positions.add(pos);
    }
}
