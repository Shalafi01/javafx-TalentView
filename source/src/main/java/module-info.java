module esercizio.vr457792 {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires java.sql;


    opens model to javafx.fxml;
    exports model;
    exports controller;
    opens controller to javafx.fxml;
}