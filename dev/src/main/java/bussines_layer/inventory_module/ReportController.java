package bussines_layer.inventory_module;

import bussines_layer.Result;
import bussines_layer.enums.ReportType;

import java.util.List;

public class ReportController {
    private Integer branch_id;
    public ReportController(Integer branch_id) {this.branch_id=branch_id;}

    /**
     * make report object which has String report field that represent the report to be prints
     * @param generalProducts - list of all the general products to make the report on
     * @param type - type of the  {OutOfStock,InStock,ExpiredDamaged}
     * @return - Result object that hold the Report object
     */
    public Result<Report> makeReport(List<GeneralProduct> generalProducts, ReportType type){
        if (generalProducts!=null && !generalProducts.isEmpty()){
            for (GeneralProduct product:generalProducts){
                if (product==null){
                    return new Result<>(false,null,"One of the general product cannot be found");

                }
            }
            Report report = new Report(generalProducts,type);
            return new Result<>(true, report,report.getReport());
        }
        else{
            return new Result<>(false,null,"No general product has been provided to the report");
        }
    }
}
