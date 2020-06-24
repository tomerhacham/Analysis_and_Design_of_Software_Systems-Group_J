import bussines_layer.employees_module.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}


