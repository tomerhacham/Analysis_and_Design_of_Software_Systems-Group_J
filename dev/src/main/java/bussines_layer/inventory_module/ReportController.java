package bussines_layer.inventory_module;

import bussines_layer.Result;

import java.util.List;

public class ReportController {

    public ReportController() {}

    /**
     * make report object which has String report field that represent the report to be prints
     * @param generalProducts - list of all the general products to make the report on
     * @param type - type of the  {OutOfStock,InStock,ExpiredDamaged}
     * @return - Result object that hold the Report object
     */
    public Result makeReport(List<GeneralProduct> generalProducts, ReportType type){
        if (generalProducts!=null && !generalProducts.isEmpty()){
            for (GeneralProduct product:generalProducts){
                if (product==null){
                    return new Result<Boolean>(false,false,"One of the general product cannot be found");

                }
            }
            Report report = new Report(generalProducts,type);
            return new Result<Report>(true, report,report.getReport());
        }
        else{
            return new Result<Boolean>(false,false,"No general product has been provided to the report");
        }
    }
}
