package controllers.categories;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.App;
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
import models.dao.CategoryDAO;
import models.dao.DaoFactory;
import models.entities.Category;
import models.enums.Perfil;

/**
 * ConsultaUsuarioController
 */
public class ListCategoriasViewController implements Initializable {

    @FXML
    private Button btnNovo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnSair;

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<Category> tbvCategorias;

    @FXML
    private TableColumn<Category, Integer> idColumn;
    @FXML
    private TableColumn<Category, String> descricaoColumn;
    @FXML
    private TableColumn<Category, Integer> vidaUtilColumn;

    private CategoryDAO categoryDAO;

    private List<Category> categorias;
    private ObservableList<Category> obsCategories;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        categoryDAO = DaoFactory.createCategoryDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        vidaUtilColumn.setCellValueFactory(new PropertyValueFactory<>("vidaUtil"));

        Perfil userPerfil = App.getAUTHENTICATED_USER().getPerfil();
        if (userPerfil.compareTo(Perfil.MASTER) < 0) {
            btnExcluir.setDisable(true);
        }
        if (userPerfil.compareTo(Perfil.EDICAO) < 0) {
            btnEditar.setDisable(true);
            btnNovo.setDisable(true);
        }

        loadData();

        txtBusca.textProperty().addListener((obs, oldValue, newValue) -> {
            List<Category> categoriasFiltradas = categorias.stream().filter(Category -> matchString(Category, newValue))
                    .toList();
            filterTableView(categoriasFiltradas);
        });
    }

    private void loadData() {
        categorias = categoryDAO.findAll();
        obsCategories = FXCollections.observableArrayList(categorias);
        tbvCategorias.setItems(obsCategories);
    }

    public boolean matchString(Category category, String searchedText) {
        String regexExpression = "(\\w*)?" + searchedText.toLowerCase() + "(\\w*)?";
        boolean descricaoSearch = category.getDescricao().toLowerCase().matches(regexExpression);
        boolean vidaUtilSearch = String.valueOf(category.getVidaUtil()).toLowerCase().matches(regexExpression);

        return (descricaoSearch || vidaUtilSearch);
    }

    public void filterTableView(List<Category> categoriasFiltradas) {
        obsCategories = FXCollections.observableArrayList(categoriasFiltradas);
        tbvCategorias.setItems(obsCategories);
    }

    public void onBtnNovoAction() {
        loadEditView();
    }

    public void onBtnEditarAction() {
        Category selectedUser = tbvCategorias.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            loadEditView(selectedUser);
        } else {
            Alerts.showAlert("Nenhuma categoria selecionada", "", "Selecione uma categoria para editar",
                    AlertType.INFORMATION);
        }
    }

    public void onBtnExcluirAction() {
        Category selectedCategory = tbvCategorias.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            Optional<ButtonType> confirm = Alerts.getUserConfirm("Exclusão!",
                    "Confirma exclusao da categoria " + selectedCategory.getDescricao() + " ?");
            if (confirm.get() == ButtonType.OK) {
                categoryDAO.deleteById(selectedCategory.getId());
                txtBusca.setText("");
                loadData();
            }
        } else {
            Alerts.showAlert("Nenhuma categoria selecionada", "", "Selecione uma categoria para excluir",
                    AlertType.INFORMATION);
        }
    }

    public void onBtnSairAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/HomeView.fxml")));
    }

    private void loadEditView(Category... category) {
        if (category.length > 0) {
            EditCategoriasViewController.setCategoria(category[0]);
        }
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/EditCategoriasView.fxml")));
    }
}