package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.App;
import db.DB;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class HomeViewController implements Initializable {

    @FXML
    private MenuItem menuItemBens;
    @FXML
    private MenuItem menuItemCategorias;
    @FXML
    private MenuItem menuItemSetores;
    @FXML
    private MenuItem menuItemUsuarios;
    @FXML
    private MenuItem menuItemMovimentBySector;
    @FXML
    private MenuItem menuItemReportAssetsActiveds;
    @FXML
    private Text textUser;

    @FXML
    public void onMenuItemUsuariosAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ListUsuariosView.fxml")));
    }

    @FXML
    public void onMenuItemCategoriasAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ListCategoriasView.fxml")));
    }

    @FXML
    public void onMenuItemSetoresAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ListSetoresView.fxml")));
    }

    @FXML
    public void onMenuItemBensAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ListAtivosView.fxml")));
    }

    @FXML
    public void onMenuItemMovimentBySector() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ChoseSectorView.fxml")));
    }

    @FXML
    public void onMenuItemReportAssetsActiveds() {
        try {
            Connection conn = DB.getConnection();

            String reportPath = "src\\reports\\Report_Bens_Ativos.jrxml";
            JasperReport jr = JasperCompileManager.compileReport(reportPath);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            JasperViewer.viewReport(jp, false);

            conn.close();
        } catch (JRException | SQLException e) {
            Alerts.showAlert("Erro", "Algo deu errado!", e.getMessage(), AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textUser.setText(App.getAUTHENTICATED_USER().getNome());

        //TODO VIEWS DA HOME
        //? ViewBensAtivos
        //? ViewValorTotalBensAtivos
        //? ViewUltimoBemCadastrado
    }
}
