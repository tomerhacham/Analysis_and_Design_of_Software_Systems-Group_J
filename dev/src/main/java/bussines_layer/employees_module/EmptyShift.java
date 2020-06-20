package bussines_layer.employees_module;
import java.util.Date;

public class EmptyShift extends Shift {
    public EmptyShift( Date date, boolean timeOfday,Integer branch_id) {
        super(date, timeOfday,null,branch_id);
    }
}