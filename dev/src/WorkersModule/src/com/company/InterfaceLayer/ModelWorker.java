package com.company.InterfaceLayer;

import com.company.BussinesLayer.Worker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelWorker {
    public String name;
    public String id;
    public Date start_Date;
    public double salary;
    public List<String> positions;

    public ModelWorker(Worker worker) {
        name=worker.getName();
        id=worker.getId();
        start_Date=worker.getStart_Date();
        salary=worker.getSalary();
        positions=new ArrayList<>();
        positions.addAll(worker.getPositions());
    }

    @Override
    public String toString() {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String DateString = myFormat.format(start_Date);

        return
                 name + '\t'+
                " id='" + id + '\t' +
                "start Date=" + DateString+'\t'+
                "salary=" + salary +'\t'+
                "positions=" + positions;
    }
}
