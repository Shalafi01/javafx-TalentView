package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.dipendente;

public class autenticazione extends Controller{
    public TextField login;
    public PasswordField password;
    public Label err;
    public VBox root;

    public void login(ActionEvent e) {

        for (dipendente d: database.getDipendenti())
            if(d.getLogin().equals(login.getText()) && d.getPassword().equals(password.getText()))
                cambiaScena("/view/elencoLavoratori.fxml", root);
        err.setText("ID o password incorretti");
    }
}
