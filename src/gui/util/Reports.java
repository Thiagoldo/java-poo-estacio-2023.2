package gui.util;

import java.util.Map;

import db.DB;
import javafx.scene.control.Alert.AlertType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class Reports {
    public static void openReport(String path){
        try {
            JasperReport jr = JasperCompileManager.compileReport(path);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, DB.getConnection());
            JasperViewer.viewReport(jp, false);
        } catch (JRException e) {
            Alerts.showAlert("Erro", "Algo deu errado!", e.getMessage(), AlertType.ERROR);
        }
    }

    public static void openReport(String path, Map<String, Object> params){
        try {
            JasperReport jr = JasperCompileManager.compileReport(path);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, DB.getConnection());
            JasperViewer.viewReport(jp, false);
        } catch (JRException e) {
            Alerts.showAlert("Erro", "Algo deu errado!", e.getMessage(), AlertType.ERROR);
        }
    }
}

