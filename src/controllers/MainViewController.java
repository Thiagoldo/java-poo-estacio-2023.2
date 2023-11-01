package controllers;

import java.io.IOException;

import application.App;
import gui.util.Alerts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainViewController {

    public synchronized static void loadView(FXMLLoader loader) {
        try {
            VBox newVBox = loader.load();

            Scene mainScene = App.getMainScene();

            ScrollPane mainScrollPane = (ScrollPane) mainScene.getRoot();

            VBox vBox = (VBox) mainScrollPane.getContent();
            
            vBox.getChildren().clear();
            vBox.getChildren().addAll(newVBox.getChildren());
            
            mainScrollPane.setFitToHeight(true);
            mainScrollPane.setFitToWidth(true);

            Stage stage = (Stage) mainScene.getWindow();

            stage.hide();
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
}