package presentation_layer;

import com.j256.ormlite.logger.LocalLog;
import data_access_layer.Mapper;

public class main {

    public static void main(String[] args) {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        CLController clc = new CLController();
        clc.displayMenu();
    }
}
