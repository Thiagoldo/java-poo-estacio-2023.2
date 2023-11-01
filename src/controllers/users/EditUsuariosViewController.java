package controllers.users;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import controllers.MainViewController;
import controllers.helpers.UsuariosHelper;
import gui.util.Alerts;
import gui.util.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import models.dao.DaoFactory;
import models.dao.UserDAO;
import models.entities.User;
import models.enums.Perfil;
import models.enums.Status;

public class EditUsuariosViewController implements Initializable {

    @FXML
    private TextField txtNome;

    public TextField getTxtNome() {
        return txtNome;
    }

    @FXML
    private TextField txtUsuario;

    public TextField getTxtUsuario() {
        return txtUsuario;
    }

    @FXML
    private TextField txtSenha;

    public TextField getTxtSenha() {
        return txtSenha;
    }

    @FXML
    private ComboBox<Perfil> cBoxPerfil;

    public ComboBox<Perfil> getcBoxPerfil() {
        return cBoxPerfil;
    }

    @FXML
    private ComboBox<Status> cBoxStatus;

    public ComboBox<Status> getcBoxStatus() {
        return cBoxStatus;
    }

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private static User usuario;

    private UsuariosHelper helper;

    private UserDAO userDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        helper = new UsuariosHelper(this);

        ObservableList<Perfil> obsPerfis = FXCollections.observableArrayList(Arrays.asList(Perfil.values()));
        cBoxPerfil.setItems(obsPerfis);
        cBoxPerfil.getSelectionModel().selectFirst();

        ObservableList<Status> obsStatus = FXCollections.observableArrayList(Arrays.asList(Status.values()));
        cBoxStatus.setItems(obsStatus);
        cBoxStatus.getSelectionModel().selectFirst();

        Constraints.setTextFieldMaxLength(txtNome, 100);
        Constraints.setTextFieldMaxLength(txtUsuario, 20);
        Constraints.setTextFieldMaxLength(txtSenha, 20);

        if (usuario != null) {
            helper.setUser(usuario);
        }
    }

    public void onBtnSalvarAction() {
        if (isComplete()) {
            User viewUser = helper.getUser();
            userDAO = DaoFactory.createUserDAO();
            if (usuario == null) {
                userDAO.insert(viewUser);
            } else {
                viewUser.setId(usuario.getId());
                userDAO.update(viewUser);
            }

            voltar();
        } else {
            Alerts.showAlert("Atenção", "", "Existem campos necessários não preenchidos.", AlertType.WARNING);
        }

    }

    public void onBtnCancelarAction() {
        voltar();
    }

    public static void setUsuario(User u) {
        usuario = u;
    }

    private void voltar() {
        helper.limpar();
        usuario = null;
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/ListUsuariosView.fxml")));
    }

    private boolean isComplete() {
        boolean checkNome = txtNome.getText() != "";
        boolean checkUsuario = txtUsuario.getText() != "";
        boolean checkSenha = txtSenha.getText() != "";
        boolean checkPerfil = !cBoxPerfil.getSelectionModel().isEmpty();
        boolean checkStatus = !cBoxStatus.getSelectionModel().isEmpty();

        return (checkNome && checkUsuario && checkSenha && checkPerfil && checkStatus);
    }
}
