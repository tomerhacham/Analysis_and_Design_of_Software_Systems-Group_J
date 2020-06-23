package bussines_layer.employees_module;

import bussines_layer.employees_module.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {
    private  Date startDate= parseDate("02/04/2020");
    private  Date dateOfShift= new Date();
    private  Date invalidShift= parseDate("01/04/2020");
    private static final boolean morning=true;
    private static final boolean night=false;
    private Worker worker1;
    private  Worker worker2;
    private Scheduler scheduler;
    private Roster roster;
    private Integer branch_id;

    //shifts test fields
    private Worker worker3;
    private Worker worker4;
    private Shift shift;
    List<Worker> availables;

    //roster tests fields
    private List<String> positions=new ArrayList<>();

    @BeforeEach
    private void init()
    {
        branch_id=-1;
        worker1=new Worker("Gil","1",branch_id,startDate,16);
        worker2=new Worker("Sharon","2",branch_id,startDate,15);
        scheduler =new Scheduler();
        roster= new Roster();
        roster.getWorkers(branch_id).add(worker1);
        roster.getWorkers(branch_id).add(worker2);
        scheduler.addAvailableWorker(dateOfShift,morning,"1",branch_id);


        //shift test
        List<Worker> availables=new ArrayList<>();
        worker3 =new Worker("Gil","1",branch_id,startDate,16);
        worker4 =new Worker("Sharon","2",branch_id,startDate,15);
        worker3.addPosition("manager");
        availables.add(worker3);
        availables.add(worker4);
        shift=new Shift(dateOfShift,morning,"0000-0000-0001",branch_id);
    }

    @Test
    public void addAvailableWorker(){

        String output=scheduler.addAvailableWorker(null,morning,"1",branch_id);
        assertTrue("Invalid date".equals(output));
        output=scheduler.addAvailableWorker(invalidShift,morning,"1",branch_id);
        assertTrue( "Can not add worker before his start date of working".equals(output));
        output=scheduler.addAvailableWorker(dateOfShift,morning,"1",branch_id);
        Worker temp=scheduler.getAvailableWorkers().get(dateOfShift).getMorning().get(0);
        assertTrue(scheduler.getAvailableWorkers().get(dateOfShift).getMorning().contains(worker1));

    }
    @Test
    public void removeAvailableWorker(){
        String output=scheduler.removeAvailableWorker(dateOfShift,morning,"2",branch_id);
        assertTrue("The worker is not available for this shift".equals(output));
        scheduler.removeAvailableWorker(dateOfShift,morning,"1",branch_id);
        boolean found=scheduler.getAvailableWorkers().get(dateOfShift).getMorning().contains(worker1);
        assertFalse(found);
    }
    @Test
    public void createShift()
    {
        worker1.addPosition("manager");
        String output=scheduler.createShift(dateOfShift,night,branch_id);
        assertTrue("No Available workers were marked for this shift".equals(output));
        output=scheduler.createShift(dateOfShift,morning,branch_id);
        scheduler.addWorkerToPositionInShift("manager","1",branch_id);
        String out=scheduler.submitShift(branch_id);
        assertFalse(scheduler.findShift(dateOfShift,morning,branch_id)==null);
    }
    @AfterEach
    private void tearDown() {
        roster.removeExistingWorkers();
        for (Date date:scheduler.getAvailableWorkers().keySet()){
            Pair<LazyList<Worker>, LazyList<Worker>> pair= scheduler.getAvailableWorkers().get(date) ;{
                for (Worker w :pair.getMorning())
                {
                    scheduler.removeAvailableWorker(date,morning,w.getId(),branch_id);
                }
                for (Worker w :pair.getNight())
                {
                    scheduler.removeAvailableWorker(date,night,w.getId(),branch_id);
                }
            }
        }
        scheduler.clearAllSchedule(branch_id);
    }



    private static Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date=null;
        try {
            //Parsing the String
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {

        }
        return date;
    }



    //shifts tests


    @Test
    void addPosition() {
        String output=shift.addPosition("",1);
        assertTrue("Invalid position".equals(output));
        shift.addPosition("cashier",1);
        boolean found=shift.getOccupation().containsKey("cashier");
        assertTrue(found);
        output=shift.addPosition("cashier",1);
        assertTrue("The position already exists".equals(output));

    }

    @Test
    void removePosition() {
        String output=shift.removePosition("cashier");
        assertTrue("The shift does not contain this position".equals(output));
        boolean before= shift.getOccupation().containsKey("manager");
        shift.removePosition("manager");
        boolean after= shift.getOccupation().containsKey("manager");
        assertTrue(before&!after);
    }

    @Test
    void addWorkerToPosition() {
        String output=shift.addWorkerToPosition("cashier","1",availables);
        assertTrue("The position does not exist".equals(output));
        output=shift.addWorkerToPosition("manager","3",availables);
        assertTrue("The worker is not available".equals(output));
        output=shift.addWorkerToPosition("manager","1",availables);
        boolean found=shift.getOccupation().get("manager").contains(worker3);
        assertTrue(found);
        output=shift.addWorkerToPosition("manager","2",availables);
        assertTrue("The position is Full".equals(output));
    }

    @Test
    void removeWorkerFromPosition() {
        String output=shift.removePosition("cashier");
        assertTrue("The shift does not contain this position".equals(output));
        shift.addWorkerToPosition("manager","1",availables);
        shift.removeWorkerFromPosition("manager","1",availables);
        boolean found =shift.getOccupation().get("manager").contains("1");
        assertFalse(found);

    }


    //roseter tests

    @Test
    public void testFailureAddingWorker()//this test assert failure when adding a worker with negative salary
    {
        positions.add("manger");
        positions.add("cashier");
        String output=roster.addWorker("Dor",-16,startDate,branch_id,positions);
        assertTrue("Illegal salary".equals(output));
        boolean found=false;
        for(Worker w:roster.getWorkers(branch_id))
        {
            if(w.getName()=="Gil")
                found=true;
        }
        assertFalse(found);
    }
    @Test
    public void testAddWorker(){//this test checks the existence of a worker after adding him
        positions.add("manger");
        positions.add("cashier");
        String output=roster.addWorker("Dor",16,startDate,branch_id,positions);
        assertTrue("Illegal salary".equals(output));
        boolean found=false;
        for(Worker w:roster.getWorkers(branch_id))
        {
            if(w.getName()=="Gil")
                found=true;
        }
        assertTrue(found);
    }
    @Test
    public void testEditSalary(){// this test checks if salary changes
        Worker w=new Worker("Dor","1",branch_id,startDate,16);
        roster.getWorkers(branch_id).add(w);
        roster.editSalary(17,"1");
        assertEquals(17,w.getSalary());
        roster.removeWorker(w.getId());
    }


}


