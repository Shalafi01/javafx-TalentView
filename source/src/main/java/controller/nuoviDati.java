package controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import model.*;
import java.net.URL;
import java.util.*;

public class nuoviDati extends Controller implements Initializable {
    public VBox elenco;
    public VBox root;

    private void annulla(Button btn, String s) {
        btn.setOnAction(e -> {
            database.setDaAggiungere(null);
            cambiaScena(s, root);
        });
    }

    private TextField nuovoCampo(GridPane grid, String s, String value, int i) {
        grid.add(new Label(s), 0, i);
        TextField t = new TextField(value);
        t.setPrefWidth(250);
        grid.add(t, 1, i);
        return t;
    }

    private VBox nuovoMultiCampo(GridPane grid, List<String> list, String value, int j) {
        List<TextField> field = new ArrayList<>();

        Label l = new Label(value);
        GridPane.setValignment(l, VPos.TOP);
        GridPane.setHalignment(l, HPos.LEFT);
        GridPane.setMargin(l, new Insets(2,0,0,0));
        grid.add(l, 0, j);

        VBox multiElenco = new VBox();
        multiElenco.setAlignment(Pos.CENTER);
        multiElenco.setSpacing(3);
        for(String s:list){
            field.add(new TextField(s));
            multiElenco.getChildren().add(field.get(field.size()-1));
        }
        grid.add(multiElenco, 1, j);

        return multiElenco;
    }

    private Button bottoneAggiungi(VBox multiElenco, boolean date){
        Button aggiungi = new Button("Aggiungi");
        aggiungi.setStyle("-fx-background-color: #00B6DB; -fx-text-fill: white; -fx-font-size:10;");
        GridPane.setValignment(aggiungi, VPos.BOTTOM);
        GridPane.setHalignment(aggiungi, HPos.RIGHT);
        GridPane.setMargin(aggiungi, new Insets(0,0,2,0));
        aggiungi.setOnAction(e -> {
            multiElenco.getChildren().add(new TextField());
            if(date)
                multiElenco.getChildren().add(new TextField());
        });
        return aggiungi;
    }

    private List<String> estraiMultiCampo(VBox multiElenco){
        List<String> list = new ArrayList<>();
        for(Node e: multiElenco.getChildren())
            if(e instanceof TextField t)
                if(!t.getText().equals(""))
                    list.add(t.getText());
        return list;
    }

    private void queryLavoro(boolean modifica, String text, List<String> mansioni, List<String> periodo, String... values) {
        titolo.setText(text+"\n\n\n");
        TextField t1 = nuovoCampo(grid, "Nome azienda*:", values[0], 0);
        TextField t2 = nuovoCampo(grid, "Luogo*:", values[1], 1);
        TextField t3 = nuovoCampo(grid, "Retribuzione*:", values[2], 2);

        VBox multiElenco1 = nuovoMultiCampo(grid, mansioni, "Mansioni*:", 3);
        grid.add(bottoneAggiungi(multiElenco1, false), 2, 3);
        VBox multiElenco2 = nuovoMultiCampo(grid, periodo, "Periodo*:", 4);

        conferma.setOnAction(e -> {
            try{
                List<String> newMansioni = estraiMultiCampo(multiElenco1);
                List<String> newPeriodi = estraiMultiCampo(multiElenco2);
                if(modifica)
                    database.setDaModificare(new lavoro(t1.getText(), t2.getText(), t3.getText(), newMansioni, newPeriodi));
                else
                    database.setDaAggiungere(new lavoro(t1.getText(), t2.getText(), t3.getText(), newMansioni, newPeriodi));
                cambiaScena("/view/interfacciaLavoratore.fxml", root);
            } catch (IllegalFieldException ex) {err.setText(ex.toString());}
            catch (Exception ex){ex.printStackTrace();}
        });
        grid.add(conferma, 0, 5);
        annulla(annulla, "/view/interfacciaLavoratore.fxml");
        grid.add(annulla, 0, 6);
    }

    private void queryContatto(boolean modifica, String text, String... values){
        titolo.setText(text+"\n\n\n");
        TextField t1 = nuovoCampo(grid, "Nome*:", values[0], 0);
        TextField t2 = nuovoCampo(grid, "Cognome*:", values[1], 1);
        TextField t3 = nuovoCampo(grid, "Email*:", values[2], 2);
        TextField t4 = nuovoCampo(grid, "Telefono*:", values[3], 3);

        conferma.setOnAction(e -> {
            try {
                if((database.getDaAggiungere() instanceof contattoICE) || (database.getDaAggiungere() == null)) {
                    if(modifica)
                        database.setDaModificare(new contattoICE(t1.getText(), t2.getText(), t3.getText(), t4.getText()));
                    else
                        database.setDaAggiungere(new contattoICE(t1.getText(), t2.getText(), t3.getText(), t4.getText()));
                    cambiaScena("/view/interfacciaLavoratore.fxml", root);
                } else {
                    ((lavoratore) database.getDaAggiungere()).setContattiICE(new contattoICE(t1.getText(), t2.getText(), t3.getText(), t4.getText()));
                    database.setDaModificare(null);
                    cambiaScena("/view/interfacciaLavoratore.fxml", root);
                }
            } catch (IllegalFieldException ex) {err.setText(ex.toString());
            } catch (Exception ex) {ex.printStackTrace();}
        });

        grid.add(conferma, 0, 4);
        if((database.getDaAggiungere() instanceof contattoICE) || (database.getDaAggiungere() == null))
            annulla(annulla, "/view/interfacciaLavoratore.fxml");
        else{
            annulla(annulla, "/view/elencoLavoratori.fxml");
            titolo.setText("Nuovo contatto in caso di emergenza\n\n\n");
        }

        grid.add(annulla, 0, 5);
    }

    private void queryLavoratore(Boolean modifica, String text, lavoratore lav, List<String> lingueParlate, List<String> zoneDisponibilita, List<String> periodiDisponibilita, String... values){
        titolo.setText(text+"\n\n\n");
        TextField t1 = nuovoCampo(grid, "Nome*:", values[0], 0);
        TextField t2 = nuovoCampo(grid, "Cognome*:", values[1], 1);
        TextField t3 = nuovoCampo(grid, "Email*:", values[2], 2);
        TextField t4 = nuovoCampo(grid, "Telefono:", values[3], 3);
        TextField t5 = nuovoCampo(grid, "Luogo di nascita*:", values[4], 4);
        TextField t6 = nuovoCampo(grid, "Data di nascita*:", values[5], 5);
        TextField t7 = nuovoCampo(grid, "Nazionalità*:", values[6], 6);
        TextField t8 = nuovoCampo(grid, "Indirizzo*:", values[7], 7);
        TextField t9 = nuovoCampo(grid, "Patente*", values[8], 8);
        TextField t10 = nuovoCampo(grid, "Automunito*:", values[9], 9);

        VBox multiElenco1 = nuovoMultiCampo(grid, lingueParlate, "Lingue parlate*:", 10);
        grid.add(bottoneAggiungi(multiElenco1, false), 2, 10);
        VBox multiElenco2 = nuovoMultiCampo(grid, zoneDisponibilita, "Zone disponibilità*:", 11);
        grid.add(bottoneAggiungi(multiElenco2, false), 2, 11);
        VBox multiElenco3 = nuovoMultiCampo(grid, periodiDisponibilita, "Periodi disponibilità*:", 12);
        grid.add(bottoneAggiungi(multiElenco3, true), 2, 12);

        conferma.setOnAction(e -> {
            try {
                List<String> newLingueParlate = estraiMultiCampo(multiElenco1);
                List<String> newZoneDisponibilita = estraiMultiCampo(multiElenco2);
                List<String> newPeriodiDisponibilita = estraiMultiCampo(multiElenco3);
                if (modifica){
                    database.setDaModificare(new lavoratore(t1.getText(), t2.getText(), t3.getText(), t4.getText(), t5.getText(), t6.getText(), t7.getText(), t8.getText(), t9.getText(), t10.getText(), newLingueParlate, newZoneDisponibilita, newPeriodiDisponibilita, lav.getContattiICE(), lav.getLavori()));
                cambiaScena("/view/interfacciaLavoratore.fxml", root);
                } else {
                    database.setDaAggiungere(new lavoratore(t1.getText(), t2.getText(), t3.getText(), t4.getText(), t5.getText(), t6.getText(), t7.getText(), t8.getText(), t9.getText(), t10.getText(), newLingueParlate, newZoneDisponibilita, newPeriodiDisponibilita, null, null));
                    database.setDaModificare(new contattoICE());
                    cambiaScena("/view/nuoviDati.fxml", root);
                }
            } catch (IllegalFieldException ex) {err.setText(ex.toString());
            } catch (Exception ex) {ex.printStackTrace();}
        });

        grid.add(conferma, 0, 13);
        if(modifica)
            annulla(annulla, "/view/interfacciaLavoratore.fxml");
        else
            annulla(annulla, "/view/elencoLavoratori.fxml");
        grid.add(annulla, 0, 14);
    }

    GridPane grid;
    Button conferma, annulla;
    Label titolo, err;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(3);
        grid.setHgap(5);
        Platform.runLater( () -> grid.requestFocus());

        titolo = new Label();
        titolo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        elenco.getChildren().add(titolo);
        err = new Label();
        err.setTextFill(Color.RED);

        conferma = new Button("Conferma");
        conferma.setStyle("-fx-background-color: #007FDB;  -fx-text-fill: white;");
        GridPane.setMargin(conferma, new Insets(20, 0, 0, 0));
        conferma.setPrefWidth(70);
        annulla = new Button("Annulla");
        annulla.setStyle("-fx-background-color: #FF2626;  -fx-text-fill: white;");
        annulla.setPrefWidth(conferma.getPrefWidth());

        if(database.getDaModificare() != null) {
            if(database.getDaModificare() instanceof lavoro l)
                queryLavoro(true, "Modifica lavoro", l.getMansioni(), l.getPeriodo(), l.getNomeAzienda(), l.getLuogo(), String.valueOf(l.getRetribuzione()));
            if(database.getDaModificare() instanceof contattoICE c)
                queryContatto(true, "Modifica contatto in caso di emergenza", c.getNome(), c.getCognome(),c.getEmail(),c.getTelefono());
            if(database.getDaModificare() instanceof lavoratore l)
              queryLavoratore(true, "Modifica anagrafiche", l, l.getLingueParlate(), l.getZoneDisponibilita(), l.getPeriodiDisponibilitaString(), l.getNome(), l.getCognome(), l.getEmail(),l.getTelefono(),l.getLuogoNascita(), l.getDataNascita(), l.getNazionalita(), l.getIndirizzo(),l.getPatente(),l.getAutomunito());
        } else {
            if(database.getDaAggiungere() instanceof lavoro)
                queryLavoro(false, "Nuovo lavoro", List.of(""), Arrays.asList("",""), "", "", "");
            if (database.getDaAggiungere() instanceof contattoICE)
                queryContatto(false, "Nuovo contatto in caso di emergenza","", "", "", "");
            if (database.getDaAggiungere() instanceof lavoratore l)
                queryLavoratore(false, "Nuovo lavoratore",l, List.of(""),List.of(""),Arrays.asList("",""), "", "", "","","","","","","","");
        }
        elenco.getChildren().add(grid);
        elenco.getChildren().add(err);
    }
}