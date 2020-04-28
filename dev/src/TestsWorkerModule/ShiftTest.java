package com.company.Tests;

import BusinessLayer.Workers.Shift;
import BusinessLayer.Workers.Worker;
import PresentationLayer.Workers.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShiftTest {
private  Date startDate= Main.parseDate("02/04/2020");
private  Date dateOfShift= Main.parseDate("10/04/2020");
private Worker worker1;
private Worker worker2;
private static final boolean morning=true;
private static final boolean night=false;
private Shift shift;
    @BeforeEach
    private void init() {
        List<Worker> availables=new ArrayList<>();
        worker1=new Worker("Gil","1",startDate,16);
        worker2=new Worker("Sharon","2",startDate,15);
        worker1.addPosition("manager");
        availables.add(worker1);
        availables.add(worker2);
        shift=new Shift(availables,dateOfShift,morning);
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
        String output=shift.addWorkerToPosition("cashier","1");
        assertTrue("The position does not exist".equals(output));
        output=shift.addWorkerToPosition("manager","3");
        assertTrue("The worker is not available".equals(output));
        output=shift.addWorkerToPosition("manager","1");
        boolean found=shift.getOccupation().get("manager").contains(worker1);
        assertTrue(found);
        output=shift.addWorkerToPosition("manager","2");
        assertTrue("The position is Full".equals(output));
    }

    @Test
    void removeWorkerFromPosition() {
        String output=shift.removePosition("cashier");
        assertTrue("The shift does not contain this position".equals(output));
        shift.addWorkerToPosition("manager","1");
        shift.removeWorkerFromPosition("manager","1");
        boolean found =shift.getOccupation().get("manager").contains("1");
        assertFalse(found);

    }

}