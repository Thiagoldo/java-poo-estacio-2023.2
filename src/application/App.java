package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.entities.User;

public class App extends Application {

    private final String LOGIN_PATH = "../gui/views/LoginView.fxml";
    private final static String APPLICATION_NAME = "Controle Patrimonial";
    private static User AUTHENTICATED_USER;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource(LOGIN_PATH));
        ScrollPane mainScrollPane = new ScrollPane();
        VBox loginVBox = loginLoader.load();

        mainScrollPane.setContent(loginVBox);

        mainScene = new Scene(mainScrollPane);

        stage.setScene(mainScene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle(APPLICATION_NAME);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static String getApplicationName() {
        return APPLICATION_NAME;
    }

    public static User getAUTHENTICATED_USER() {
        return AUTHENTICATED_USER;
    }

    public static void setAUTHENTICATED_USER(User user) {
        AUTHENTICATED_USER = user;
    }

}
