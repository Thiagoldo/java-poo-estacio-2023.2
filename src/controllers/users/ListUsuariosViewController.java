package controllers.users;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import controllers.MainViewController;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.dao.DaoFactory;
import models.dao.UserDAO;
import models.entities.User;

/**
 * ConsultaUsuarioController
 */
public class ListUsuariosViewController implements Initializable {

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnSair;

    @FXML
    private Button btnNovo;

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<User> tbvUsuarios;

    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> nomeColumn;
    @FXML
    private TableColumn<User, String> loginColumn;
    @FXML
    private TableColumn<User, String> perfilColumn;
    @FXML
    private TableColumn<User, String> statusColumn;

    private UserDAO userDAO;

    private List<User> usuarios;
    private ObservableList<User> obsUsuarios;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        userDAO = DaoFactory.createUserDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        perfilColumn.setCellValueFactory(new PropertyValueFactory<>("perfil"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadData();

        txtBusca.textProperty().addListener((obs, oldValue, newValue) -> {
            List<User> usuariosFiltrados = usuarios.stream().filter(user -> matchString(user, newValue)).toList();
            filterTableView(usuariosFiltrados);
        });
    }

    public void loadData() {
        usuarios = userDAO.findAll();
        obsUsuarios = FXCollections.observableArrayList(usuarios);
        tbvUsuarios.setItems(obsUsuarios);
    }

    public boolean matchString(User user, String searchedText) {
        String regexExpression = "(\\w*)?" + searchedText.toLowerCase() + "(\\w*)?";
        boolean nameSearch = user.getNome().toLowerCase().matches(regexExpression);
        boolean loginSearch = user.getUsuario().toLowerCase().matches(regexExpression);

        return (nameSearch || loginSearch);
    }

    public void filterTableView(List<User> usuariosFiltrados) {
        obsUsuarios = FXCollections.observableArrayList(usuariosFiltrados);
        tbvUsuarios.setItems(obsUsuarios);
    }

    public void onBtnNovoAction() {
        loadEditView();
    }

    public void onBtnEditarAction() {
        User selectedUser = tbvUsuarios.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            loadEditView(selectedUser);
        } else {
            Alerts.showAlert("Nenhum usuário selecionado", "", "Selecione um usuário para editar",
                    AlertType.INFORMATION);
        }
    }

    public void onBtnExcluirAction() {
        User selectedUser = tbvUsuarios.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Optional<ButtonType> confirm = Alerts.getUserConfirm("Exclusão!",
                    "Confirma exclusao do usuário " + selectedUser.getNome() + " ?");
            if (confirm.get() == ButtonType.OK) {
                userDAO.deleteById(selectedUser.getId());
                txtBusca.setText("");
                loadData();
            }
        } else {
            Alerts.showAlert("Nenhum usuário selecionado", "", "Selecione um usuário para excluir",
                    AlertType.INFORMATION);
        }

    }

    public void onBtnSairAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/HomeView.fxml")));
    }

    public void loadEditView(User... user) {
        if (user.length > 0) {
            EditUsuariosViewController.setUsuario(user[0]);
        }
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/EditUsuariosView.fxml")));
    }

}