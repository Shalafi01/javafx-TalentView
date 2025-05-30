package controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.contattoICE;
import model.databaseDAO;
import model.lavoratore;
import model.lavoro;

import static model.DaoPattern.DaoDatabase;

public abstract class Controller
{
    databaseDAO database = DaoDatabase;

    public void cambiaScena(String s, Node root){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(s));
            Scene scene = root.getScene();
            ((Stage) scene.getWindow()).setScene(new Scene(loader.load(), scene.getWidth(), scene.getHeight()));
        }catch (Exception e){e.printStackTrace();}
    }

    public Button creaBottoneNuovo(String text, Object object, Node root){
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #008021;  -fx-text-fill: white;");
        btn.setOnAction(e -> {
            database.setDaAggiungere(object);
            cambiaScena("/view/nuoviDati.fxml", root);
        });
        return btn;
    }

    public void aggiungiGestione(Object object, lavoratore l, GridPane grid, Node root, VBox scheda){
        Button btn = new Button("Modifica");
        Button btn2 = new Button("Elimina");
        btn.setStyle("-fx-background-color: #007FDB;  -fx-text-fill: white;");
        btn2.setStyle("-fx-background-color: #FF2626;  -fx-text-fill: white;");
        btn.setPrefWidth(70);
        btn2.setPrefWidth(70);

        btn.setOnAction(e -> {
            if(object instanceof contattoICE temp){
                database.setDaModificare(object);
                l.getContattiICE().remove(temp);
                cambiaScena("/view/nuoviDati.fxml", root);
            }
            else if(object instanceof lavoro temp) {
                database.setDaModificare(object);
                l.getLavori().remove(temp);
                cambiaScena("/view/nuoviDati.fxml", root);
            }
            else if(object instanceof lavoratore temp) {
                database.setSelected(temp);
                cambiaScena("/view/interfacciaLavoratore.fxml", root);
            }
        });

        btn2.setOnAction(e -> {
            String s = "lavoratore";
            if(object instanceof contattoICE)
                s = "contatto d'emergenza";
            if(object instanceof lavoro)
                s = "lavoro";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler eliminare questo "+s+"?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES)
            {
                if((object instanceof contattoICE) && !(l.getContattiICE().size() > 1)){
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Almeno un contatto di emergenza è obbligatorio");
                    alert2.showAndWait();
                } else {
                    if(object instanceof contattoICE temp) {
                        l.getContattiICE().remove(temp);
                        cambiaScena("/view/interfacciaLavoratore.fxml", root);
                    }
                    else if(object instanceof lavoro temp) {
                        l.getLavori().remove(temp);
                        cambiaScena("/view/interfacciaLavoratore.fxml", root);
                    }
                    else if(object instanceof lavoratore temp) {
                        database.getLavoratori().remove(temp);
                        cambiaScena("/view/elencoLavoratori.fxml", root);
                    }
                }
            }
        });

        VBox vbox = new VBox(btn, btn2);
        vbox.setAlignment(Pos.BOTTOM_RIGHT);
        vbox.setSpacing(4);
        StackPane stackPane = new StackPane(vbox);
        stackPane.setMaxWidth(800);
        StackPane.setMargin(vbox, new Insets(0, 8, 8, 0));

        StackPane stackPane2 = new StackPane(grid, stackPane);
        stackPane2.setAlignment(Pos.CENTER);
        scheda.getChildren().add(stackPane2);
        VBox.setMargin(stackPane2, new Insets(0, 20, 0, 20));
    }
}
