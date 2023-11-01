package controllers.sectors;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controllers.MainViewController;
import controllers.helpers.SetoresHelper;
import gui.util.Alerts;
import gui.util.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.dao.DaoFactory;
import models.dao.SectorDAO;
import models.dao.UserDAO;
import models.entities.Sector;
import models.entities.User;

public class EditSetoresViewController implements Initializable {

    @FXML
    private TextField txtDescricao;

    public TextField getTxtDescricao() {
        return txtDescricao;
    }

    @FXML
    private ComboBox<User> cBoxResponsavel;

    public ComboBox<User> getcBoxResponsavel() {
        return cBoxResponsavel;
    }

    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnSair;

    private SectorDAO sectorDAO;

    private static Sector setor;

    private SetoresHelper helper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helper = new SetoresHelper(this);

        UserDAO userDAO = DaoFactory.createUserDAO();
        List<User> users = userDAO.findAll();

        ObservableList<User> obsUsers = FXCollections.observableArrayList(users);
        cBoxResponsavel.setItems(obsUsers);
        cBoxResponsavel.getSelectionModel().selectFirst();

        Constraints.setTextFieldMaxLength(txtDescricao, 100);

        if (setor != null) {
            helper.setSetor(setor);
        }
    }

    public void onBtnSalvarAction() {
        if (isComplete()) {
            Sector viewSector = helper.getSetor();
            sectorDAO = DaoFactory.createSectorDAO();
            if (setor == null) {
                sectorDAO.insert(viewSector);
            } else {
                viewSector.setId(setor.getId());
                sectorDAO.update(viewSector);
            }

            voltar();
        } else {
            Alerts.showAlert("Atenção", "", "Existem campos necessários não preenchidos.", AlertType.WARNING);
        }

    }

    public void onBtnSairAction() {
        voltar();
    }

    public static void setSetor(Sector s) {
        setor = s;
    }

    private void voltar() {
        helper.limpar();
        setor = null;
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/ListSetoresView.fxml")));
    }

    private boolean isComplete() {
        boolean checkNome = txtDescricao.getText() != "";
        boolean checkStatus = !cBoxResponsavel.getSelectionModel().isEmpty();

        return (checkNome && checkStatus);
    }

}
