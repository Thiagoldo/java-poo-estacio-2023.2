package controllers.moviments;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import controllers.MainViewController;
import gui.util.Reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import models.dao.DaoFactory;
import models.entities.Sector;

public class ChoseSectorViewController implements Initializable {

    @FXML
    private ComboBox<Sector> cBoxSetor;

    @FXML
    private Button btnCancelar;

    private Sector selectedSector;

    public static Integer option;

    public Sector getSelectedSector() {
        return selectedSector;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Sector> setores = DaoFactory.createSectorDAO().findAll();
        ObservableList<Sector> obsSetores = FXCollections.observableArrayList(setores);
        cBoxSetor.setItems(obsSetores);
    }

    public void onBtnContinuarAction() {
        selectedSector = cBoxSetor.getSelectionModel().getSelectedItem();
        if (option == 1) {
            BySectorViewController.setSetor(selectedSector);
            MainViewController
                    .loadView(new FXMLLoader(getClass().getResource("../../gui/views/MovimentaBensView.fxml")));
        } else if (option == 2) {
            Map<String, Object> params = new HashMap<>();
            params.put("Setor", selectedSector.getId());
            onBtnCancelarAction();
            Reports.openReport("src\\gui\\reports\\Report_Bens_Por_Setor.jrxml", params);
        }
    }

    public void onBtnCancelarAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/HomeView.fxml")));
    }

}
