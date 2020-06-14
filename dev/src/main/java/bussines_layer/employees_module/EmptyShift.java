package bussines_layer.employees_module;
import java.util.Date;

public class EmptyShift extends Shift {
    public EmptyShift( Date date, boolean timeOfday) {
        super(date, timeOfday,null);
    }
}
