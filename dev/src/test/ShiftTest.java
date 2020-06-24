import bussines_layer.employees_module.Shift;
import bussines_layer.employees_module.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShiftTest {
    private  Date startDate= parseDate("02/04/2020");
    private  Date dateOfShift= parseDate("10/04/2020");
    private Worker worker1;
    private Worker worker2;
    private Integer branch_id;
    private static final boolean morning=true;
    private static final boolean night=false;
    private Shift shift;
    List<Worker> availables;
    @BeforeEach
    private void init() {
        branch_id=-1;
        List<Worker> availables=new ArrayList<>();
        worker1=new Worker("Gil","1",branch_id,startDate,16);
        worker2=new Worker("Sharon","2",branch_id,startDate,15);
        worker1.addPosition("manager");
        availables.add(worker1);
        availables.add(worker2);
        shift=new Shift(dateOfShift,morning,"0000-0000-0001",branch_id);
    }


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
        boolean found=shift.getOccupation().get("manager").contains(worker1);
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
