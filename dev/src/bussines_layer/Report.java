package bussines_layer;

import java.util.Date;
import java.util.List;

enum ReportType
{OutOfStock,InStock,ExpiredDamaged;}

public class Report {
    //fields:
    private Enum<ReportType> type;
    final private Date creation_date;
    private List<SpecificProduct> products;

    public Report() {
        creation_date = null;
    }
}
