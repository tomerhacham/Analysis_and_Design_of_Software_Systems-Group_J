import bussines_layer.employees_module.Roster;
import bussines_layer.employees_module.Worker;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RosterTest {
    private Roster roster = new Roster();
    private List<String> positions=new ArrayList<>();
    private Date startDate= parseDate("01/06/2020");
    private Integer branch_id=-1;

    private void init_employees_Roster_test()
    {

    }
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
        String output=roster.addWorker("Dor",-16,startDate,branch_id,positions);

        assertTrue("Illegal salary".equals(output));
        roster.addWorker("Gil",16,startDate,branch_id,positions);
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

    private void workers_tear_down()
    {
        roster.removeExistingWorkers();
    }
}