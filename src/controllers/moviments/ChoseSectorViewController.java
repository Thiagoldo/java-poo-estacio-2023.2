package controllers.moviments;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controllers.MainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import models.dao.DaoFactory;
import models.entities.Sector;

public class ChoseSectorViewController implements Initializable {
    
    @FXML
    private ComboBox<Sector> cBoxSetor;

    private Sector selectedSector;

    public Sector getSelectedSector() {
        return selectedSector;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Sector> setores = DaoFactory.createSectorDAO().findAll();
        ObservableList<Sector> obsSetores = FXCollections.observableArrayList(setores);
        cBoxSetor.setItems(obsSetores);
    }

    public void onBtnContinuarAction(){
        selectedSector = cBoxSetor.getSelectionModel().getSelectedItem();
        BySectorViewController.setSetor(selectedSector);
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/MovimentaBensView.fxml")));
    }

    public void onBtnCancelarAction(){
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/HomeView.fxml")));
    }
}
