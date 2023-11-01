package gui.util;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import db.DB;

public class Reports {
    public static void openReport(String path){
        try {
            Connection conn = DB.getConnection();

            JasperReport jr = JasperCompileManager.compileReport(path);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            JasperViewer.viewReport(jp, false);

            conn.close();
        } catch (JRException | SQLException e) {
            Alerts.showAlert("Erro", "Algo deu errado!", e.getMessage(), AlertType.ERROR);
        }
    }
}
