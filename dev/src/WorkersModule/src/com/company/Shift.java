package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shift {
    HashMap<String, Worker[]> occupation;
    List<Worker> availableWorkers;
    public Shift()
    {
        occupation=new HashMap<>();
        availableWorkers=new ArrayList<>();
        occupation.put("Manager",new Worker[1]);
    }
    public void addPosition(String pos,int quantity)
    {
        occupation.put(pos,new Worker[quantity]);
    }
    public void addWorker()

}
