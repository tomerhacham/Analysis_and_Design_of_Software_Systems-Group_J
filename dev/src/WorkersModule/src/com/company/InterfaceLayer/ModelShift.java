package com.company.InterfaceLayer;

import com.company.BussinesLayer.EmptyShift;
import com.company.BussinesLayer.FixedSizeList;
import com.company.BussinesLayer.Shift;
import com.company.BussinesLayer.Worker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ModelShift {
    public HashMap<String, FixedSizeList<ModelWorker>> occupation;
    public List<ModelWorker> availableWorkers;
    public Date date;
    public String partOfday;

    public ModelShift(EmptyShift es) {
        occupation = new HashMap<>();
        availableWorkers = new ArrayList<>();
        date=es.getDate();
        if(es.getTimeOfDay()==true)
            partOfday="Empty morning shift";
        else
            partOfday="Empty night shift";
    }

    public ModelShift(Shift shift)
    {
        if(shift!=null) {
            occupation = new HashMap<>();
            HashMap<String, FixedSizeList<Worker>> businessOccupation = shift.getOccupation();
            for (String pos : businessOccupation.keySet()) {
                FixedSizeList original=businessOccupation.get(pos);
                FixedSizeList<ModelWorker> cloned = cloneModelWorkers(original,original.capacity());
                occupation.put(pos, cloned);
            }
            availableWorkers = cloneModelWorkers(shift.getAvailableWorkers(),shift.getAvailableWorkers().size());
            this.date=shift.getDate();
            if(shift.getTimeOfDay())
                partOfday="Morning Shift";
            else
                partOfday="Night Shift";

        }
        else {
            occupation = new HashMap<>();
            availableWorkers = new ArrayList<>();
            date=new Date(0);
            partOfday="Empty Shift";
        }

    }

    private FixedSizeList<ModelWorker> cloneModelWorkers(List<Worker> workers,int cap) {
        FixedSizeList<ModelWorker> cloned=new FixedSizeList<>(cap);
        for(Worker w:workers)
        {
            cloned.add(new ModelWorker(w));
        }
        return cloned;
    }


    @Override
    public String toString() {
        return fullView();
    }

    public String minimizedView() {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String DateString = myFormat.format(date);
        return DateString+"\t"+partOfday+'\n'+
                printOccupation();
    }

    private String printOccupation() {
        String output="Positions:\n";
        for(String pos:occupation.keySet())
        {
            output+='\t'+pos+"- ["+occupation.get(pos).size()+"/"+occupation.get(pos).capacity()+"]\t";
            for(ModelWorker mw:occupation.get(pos))
                output+=mw.name+',';
            output+='\n';
        }
        return output;
    }

    public String fullView()
    {
        String output=minimizedView();
        output+="Available Workers:\n";
        for(ModelWorker mw:availableWorkers)
        {
            output+='\t'+mw.name+": ";
            for(String pos:mw.positions)
                output+=pos+',';
            output+='\n';
        }
        return output;
    }
}
