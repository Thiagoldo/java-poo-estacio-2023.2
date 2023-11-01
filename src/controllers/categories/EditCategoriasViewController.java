package controllers.categories;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.MainViewController;
import controllers.helpers.CategoriasHelper;
import gui.util.Alerts;
import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.dao.CategoryDAO;
import models.dao.DaoFactory;
import models.entities.Category;

public class EditCategoriasViewController implements Initializable {

    @FXML
    private TextField txtDescricao;

    public TextField getTxtDescricao() {
        return txtDescricao;
    }

    @FXML
    private TextField txtVidaUtil;

    public TextField getTxtVidaUtil() {
        return txtVidaUtil;
    }

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnSair;

    private CategoryDAO categoryDAO;

    private static Category categoria;

    private CategoriasHelper helper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helper = new CategoriasHelper(this);

        Constraints.setTextFieldMaxLength(txtDescricao, 100);

        if (categoria != null) {
            helper.setCategoria(categoria);
        }
    }

    public void onBtnSalvarAction() {
        if (isComplete()) {
            Category viewSector = helper.getCategoria();
            categoryDAO = DaoFactory.createCategoryDAO();
            if (categoria == null) {
                categoryDAO.insert(viewSector);
            } else {
                viewSector.setId(categoria.getId());
                categoryDAO.update(viewSector);
            }

            voltar();
        } else {
            Alerts.showAlert("Atenção", "", "Existem campos necessários não preenchidos.", AlertType.WARNING);
        }

    }

    public void onBtnSairAction() {
        voltar();
    }

    public static void setCategoria(Category c) {
        categoria = c;
    }

    private void voltar() {
        helper.limpar();
        categoria = null;
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../../gui/views/ListCategoriasView.fxml")));
    }

    private boolean isComplete() {
        boolean checkDescricao = txtDescricao.getText() != "";
        boolean checkVidaUtil = txtVidaUtil.getText() != "";

        return (checkDescricao && checkVidaUtil);
    }

}
