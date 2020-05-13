package BusinessLayer.Workers;
import java.util.Date;

public class EmptyShift extends Shift {
    public EmptyShift( Date date, boolean timeOfday) {
        super(null, date, timeOfday);
    }
}
