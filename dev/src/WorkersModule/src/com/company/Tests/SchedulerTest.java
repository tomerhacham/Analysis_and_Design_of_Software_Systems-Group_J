package com.company.Tests;

import com.company.BussinesLayer.Roster;
import com.company.BussinesLayer.Scheduler;
import com.company.BussinesLayer.Worker;
import com.company.PresentationLayer.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {
    private  Date startDate= Main.parseDate("02/04/2020");
    private  Date dateOfShift= new Date();
    private  Date invalidShift= Main.parseDate("01/04/2020");
    private static final boolean morning=true;
    private static final boolean night=false;
    private Worker worker1;
    private  Worker worker2;
    private Scheduler scheduler;
    @BeforeEach
    private void init()
    {
        worker1=new Worker("Gil","1",startDate,16);
        worker2=new Worker("Sharon","2",startDate,15);
        scheduler =new Scheduler();
        Roster.getInstance().getWorkers().add(worker1);
        Roster.getInstance().getWorkers().add(worker2);
        scheduler.addAvailableWorker(dateOfShift,morning,"1");

    }

    @Test
    public void addAvailableWorker(){

        String output=scheduler.addAvailableWorker(null,morning,"1");
        assertTrue("Invalid date".equals(output));
        output=scheduler.addAvailableWorker(invalidShift,morning,"1");
        assertTrue( "Can not add worker before his start date of working".equals(output));
         output=scheduler.addAvailableWorker(dateOfShift,morning,"1");
        Worker temp=scheduler.getAvailableWorkers().get(dateOfShift).getKey().get(0);
        assertTrue(scheduler.getAvailableWorkers().get(dateOfShift).getKey().contains(worker1));

    }
    @Test
    public void removeAvailableWorker(){
        String output=scheduler.removeAvailableWorker(dateOfShift,morning,"2");
        assertTrue("The worker is not available for this shift".equals(output));
        scheduler.removeAvailableWorker(dateOfShift,morning,"1");
        boolean found=scheduler.getAvailableWorkers().get(dateOfShift).getKey().contains(worker1);
        assertFalse(found);
    }
    @Test
    public void createShift()
    {
        worker1.addPosition("manager");
        String output=scheduler.createShift(dateOfShift,night);
        assertTrue("No Available workers were marked for this shift".equals(output));
        output=scheduler.createShift(dateOfShift,morning);
        scheduler.addWorkerToPositionInShift("manager","1");
        String out=scheduler.submitShift();
        assertFalse(scheduler.findShift(dateOfShift,morning)==null);
    }
    @AfterEach
     private void tearDown()
    {
        Roster.getInstance().getWorkers().clear();
        scheduler.getAvailableWorkers().clear();
        scheduler.getSchedule().clear();
    }




}