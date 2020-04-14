package com.company.BussinesLayer;

import java.util.Date;
import java.util.List;

public class EmptyShift extends Shift {
    public EmptyShift( Date date, boolean timeOfday) {
        super(null, date, timeOfday);
    }
}
