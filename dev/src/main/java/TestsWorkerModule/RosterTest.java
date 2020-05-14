/*package com.company.Tests;

import BusinessLayer.Workers.Roster;
import BusinessLayer.Workers.Worker;
import PresentationLayer.Workers.IOWorkers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RosterTest {
    private Roster roster =Roster.getInstance();
    private List<String> positions=new ArrayList<>();
    private Date startDate= IOWorkers.parseDate("01/04/2020");


    @Test
    public void testFailureAddingWorker()//this test assert failure when adding a worker with negative salary

    {
        positions.add("manger");
        positions.add("cashier");
        String output=roster.addWorker("Gil",-16,startDate,positions);
        assertTrue("Illegal salary".equals(output));
        boolean found=false;
        for(Worker w:roster.getWorkers())
        {
            if(w.getName()=="Gil")
                found=true;
        }
        assertFalse(found);
    }
    @Test
    public void testAddWorker(){//this test checks the existence of a worker after adding him
        addWorker();
        boolean found=false;
        for(Worker w:roster.getWorkers())
        {
            if(w.getName()=="Gil")
                found=true;
        }
        assertTrue(found);
    }
    @Test
    public void testEditSalary(){
        Worker w=new Worker("Gil","1",startDate,16);
        roster.getWorkers().add(w);
        roster.editSalary(17,"1");
        assertEquals(17,w.getSalary());
    }

    private void cleanRoster() {
        for(Worker w:roster.getWorkers())
        {
            roster.getWorkers().remove(w);
        }
    }

    private void addWorker() {
        positions.add("manger");
        positions.add("cashier");
        roster.addWorker("Gil",16,startDate,positions);
    }
}*/