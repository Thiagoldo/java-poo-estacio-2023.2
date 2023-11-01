package controllers.sectors;

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
import models.dao.SectorDAO;
import models.entities.Sector;
import models.entities.User;

/**
 * ConsultaUsuarioController
 */
public class ListSetoresViewController implements Initializable {

    @FXML
    private Button btnBuscar;

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
    private TableView<Sector> tbvSetores;

    @FXML
    private TableColumn<Sector, Integer> idColumn;
    @FXML
    private TableColumn<Sector, String> descricaoColumn;
    @FXML
    private TableColumn<User, String> responsavelColumn;

    private SectorDAO sectorDAO;

    private List<Sector> setores;
    private ObservableList<Sector> obsSectors;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        sectorDAO = DaoFactory.createSectorDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        responsavelColumn.setCellValueFactory(new PropertyValueFactory<>("responsavel"));

        loadData();

        txtBusca.textProperty().addListener((obs, oldValue, newValue) -> {
            List<Sector> setoresFiltrados = setores.stream().filter(sector -> matchString(sector, newValue)).toList();
            filterTableView(setoresFiltrados);
        });
    }

    private void loadData() {
        setores = sectorDAO.findAll();
        obsSectors = FXCollections.observableArrayList(setores);
        tbvSetores.setItems(obsSectors);
    }

    public boolean matchString(Sector sector, String searchedText) {
        String regexExpression = "(\\w*)?" + searchedText.toLowerCase() + "(\\w*)?";
        boolean descricaoSearch = sector.getDescricao().toLowerCase().matches(regexExpression);
        boolean responsavelSearch = sector.getResponsavel().getNome().toLowerCase().matches(regexExpression);

        return (descricaoSearch || responsavelSearch);
    }

    public void filterTableView(List<Sector> setoresFiltrados) {
        obsSectors = FXCollections.observableArrayList(setoresFiltrados);
        tbvSetores.setItems(obsSectors);
    }

    public void onBtnNovoAction() {
        loadEditView();
    }

    public void onBtnEditarAction() {
        Sector selectedSector = tbvSetores.getSelectionModel().getSelectedItem();
        if (selectedSector != null) {
            loadEditView(selectedSector);
        } else {
            Alerts.showAlert("Nenhum setor selecionado", "", "Selecione um setor para editar",
                    AlertType.INFORMATION);
        }
    }

    public void onBtnExcluirAction() {
        Sector selectedSector = tbvSetores.getSelectionModel().getSelectedItem();
        if (selectedSector != null) {
            Optional<ButtonType> confirm = Alerts.getUserConfirm("Exclusão!",
                    "Confirma exclusao do setor " + selectedSector.getDescricao() + " ?");
            if (confirm.get() == ButtonType.OK) {
                sectorDAO.deleteById(selectedSector.getId());
                txtBusca.setText("");
                loadData();
            }
        } else {
            Alerts.showAlert("Nenhum setor selecionado", "", "Selecione um setor para excluir",
                    AlertType.INFORMATION);
        }
    }

    public void onBtnSairAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/HomeView.fxml")));
    }

    public void loadEditView(Sector... sector) {
        if (sector.length > 0) {
            EditSetoresViewController.setSetor(sector[0]);
        }
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/EditSetoresView.fxml")));
    }

}