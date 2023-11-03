package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.App;
import db.DB;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.dao.DaoFactory;
import models.dao.UserDAO;
import models.entities.User;
import models.enums.Status;

/**
 * LoginViewController
 */
public class LoginViewController implements Initializable {

    private final String HOME_PATH = "../gui/views/HomeView.fxml";
    // private final String HOME_PATH = "../gui/views/MovimentaBensView.fxml";

    @FXML
    private Button btnLogin;

    @FXML
    private Text txtProgramName;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private TextField txtUsuario;

    @FXML
    public void onBtnLoginAction() {

        if (validaUsuario(txtUsuario.getText(), (String) txtSenha.getText())) {
            MainViewController.loadView(new FXMLLoader(getClass().getResource(HOME_PATH)));
        } else {
            Alerts.showAlert("Não permitido.", "Acesso Negado.", "Usuário ou Senha inválidos.", AlertType.WARNING);
        }
    }

    private boolean validaUsuario(String usuario, String senha) {
        DB.createDatabBase();

        UserDAO userDAO = DaoFactory.createUserDAO();
        User user = userDAO.findByUser(usuario);

        if (user.getUsuario() != null && user.getStatus() == Status.ATIVO && senha.compareTo(user.getSenha()) == 0) {
            App.setAUTHENTICATED_USER(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtProgramName.setText(App.getApplicationName()); 
    }

}