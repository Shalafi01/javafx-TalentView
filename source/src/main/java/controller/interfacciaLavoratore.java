package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.contattoICE;
import model.lavoratore;
import model.lavoro;
import java.net.URL;
import java.util.ResourceBundle;

public class interfacciaLavoratore extends Controller implements Initializable {

    public VBox scheda;
    public VBox root;

    public void torna(ActionEvent actionEvent) {
        cambiaScena("/view/elencoLavoratori.fxml", root);
    }

    private Object aggiornaDati(Object object){
        if (object instanceof contattoICE c)
            database.getSelected().getContattiICE().add(c);
        if (object instanceof lavoro l)
            database.getSelected().getLavori().add(l);
        if (object instanceof lavoratore L) {
            database.setSelected(L);
            database.getLavoratori().add(L);
        }
        return null;
    }

    private GridPane newGrid(){
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().add(constraint);
        gridpane.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(2))));
        gridpane.setVgap(8);
        gridpane.setHgap(5);
        gridpane.setMaxWidth(800);
        VBox.setMargin(gridpane, new Insets(0, 20, 0, 20));
        return gridpane;
    }

    private void titoloSezione(String s){
        Label titolo = new Label(s+"\n");
        titolo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        scheda.getChildren().add(titolo);
    }

    private void aggiungiBtnNuovo(Button btn, Pane root){
        StackPane stackPane = new StackPane(btn);
        stackPane.setMaxWidth(840);
        stackPane.setAlignment(Pos.BOTTOM_RIGHT);
        StackPane.setMargin(btn, new Insets(0, 20, 2, 0));
        root.getChildren().add(stackPane);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        database.setDaModificare(aggiornaDati(database.getDaModificare()));
        database.setDaAggiungere(aggiornaDati(database.getDaAggiungere()));

        lavoratore selected = database.getSelected();

        titoloSezione("\nDati anagrafici");
        GridPane gridDatiAnagrafici = newGrid();

        Button btn1 = new Button("Modifica anagrafiche");
        btn1.setStyle("-fx-background-color: #007FDB;  -fx-text-fill: white;");
        btn1.setOnAction(e -> {
            database.setDaModificare(selected);
            database.getLavoratori().remove((selected));
            cambiaScena("/view/nuoviDati.fxml", root);
        });
        aggiungiBtnNuovo(btn1, scheda);

        Label nomeCognome = new Label(selected.getNome() + " " + selected.getCognome());
        nomeCognome.setStyle("-fx-font-weight:bold;");
        gridDatiAnagrafici.add(nomeCognome, 0, 0);
        gridDatiAnagrafici.add(new Label("Contatti:"), 1, 3);
        gridDatiAnagrafici.add(new Label(selected.getEmail()), 1, 4);
        gridDatiAnagrafici.add(new Label(selected.getTelefono()), 1, 5);
        gridDatiAnagrafici.add(new Label("Data di nascita:\t\t"+selected.getDataNascita()), 0, 3);
        gridDatiAnagrafici.add(new Label("Luogo di nascita:\t\t"+selected.getLuogoNascita()), 0, 4);
        gridDatiAnagrafici.add(new Label("Nazionalità:\t\t\t"+selected.getNazionalita()), 0, 5);
        gridDatiAnagrafici.add(new Label("Indirizzo:\t\t\t\t"+selected.getIndirizzo()), 0, 6);
        gridDatiAnagrafici.add(new Label("Lingue parlate:\t\t\t"+selected.stampaLingueParlate()), 0, 7);
        gridDatiAnagrafici.add(new Label("Zone disponibilità:\t\t"+selected.stampaZoneDisponibilita()), 0, 8);
        gridDatiAnagrafici.add(new Label("Periodi disponibilità:\t"+selected.stampaPeriodiDisponibilita()), 0, 9);
        gridDatiAnagrafici.add(new Label("Patente:\t\t"+selected.getPatente()), 1, 7);
        gridDatiAnagrafici.add(new Label("Automunito:\t"+selected.getAutomunito()), 1, 8);
        scheda.getChildren().add(gridDatiAnagrafici);

        //=============================================================================================================================

        titoloSezione("\n\nEsperienze precedenti");
        aggiungiBtnNuovo(creaBottoneNuovo("Nuovo lavoro", new lavoro(), scheda), scheda);

        for(lavoro r:selected.getLavori()) {
            GridPane gridLavori = newGrid();
            gridLavori.add(new Label("Nome azienda:\t"+r.getNomeAzienda()), 0, 0);
            gridLavori.add(new Label("Luogo:\t\t\t"+r.getLuogo()), 0, 1);
            gridLavori.add(new Label("Periodo:\t\t\t"+r.stampaPeriodo()), 0, 2);
            gridLavori.add(new Label("Mansioni:\t\t\t"+r.stampaMansioni()), 0, 3);
            gridLavori.add(new Label("Retribuzione:\t\t"+r.getRetribuzione()+"€ al giorno"), 0, 4);
            aggiungiGestione(r, selected, gridLavori, root, scheda);
        }

        //=============================================================================================================================

        titoloSezione("\n\nContatti in caso di emergenza");
        aggiungiBtnNuovo(creaBottoneNuovo("Nuovo contatto", new contattoICE(), scheda), scheda);

        for(contattoICE c:selected.getContattiICE()) {
            GridPane gridContatti = newGrid();
            gridContatti.add(new Label("Nome:\t"+c.getNome()+ " "+c.getCognome()), 0, 0);
            gridContatti.add(new Label("Email:\t"+c.getEmail()), 0, 1);
            gridContatti.add(new Label("Telefono:\t"+c.getTelefono()), 0, 2);
            aggiungiGestione(c, selected, gridContatti, root, scheda);
        }
    }
}