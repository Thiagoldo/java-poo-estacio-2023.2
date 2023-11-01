package controllers.moviments;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.App;
import controllers.MainViewController;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import models.dao.AssetDAO;
import models.dao.AssetSectorDAO;
import models.dao.DaoFactory;
import models.entities.Asset;
import models.entities.AssetSector;
import models.entities.Sector;
import models.enums.Status;

public class BySectorViewController implements Initializable {

    @FXML
    private Text txtSetor;
    @FXML
    private Text txtValorTotal;
    @FXML
    private DatePicker dtInicio;
    @FXML
    private DatePicker dtFim;
    @FXML
    private TextField txtObservacao;

    private static Sector setor;

    public static void setSetor(Sector selectedSetor) {
        setor = selectedSetor;
    }

    private List<Asset> ativos;
    private List<AssetSector> ativosSetores;
    private List<AssetSector> ativosNoSetor;
    private List<AssetSector> ativosRemovidosDoSetor;

    private AssetDAO assetDAO;
    private AssetSectorDAO assetSectorDAO;

    @FXML
    private TableView<Asset> tbvAtivosDisponiveis;
    @FXML
    private TableColumn<Asset, Integer> idFromColumn;
    @FXML
    private TableColumn<Asset, String> descricaoFromColumn;
    @FXML
    private TableColumn<Asset, String> categoriaFromColumn;

    @FXML
    private TableView<AssetSector> tbvAtivosNoSetor;
    @FXML
    private TableColumn<AssetSector, Integer> idToColumn;
    @FXML
    private TableColumn<AssetSector, String> descricaoToColumn;
    @FXML
    private TableColumn<AssetSector, String> categoriaToColumn;
    @FXML
    private TableColumn<AssetSector, LocalDate> dataInicioToColumn;

    @FXML
    private Button btnAdicionar;
    @FXML
    private Button btnRetirar;

    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnSair;

    private ObservableList<AssetSector> obsAtivosNoSetor;
    private ObservableList<Asset> obsAtivosDisponiveis;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        assetDAO = DaoFactory.createAssetDAO();
        assetSectorDAO = DaoFactory.createAssetSectorDAO();

        txtSetor.setText(setor.getDescricao());

        idFromColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricaoFromColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        categoriaFromColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        idToColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<AssetSector, Integer>, ObservableValue<Integer>>() {
                    public ObservableValue<Integer> call(CellDataFeatures<AssetSector, Integer> p) {
                        return new ReadOnlyObjectWrapper<>(p.getValue().getAtivo().getId());
                    }
                });
        descricaoToColumn.setCellValueFactory(new PropertyValueFactory<>("ativo"));
        categoriaToColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<AssetSector, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<AssetSector, String> p) {
                        return new ReadOnlyObjectWrapper<>(p.getValue().getAtivo().getCategoria().getDescricao());
                    }
                });
        dataInicioToColumn.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));

        loadData();
        calcularValorTotal();
    }

    private void loadData() {
        ativos = assetDAO.findAll();
        ativosSetores = assetSectorDAO.findAll();
        ativosRemovidosDoSetor = new ArrayList<>();

        // Filtra ativos no setor
        ativosNoSetor = ativosSetores.stream()
                .filter(ativo -> ativo.getAtivo().getStatus() == Status.ATIVO)
                .filter(ativo -> ativo.getDataFim() == null)
                .filter(ativo -> ativo.getSetor().equals(setor))
                .collect(Collectors.toList());
        obsAtivosNoSetor = FXCollections.observableArrayList(ativosNoSetor);
        tbvAtivosNoSetor.setItems(obsAtivosNoSetor);

        // Remove ativos indisponiveis da lista de ativos
        ativosSetores.stream()
                .filter(ativo -> ativo.getAtivo().getStatus() == Status.ATIVO)
                .filter(ativo -> ativo.getDataFim() == null)
                .map(a -> a.getAtivo())
                .forEach(a -> ativos.remove(a));

        obsAtivosDisponiveis = FXCollections.observableArrayList(ativos);

        tbvAtivosDisponiveis.setItems(obsAtivosDisponiveis);
    }

    private void calcularValorTotal() {
        Double total = ativosNoSetor.stream()
                .map(ativo -> ativo.getAtivo().getValor())
                .reduce(0.0, Double::sum);

        txtValorTotal.setText(String.format("%.2f", total));
    }

    public void onBtnAdicionarAction() {
        if (dataInicioPreenchida() && ativoDisponivelSelecionado()) {
            Asset auxAtivo = tbvAtivosDisponiveis.getSelectionModel().getSelectedItem();

            AssetSector auxAtivoSetor = new AssetSector();

            auxAtivoSetor.setAtivo(auxAtivo);
            auxAtivoSetor.setSetor(setor);
            auxAtivoSetor.setDataInicio(dtInicio.getValue());
            auxAtivoSetor.setCadastradoPor(App.getAUTHENTICATED_USER());
            auxAtivoSetor.setCadastradoEm(LocalDateTime.now());

            ativosNoSetor.add(auxAtivoSetor);
            ativos.remove(auxAtivo);

            obsAtivosNoSetor = FXCollections.observableArrayList(ativosNoSetor);
            tbvAtivosNoSetor.getItems().clear();
            tbvAtivosNoSetor.setItems(obsAtivosNoSetor);

            obsAtivosDisponiveis = FXCollections.observableArrayList(ativos);
            tbvAtivosDisponiveis.getItems().clear();
            tbvAtivosDisponiveis.setItems(obsAtivosDisponiveis);

            clearTexts();
            calcularValorTotal();
        } else {
            Alerts.showAlert("Atenção", null, "Data inválida ou nenhum bem selecionado", AlertType.ERROR);
        }
    }

    public void onBtnRetirarAction() {
        if (dataFimPreenchida() && ativoNoSetorSelecionado()) {
            AssetSector auxAtivoSetor = tbvAtivosNoSetor.getSelectionModel().getSelectedItem();

            auxAtivoSetor.setDataFim(dtFim.getValue());
            auxAtivoSetor.setObservacao(txtObservacao.getText());
            auxAtivoSetor.setAlteradoPor(App.getAUTHENTICATED_USER());
            auxAtivoSetor.setAlteradoEm(LocalDateTime.now());

            ativosNoSetor.remove(auxAtivoSetor);
            ativosRemovidosDoSetor.add(auxAtivoSetor);
            // ativos.add(auxAtivoSetor.getAtivo());

            obsAtivosNoSetor = FXCollections.observableArrayList(ativosNoSetor);
            tbvAtivosNoSetor.getItems().clear();
            tbvAtivosNoSetor.setItems(obsAtivosNoSetor);

            // obsAtivosDisponiveis = FXCollections.observableArrayList(ativos);
            // tbvAtivosDisponiveis.getItems().clear();
            // tbvAtivosDisponiveis.setItems(obsAtivosDisponiveis);

            clearTexts();
            calcularValorTotal();
        } else {
            Alerts.showAlert("Atenção", null, "Data inválida ou nenhum bem selecionado", AlertType.ERROR);
        }
    }

    public void onBtnSalvarAction() {

        Optional<ButtonType> confirm = Alerts.getUserConfirm("Alterações!",
                "Confirma as alterações realizadas nos bens do setor " + setor.getDescricao() + " ?");
        
        int alteracoes = 0;

        if (confirm.get() == ButtonType.OK) {
            if (!ativosNoSetor.isEmpty()) {
                for (AssetSector ativoNoSetor : ativosNoSetor) {
                    boolean founded = false;
                    for (AssetSector ativoSetor : ativosSetores) {
                        if (ativoNoSetor.equals(ativoSetor)) {
                            founded = true;
                        }
                    }
                    if (!founded) {
                        assetSectorDAO.insert(ativoNoSetor);
                        alteracoes++;
                    }
                }
            }
            if (!ativosRemovidosDoSetor.isEmpty()) {
                for (AssetSector ativoRemovidoDoSetor : ativosRemovidosDoSetor) {
                    assetSectorDAO.update(ativoRemovidoDoSetor);
                    alteracoes++;
                }
            }

            if (alteracoes > 0) {
                Alerts.showAlert("Sucesso!", "Alterado", "Alterações salvas com sucesso!", AlertType.INFORMATION);
            } else {
                Alerts.showAlert("Atenção!", "Sem alterações", "Nenhuma alteração identificada!", AlertType.WARNING);
            }
        }
    }

    public void onBtnSairAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/HomeView.fxml")));
    }

    private boolean dataInicioPreenchida() {
        return dtInicio.getValue() == null ? false : true;
    }

    private boolean dataFimPreenchida() {
        return dtFim.getValue() == null ? false : true;
    }

    private boolean ativoDisponivelSelecionado() {
        return tbvAtivosDisponiveis.getSelectionModel().getSelectedItem() == null ? false : true;
    }

    private boolean ativoNoSetorSelecionado() {
        return tbvAtivosNoSetor.getSelectionModel().getSelectedItem() == null ? false : true;
    }

    private void clearTexts() {
        dtInicio.setValue(null);
        dtFim.setValue(null);
        txtObservacao.setText(null);
    }
}
