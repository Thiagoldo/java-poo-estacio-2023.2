package controllers.assets;

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
import models.dao.AssetDAO;
import models.dao.DaoFactory;
import models.entities.Asset;
import models.entities.Category;
import models.enums.Perfil;

public class ListAtivosViewController implements Initializable {

    @FXML
    private Button btnNovo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnSair;

    @FXML
    private TableView<Asset> tbvAtivos;

    @FXML
    private TableColumn<Category, String> categoriaColumn;

    @FXML
    private TableColumn<Asset, String> descricaoColumn;

    @FXML
    private TableColumn<Asset, Integer> idColumn;

    @FXML
    private TableColumn<Asset, Double> valorColumn;

    @FXML
    private TableColumn<Asset, String> statusColumn;

    @FXML
    private TextField txtBusca;

    private AssetDAO assetDAO;

    private List<Asset> ativos;
    private ObservableList<Asset> obsAssets;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        assetDAO = DaoFactory.createAssetDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        valorColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

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
            List<Asset> ativosFiltrados = ativos.stream().filter(ativo -> matchString(ativo, newValue)).toList();
            filterTableView(ativosFiltrados);
        });
    }

    private void loadData() {
        ativos = assetDAO.findAll();
        obsAssets = FXCollections.observableArrayList(ativos);
        tbvAtivos.setItems(obsAssets);
    }

    public boolean matchString(Asset asset, String searchedText) {
        String regexExpression = "(\\w*)?" + searchedText.toLowerCase() + "(\\w*)?";

        String descricaoString = asset.getDescricao() != null ? asset.getDescricao() : "";
        String numeroSerieString = asset.getNumeroSerie()  != null ? asset.getNumeroSerie() : "";
        String notaFiscalString = asset.getNotaFiscal()  != null ? asset.getNotaFiscal() : "";
        String modeloString = asset.getModelo()  != null ? asset.getModelo() : "";
        String categoriaString = asset.getCategoria().toString()  != null ? asset.getCategoria().toString() : "";

        boolean descricaoSearch = descricaoString.toLowerCase().matches(regexExpression);
        boolean numeroSerieSearch = numeroSerieString.toLowerCase().matches(regexExpression);
        boolean notaFiscalSearch = notaFiscalString.toLowerCase().matches(regexExpression);
        boolean modeloSearch = modeloString.toLowerCase().matches(regexExpression);
        boolean categoriaSearch = categoriaString.toString().toLowerCase().matches(regexExpression);

        return (descricaoSearch || numeroSerieSearch || notaFiscalSearch || modeloSearch || categoriaSearch);
    }

    public void filterTableView(List<Asset> ativosFiltrados) {
        obsAssets = FXCollections.observableArrayList(ativosFiltrados);
        tbvAtivos.setItems(obsAssets);
    }

    public void onBtnNovoAction() {
        loadEditView();
    }

    public void onBtnEditarAction() {
        Asset selectedAsset = tbvAtivos.getSelectionModel().getSelectedItem();
        if (selectedAsset != null) {
            loadEditView(selectedAsset);
        } else {
            Alerts.showAlert("Nenhum ativo selecionado", "", "Selecione um ativo para editar",
                    AlertType.INFORMATION);
        }
    }

    public void onBtnExcluirAction() {

        Asset selectedAsset = tbvAtivos.getSelectionModel().getSelectedItem();
        if (selectedAsset != null) {
            Optional<ButtonType> confirm = Alerts.getUserConfirm("Exclusão!",
                    "Confirma exclusao do ativo " + selectedAsset.getDescricao() + " ?");
            if (confirm.get() == ButtonType.OK) {
                assetDAO.deleteById(selectedAsset.getId());
                txtBusca.setText("");
                loadData();
            }
        } else {
            Alerts.showAlert("Nenhum ativo selecionado", "", "Selecione um ativo para excluir ",
                    AlertType.INFORMATION);
        }

    }

    public void onBtnSairAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/HomeView.fxml")));
    }

    public void loadEditView(Asset... asset) {
        if (asset.length > 0) {
            EditAtivosViewController.setAtivo(asset[0]);
        }
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/EditAtivosView.fxml")));
    }
}
