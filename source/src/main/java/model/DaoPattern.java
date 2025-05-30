package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import java.io.IOException;

public class DaoPattern extends Application {

    public static ricerca r = null;
    public static String[] parametri = {null, null, null, null, null, null, null, null, null};
    public static databaseDAO DaoDatabase;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/autenticazione.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Ricerca lavoro stagionale");
        primaryStage.show();
    }

    public static void main(String[] args) {
        DaoDatabase = new databaseDAOimpl();
        launch(args);
    }
}