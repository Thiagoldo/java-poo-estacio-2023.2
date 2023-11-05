package controllers.assets;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import gui.util.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import controllers.MainViewController;
import controllers.helpers.AtivosHelper;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.dao.AssetDAO;
import models.dao.CategoryDAO;
import models.dao.DaoFactory;
import models.entities.Asset;
import models.entities.Category;
import models.enums.Status;

public class EditAtivosViewController implements Initializable {

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtDataCadastro;

    @FXML
    private TextField txtNotaFiscal;

    @FXML
    private TextField txtNumeroSerie;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextArea txtObservacao;

    @FXML
    private TextField txtValor;

    @FXML
    private ComboBox<Category> cBoxCategorias;

    @FXML
    private ComboBox<Status> cBoxStatus;

    @FXML
    private ImageView imgAtivo;

    @FXML
    private Button btnAdicionarImagem;

    @FXML
    private Button btnLimparImagem;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnSair;

    private static Asset ativo;

    private AtivosHelper helper;

    private AssetDAO assetDAO;

    public TextField getTxtDescricao() {
        return txtDescricao;
    }

    public TextField getTxtDataCadastro() {
        return txtDataCadastro;
    }

    public TextField getTxtNotaFiscal() {
        return txtNotaFiscal;
    }

    public TextField getTxtNumeroSerie() {
        return txtNumeroSerie;
    }

    public TextField getTxtModelo() {
        return txtModelo;
    }

    public TextArea getTxtObservacao() {
        return txtObservacao;
    }

    public TextField getTxtValor() {
        return txtValor;
    }

    public ComboBox<Category> getcBoxCategorias() {
        return cBoxCategorias;
    }

    public ComboBox<Status> getcBoxStatus() {
        return cBoxStatus;
    }

    public ImageView getImgAtivo() {
        return imgAtivo;
    }

    public Button getBtnAdicionarImagem() {
        return btnAdicionarImagem;
    }

    public Button getBtnSalvar() {
        return btnSalvar;
    }

    public Button getBtnSair() {
        return btnSair;
    }

    public static void setAtivo(Asset a) {
        ativo = a;
    }

    private Path imagePath = null;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        helper = new AtivosHelper(this);

        Constraints.setTextFieldMaxLength(txtDescricao, 100);
        Constraints.setTextFieldMaxLength(txtNotaFiscal, 100);
        Constraints.setTextFieldMaxLength(txtNumeroSerie, 100);
        Constraints.setTextFieldMaxLength(txtModelo, 100);
        Constraints.setTextAreaMaxLength(txtObservacao, 255);
        Constraints.setTextFieldDouble(txtValor);

        CategoryDAO categoryDAO = DaoFactory.createCategoryDAO();
        List<Category> categories = categoryDAO.findAll();

        ObservableList<Category> obsCategories = FXCollections.observableArrayList(categories);
        cBoxCategorias.setItems(obsCategories);
        cBoxCategorias.getSelectionModel().selectFirst();

        ObservableList<Status> obsStatus = FXCollections.observableArrayList(Status.values());
        cBoxStatus.setItems(obsStatus);
        cBoxStatus.getSelectionModel().selectFirst();

        if (ativo != null) {
            helper.setAtivo(ativo);
        }
    }

    public void onBtnSalvarAction() {
        if (isComplete()) {
            Asset viewAsset = helper.getAtivo();
            if (imagePath != null) {
                setUriImageToAsset(imagePath);
            }
            assetDAO = DaoFactory.createAssetDAO();
            if (ativo == null) {
                assetDAO.insert(viewAsset);
            } else {
                viewAsset.setId(ativo.getId());
                assetDAO.update(viewAsset);
            }

            voltar();
        } else {
            Alerts.showAlert("Atenção", "", "Existem campos necessários não preenchidos.", AlertType.WARNING);
        }

    }

    public void onBtnSairAction() {
        voltar();
    }

    public void onBtnAdicionarImagemAction() {
        try {
            File selectedImage = Files.chooseImage();

            imagePath = selectedImage.toPath();
            Image image = new Image(selectedImage.toURI().toString());

            imgAtivo.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Erro inesperado", "Erro ao definir a imagem.",
                    "Verifique se o arquivo selecionado é uma imagem.\n" + e.getMessage(), AlertType.ERROR);
        }
    }

    public void onBtnLimparImagemAction() {
        if (imgAtivo.getImage() != null){
            imgAtivo.setImage(null);
        }
    }

    private void voltar() {
        helper.limpar();
        ativo = null;
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/ListAtivosView.fxml")));
    }

    private boolean isComplete() {
        boolean checkDescricao = txtDescricao.getText() != "";
        boolean checkCategoria = !cBoxCategorias.getSelectionModel().isEmpty();
        boolean checkValor = txtValor.getText() != "";

        return (checkDescricao && checkCategoria && checkValor);
    }

    private void setUriImageToAsset(Path source) {
        try {
            // !Copiar a imagem selecionada para a pasta:
            // ! ../imgs/assets_images
            String copiedImage = Files.copyPasteImage(imagePath);
            // !Settar a url da imagem copiada no objeto Asset
            ativo.setImagem(copiedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
