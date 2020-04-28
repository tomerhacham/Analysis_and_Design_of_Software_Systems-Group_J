package com.company.PresentationLayer;


import com.company.BussinesLayer.Roster;
import com.company.InterfaceLayer.ModelShift;
import com.company.InterfaceLayer.ModelWorker;
import com.company.InterfaceLayer.RosterController;
import com.company.InterfaceLayer.ScheduleController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    private static RosterController rc=new RosterController();
    private static ScheduleController sc=new ScheduleController();
    private static MyScanner scanner=new MyScanner(System.in);
    private static final boolean morning=true;
    private static final boolean night=false;
    public static void main(String[] args) throws ParseException {
        if(args.length>0&&args[0].equals("-i"))
            init();
        mainLoop();
    }

    private static void mainLoop() {
        boolean terminate=false;
        while(!terminate)
        {
            System.out.println("Choose an option:");
            System.out.println("1.Manage workers");
            System.out.println("2.Mange schedule");
            System.out.println("3.Quit");
            int opt=scanner.nextInt();
            if(opt==1)
            {
                manageWorkers();
            }
            else if(opt==2)
            {
                manageSchedule();
            }
            else if(opt==3)
            {
                terminate=true;
            }
            else{
                System.out.println("Please select correct option number");
            }


        }
    }

    private static void manageSchedule() {
        boolean goBack=false;
        while(!goBack) {
            System.out.println("Choose an option:");
            System.out.println("1.Manage availability");
            System.out.println("2.Manage shifts");
            System.out.println("3.Return to the previous menu");
            int opt = scanner.nextInt();
            switch (opt) {
                case 1:
                    manageAvailability();
                    break;
                case 2:
                    manageShifts();
                    break;
                case 3 :
                    goBack=true;
                    break;
            }
        }
    }

    private static void manageShifts() {
        boolean goBack=false;
        while(!goBack) {
            System.out.println("Choose an option:");
            System.out.println("1.Display weekly schedule");
            System.out.println("2.Create a shift");
            System.out.println("3.Edit a shift");
            System.out.println("4.Remove shift");
            System.out.println("5.Return to the previous menu");
            int opt=scanner.nextInt();
            switch(opt){
                case(1):
                    displayWeeklySchedule();
                    break;
                case(2):
                    constructShift(true);
                    break;
                case(3):
                    constructShift(false);
                    break;
                case(4):
                    removeShift();
                    break;
                case(5):
                    goBack=true;
                    break;
            }
        }
    }

    private static void removeShift() {
        Date date=getDateFromUser();
        boolean timeOfDay=getTimeOfDayFromUser();
        String output=sc.removeShift(date,timeOfDay);
        if(output!=null)
            System.out.println(output);
    }

    private static void constructShift(boolean creation) {
        String output=null;
        Date date=getDateFromUser();
        boolean isMorningShift=getTimeOfDayFromUser();
        if(creation) {
            output=sc.createShift(date,isMorningShift);
        }
        else{
            output=sc.editShift(date,isMorningShift);
        }
        if(output!=null)
            System.out.println(output);
        else
        {
            boolean goBack=false;
            while(!goBack)
            {
                System.out.println(sc.getCurrentEditedShift().fullView());
                System.out.println("Choose an option:");
                System.out.println("1.Add position");
                System.out.println("2.Remove position");
                System.out.println("3.Add worker");
                System.out.println("4.Remove worker");
                System.out.println("5.Submit shift");
                System.out.println("6.Cancel progress");
                int opt=scanner.nextInt();
                switch(opt){
                    case(1):
                        addPositionToShift();
                        break;
                    case(2):
                        removePositionFromShift();
                        break;
                    case(3):
                        addWorkerToShift();
                        break;
                    case(4):
                        removeWorkerFromShift();
                        break;
                    case(5):
                    {
                        output=sc.submitShift();
                        if(output!=null)
                            System.out.println(output);
                        else
                            goBack=true;
                        break;
                    }

                    case(6): {
                            sc.cancelShift();
                            goBack=true;
                        break;
                    }


                }
            }

        }
    }

    private static void removeWorkerFromShift() {
        System.out.println("Enter position from which you want to remove a worker:");
        String pos=scanner.nextLine();
        if(!sc.getCurrentEditedShift().occupation.containsKey(pos))
            System.out.println("The position was not found");
        else {
            List<ModelWorker> workersInpos=sc.getCurrentEditedShift().occupation.get(pos);
            if(workersInpos.size()>0) {
                System.out.println("Eligible workers:");
                int i = 0;
                for (ModelWorker mw : workersInpos) {
                    System.out.println(i + "." + mw.name);
                    i++;
                }
                int select=-1;
                while(select<0|select>=workersInpos.size()) {
                    System.out.println("Enter number of worker to be removed");
                    select = scanner.nextInt();
                }
                String output=sc.removeWorkerToPositionInShift(pos,workersInpos.get(select).id);
                if(output!=null)
                    System.out.println(output);
            }
            System.out.println("There are no workers assigned to this position");
        }
    }

    private static void addWorkerToShift() {
        System.out.println("Enter position to be occupied:");
        String pos=scanner.nextLine();
        int i=0;
        Stream<ModelWorker> smw= sc.getCurrentEditedShift().availableWorkers.stream().filter((mw)->mw.positions.contains(pos));
        ModelWorker[] relevantWorkers=smw.toArray(size-> new ModelWorker[size]);
        if(relevantWorkers.length>0) {
            System.out.println("Available workers for this position:");
            for (ModelWorker mw : relevantWorkers) {
                System.out.println(i + "." + mw.name);
                i++;
            }
            int num = -1;
            while(num<0|num>=relevantWorkers.length)
            {
                System.out.println("Choose number of worker:");
                num=scanner.nextInt();
            }
            String output=sc.addWorkerToPositionInShift(pos,relevantWorkers[num].id);
            if(output!=null)
                System.out.println(output);
        }
        else
            System.out.println("No Available workers for this position");
    }

    private static void removePositionFromShift() {
        String output=null;
        System.out.println("Enter a position:");
        String pos=scanner.nextLine();
        output=sc.removePositionFromShift(pos);
        if(output!=null)
            System.out.println(output);
    }

    private static void addPositionToShift() {
        String output=null;
        System.out.println("Enter a position:");
        String pos=scanner.nextLine();
        System.out.println("Enter quantity of workers required fot this position in this shift:");
        int quantity=scanner.nextInt();
        output=sc.addPositionToShift(pos,quantity);
        if(output!=null)
            System.out.println(output);
    }

    private static void displayWeeklySchedule()
    {
        Date date=getDateFromUser();
        List<ModelShift>mw=sc.getWeeklyShifts(date);
        if(mw==null)
            System.out.println("Invalid Date or no scheduled shifts for given date");
        else {
            SimpleDateFormat myFormat = new SimpleDateFormat("EE MMMM dd, yyyy",Locale.US);
            String DateString = myFormat.format(mw.get(0).date);
            System.out.println("Displaying shifts for week of " + DateString+ ":");
            for (ModelShift ms : mw) {
                System.out.println(ms.minimizedView());
            }
        }
    }
    private static void manageAvailability() {
        String id=selectWorker();
        if(id!=null)
        {
            boolean goBack=false;
            while(!goBack)
            {
                System.out.println("Choose an option:");//TODO:Add display workers for shift option
                System.out.println("1.Mark worker available for shift");
                System.out.println("2.Unmark worker available for shift");
                System.out.println("3.Finish");
                int opt=scanner.nextInt();
                switch (opt){
                    case(1):
                        MarkWorkerAvailbleForShift(id);
                        break;
                    case(2):
                        UnmarkWorkerAvailbleForShift(id);
                        break;
                    case(3):
                        goBack=true;
                        break;

                }
            }
        }

    }

    private static void UnmarkWorkerAvailbleForShift(String id) {
        System.out.println("Enter shift date");
        Date date=getDateFromUser();
        boolean timeOfday = getTimeOfDayFromUser();
        String output=sc.removeAvailableWorker(date,timeOfday,id);
        if(output!=null)
            System.out.println(output);

    }

    private static void MarkWorkerAvailbleForShift(String id) {
        System.out.println("Enter shift date");
        Date date=getDateFromUser();
        boolean timeOfday = getTimeOfDayFromUser();
        sc.addAvailableWorker(date,timeOfday,id);
    }

    private static boolean getTimeOfDayFromUser() {
        int opt = 0;
        while (opt < 1 | opt > 2) {
            System.out.println("Choose shift:");
            System.out.println("1.Morning Shift");
            System.out.println("2.Night shift");
            opt = scanner.nextInt();
        }
        return opt == 1;
    }

    private static void manageWorkers() {
        boolean goBack=false;
        while(!goBack)
        {
            System.out.println("Choose an option:");
            System.out.println("1.Display workers");
            System.out.println("2.Add worker");
            System.out.println("3.Remove worker");
            System.out.println("4.Edit worker");
            System.out.println("5.Return to the previous menu");
            int opt=scanner.nextInt();
            switch(opt) {
                case(1):
                    displayWorkers();
                    break;
                case (2):
                    addWorker();
                    break;
                case(3):
                    removeWorker();
                    break;

                case(4):
                    editWorker();
                    break;
                case(5):
                    goBack=true;
                    break;

            }
        }

    }

    private static void editWorker() {
        String id=selectWorker();
        if(id!=null)
        {
            boolean goBack=false;
            while(!goBack)
            {
                System.out.println(rc.displaySingleWorker(id));
                System.out.println("Choose an option:");
                System.out.println("1.Edit worker's name");
                System.out.println("2.Add position to worker");
                System.out.println("3.Remove position from worker");
                System.out.println("4.Change worker's salary");
                System.out.println("5.Finish editing");
                int opt=scanner.nextInt();
                switch(opt){
                    case(1):
                        editWorkerName(id);
                        break;
                    case(2):
                        addPositionToWorker(id);
                        break;
                    case(3):
                        removePosition(id);
                        break;
                    case(4):
                        changeWorkerSalary(id);
                        break;
                    case(5):
                        goBack=true;
                        break;
                }

            }
        }
    }

    private static void changeWorkerSalary(String id) {
        System.out.println("Enter new Salary:");
        double newSalary=scanner.nextDouble();
        String output=rc.editSalry(newSalary,id);
        if(output!=null)
            System.out.println(output);
    }

    private static void removePosition(String id) {
        System.out.println("Enter position to remove:");
        String newPosition=scanner.nextLine();
        String output=rc.removePosition(newPosition,id);
        if(output!=null)
            System.out.println(output);
    }

    private static void addPositionToWorker(String id) {
        System.out.println("Enter new position");
        String newPosition=scanner.nextLine();
        String output=rc.addPosition(newPosition,id);
        if(output!=null)
            System.out.println(output);
    }


    private static void editWorkerName(String id) {
        System.out.println("Enter new name");
        String newName=scanner.nextLine();
        String output=rc.editName(newName,id);
        if(output!=null)
            System.out.println(output);
    }

    private static void displayWorkers() {
        int i=0;
        for(ModelWorker mw:rc.displayWorkers())
        {
            System.out.println(i+":"+mw.toString());
            i++;
        }
    }

    private static void removeWorker() {
        String id=selectWorker();
        String output=null;
        if(id!=null)
            output=sc.removeWorkerFromRoster(id);
        if(output!=null)
            System.out.println(output);
    }

    private static String selectWorker() {
        displayWorkers();
        List<ModelWorker> mw=rc.displayWorkers();
        System.out.println("Please enter the number of the selected worker:");
        int selected=scanner.nextInt();
        if(selected<0|selected>=mw.size())
            System.out.println("Invalid worker's number inserted. please try again!");
        else
        {
            return mw.get(selected).id;
        }
        return null;
    }

    private static void addWorker() {
        System.out.println("Please enter name:");
        String name=scanner.nextLine();
        System.out.println("Please enter salary:");
        double salary=scanner.nextDouble();
        System.out.println("Please enter positions separated by comma:");
        String pos=scanner.nextLine();
        List<String> positions= Arrays.asList(pos.split(","));
        Date date = getDateFromUser();
        String output=rc.addWorker(name,salary,date,positions);
        if(output!=null)
            System.out.println(output);
    }

    private static Date getDateFromUser() {
        Date date=null;
        while(date==null) {
            System.out.println("Please enter Date in format of:dd/MM/yyyy");
            String dateStr = scanner.next();
            date=parseDate(dateStr);
        }
        return date;
    }

    public static Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date2=null;
        try {
            //Parsing the String
        date2 = dateFormat.parse(date);
    } catch (ParseException e) {

    }
        return date2;
    }
    private static void init()
    {
        List<String>positions1=new ArrayList<>();
        positions1.add("manager");
        positions1.add("cashier");
        List<String>positions2=new ArrayList<>();
        positions2.add("cleaner");
        Date startDate1=parseDate("11/04/2020");
        Date startDate2=parseDate("12/04/2020");
        rc.addWorker("Gil",16,startDate1,positions1);
        rc.addWorker("Sharon",15.9,startDate2,positions1);
        rc.addWorker("Moshe",10,startDate1,positions2);
        rc.addWorker("Dani",100,startDate2,positions2);
        positions2.add("security guard");
        rc.addWorker("Avi",100,startDate1,positions2);
        List<ModelWorker> workers=rc.displayWorkers();
        Date shiftDate1=parseDate("20/04/2020");
        Date shiftDate2=parseDate("21/04/2020");
        sc.addAvailableWorker(shiftDate1,morning,workers.get(0).id);
        sc.addAvailableWorker(shiftDate1,night,workers.get(1).id);
        sc.addAvailableWorker(shiftDate1,morning,workers.get(2).id);
        sc.addAvailableWorker(shiftDate2,morning,workers.get(0).id);
        sc.addAvailableWorker(shiftDate2,morning,workers.get(1).id);
        sc.addAvailableWorker(shiftDate2,morning,workers.get(2).id);
        sc.addAvailableWorker(shiftDate2,morning,workers.get(3).id);
        sc.addAvailableWorker(shiftDate2,morning,workers.get(4).id);
        sc.createShift(shiftDate2,morning);
        sc.addPositionToShift("cashier",1);
        sc.addPositionToShift("cleaner",2);
        sc.addPositionToShift("security guard",1);
        sc.addWorkerToPositionInShift("manager",workers.get(0).id);
        sc.addWorkerToPositionInShift("cashier",workers.get(1).id);
        sc.addWorkerToPositionInShift("cleaner",workers.get(2).id);
        sc.addWorkerToPositionInShift("cleaner",workers.get(3).id);
        sc.addWorkerToPositionInShift("security guard",workers.get(4).id);
        sc.submitShift();

















    }
}
