package bussines_layer;

import java.util.Date;
import java.util.List;

enum ReportType
{OutOfStock,InStock,ExpiredDamaged;}


public class Report {
    //fields:
    private ReportType type;
    final private Date creation_date;
    private List<GeneralProduct> products;
    String report="";

    public Report(List<GeneralProduct> products,ReportType type) {
        this.type = type;
        this.products = products;
        creation_date = new Date();
        this.report="\n"+type.name().concat("\n");
        arrangeReport();
    }

    public void arrangeReport(){
        for (GeneralProduct generalProduct : products) {
            report=report.concat(generalProduct.report(type));
        }
    }
    @Override
    public String toString(){
        return report;
    }
    public String getReport() {
        return report;
    }
}
