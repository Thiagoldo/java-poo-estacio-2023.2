package controllers;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import application.App;
import db.DB;
import gui.util.Reports;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;

public class HomeViewController implements Initializable {

    @FXML
    private MenuItem menuItemBens;
    @FXML
    private MenuItem menuItemCategorias;
    @FXML
    private MenuItem menuItemSetores;
    @FXML
    private MenuItem menuItemUsuarios;
    @FXML
    private MenuItem menuItemMovimentBySector;
    @FXML
    private MenuItem menuItemReportAssetsActiveds;
    @FXML
    private Text textUser;
    @FXML
    private Text txtQuantidadeBensAtivos;
    @FXML
    private Text txtUltimoBemCadastrado;
    @FXML
    private Text txtValorTotalBensAtivos;
    @FXML
    private Label txtProgramName;

    @FXML
    public void onMenuItemUsuariosAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ListUsuariosView.fxml")));
    }

    @FXML
    public void onMenuItemCategoriasAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ListCategoriasView.fxml")));
    }

    @FXML
    public void onMenuItemSetoresAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ListSetoresView.fxml")));
    }

    @FXML
    public void onMenuItemBensAction() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ListAtivosView.fxml")));
    }

    @FXML
    public void onMenuItemMovimentBySector() {
        MainViewController.loadView(new FXMLLoader(getClass().getResource("../gui/views/ChoseSectorView.fxml")));
    }

    @FXML
    public void onMenuItemReportAssetsActiveds() {
        Reports.openReport("src\\gui\\reports\\Report_Bens_Ativos.jrxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textUser.setText(App.getAUTHENTICATED_USER().getNome());
        txtProgramName.setText(App.getApplicationName());

        //? ViewBensAtivos
        Integer bensAtivos = DB.homeQueryBensAtivos();
        txtQuantidadeBensAtivos.setText(bensAtivos + " Bens Ativos");
        //? ViewValorTotalBensAtivos
        Double valorTotalBensAtivos = DB.homeQueryValorTotalBensAtivos();
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtValorTotalBensAtivos.setText(fmt.format(valorTotalBensAtivos) + " em Bens Ativos");
        //? ViewUltimoBemCadastrado
        String ultimoBemCadastrado = DB.homeQueryUltimoBemCadastrado();
        txtUltimoBemCadastrado.setText("Último Cadastro: " + ultimoBemCadastrado);
    }
}
