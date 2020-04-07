package InterfaceLayer;

public class initialize {
    private static FacadeController facadeController = FacadeController.getInstance();

    public initialize()
    {
        facadeController.createDriver("Eran", "C1");
        facadeController.createDriver("Omer","C");
        facadeController.createDriver("Noam","C1");
        facadeController.createSite("Beer-Sheva","054-1234567", "Shira",1);
        facadeController.createSite("Ofakim","052-1234567","Einav",1);
        facadeController.createSite("Omer","050-1234567", "Amit",1);
        facadeController.createSite("herzelia","052-8912345","Shachaf",3);
        facadeController.createSite("Tel-Aviv","050-8912345","Mai",3);
        facadeController.createSite("Jerisalem","050-8912345","Eden",4);
        facadeController.createTruck("12-L8","XXX",1000,"C1");
        facadeController.createTruck("17-LD","X23",1050,"C1");
        facadeController.createTruck("J0-38","1X6",700,"c");
    }
}
