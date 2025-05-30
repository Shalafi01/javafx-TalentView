package controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.lavoratore;
import model.ricerca;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;

import static model.DaoPattern.*;

public class elencoLavoratori extends Controller implements Initializable
{
    public VBox lista;
    public GridPane gridRicerca;
    public VBox root;

    private TextField nuovoCampoRicerca(String s, int i){
        TextField t;
        if(parametri[i] != null)
            t = new TextField(parametri[i]);
        else
            t = new TextField();
        t.setPrefWidth(140);
        t.setPromptText(s);
        return t;
    }

    //crea una mappa parametro-valore da usare come criterio della ricerca
    private static Map<String, String> nuoviParametri(String[] values){
        Map<String, String> parametriRicerca = new HashMap<>();
        parametriRicerca.put("nome", values[0]);
        parametriRicerca.put("cognome", values[1]);
        parametriRicerca.put("indirizzo", values[2]);
        parametriRicerca.put("lingueParlate", values[3]);
        parametriRicerca.put("mansioni", values[4]);
        parametriRicerca.put("patente", values[5]);
        parametriRicerca.put("automunito", values[6]);
        parametriRicerca.put("inizioPeriodo", values[7]);
        parametriRicerca.put("finePeriodo", values[8]);
        return parametriRicerca;
    }

    private void nuovoBottoneRicerca(String s, String metodoRicerca){
        Button btn = new Button(s);
        btn.setAlignment(Pos.CENTER);
        btn.setStyle("-fx-background-color: #008021;  -fx-text-fill: white;");
        btn.setPrefWidth(90);
        btn.setOnAction(e -> {
            int length = 0;
            for(Node ee:campi.getChildren()) {
                if(ee instanceof TextField t)
                    if(!t.getText().equals(""))
                        parametri[length] = t.getText();
                    else
                        parametri[length] = null;
                length++;
            }
            r = new ricerca(database.getLavoratori(), nuoviParametri(parametri),metodoRicerca);
            cambiaScena("/view/elencoLavoratori.fxml", root);
        });
        tipoRicerca.getChildren().add(btn);
    }

    GridPane campi;
    VBox tipoRicerca;
    Label err;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Platform.runLater( () -> lista.requestFocus());
        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setHgrow(Priority.ALWAYS);
        gridRicerca.getColumnConstraints().add(constraint);
        VBox.setMargin(gridRicerca, new Insets(0, 20, 0, 20));

        campi = new GridPane();
        campi.setAlignment(Pos.TOP_LEFT);
        campi.setHgap(2);
        campi.setVgap(2);
        campi.addRow(0, nuovoCampoRicerca("Nome", 0), nuovoCampoRicerca("Cognome", 1), nuovoCampoRicerca("Indirizzo", 2));
        campi.addRow(1, nuovoCampoRicerca("Lingua parlata", 3), nuovoCampoRicerca("Mansione svolta", 4), nuovoCampoRicerca("Tipo patente", 5));
        campi.addRow(2, nuovoCampoRicerca("Automunito", 6), nuovoCampoRicerca("Inizio disponibilità", 7), nuovoCampoRicerca("Fine disponibilità", 8));
        gridRicerca.add(campi, 0, 0);

        err = new Label();
        err.setStyle("-fx-text-fill: white;");
        VBox messaggi = new VBox(err);
        GridPane.setMargin(messaggi, new Insets(0, 0, 0, 2));
        gridRicerca.add(messaggi, 0, 1);

        tipoRicerca = new VBox();
        tipoRicerca.setSpacing(5);
        tipoRicerca.setAlignment(Pos.TOP_RIGHT);
        nuovoBottoneRicerca("Ricerca AND", "AND");
        nuovoBottoneRicerca("Ricerca OR", "OR");
        gridRicerca.add(tipoRicerca, 1, 0);
        Button nuovoLav = creaBottoneNuovo("Nuovo lavoratore", new lavoratore(), gridRicerca);
        nuovoLav.setAlignment(Pos.BOTTOM_RIGHT);
        gridRicerca.add(nuovoLav, 1,1);

        SortedSet<lavoratore> selezionati = database.getLavoratori();
        if(r != null)
            try{
                selezionati = r.applicaFiltri();
            } catch (Exception ex){err.setText(ex.toString());}
        
        for(lavoratore l:selezionati)
        {
            GridPane grid = new GridPane();
            grid.setMaxWidth(800);
            grid.getColumnConstraints().add(constraint);
            grid.setAlignment(Pos.CENTER);
            grid.setVgap(5);
            grid.setHgap(5);
            grid.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(2))));

            Label nome = new Label(l.getNome() + " " + l.getCognome());
            nome.setStyle("-fx-text-fill: #007FDB; -fx-font-weight:bold;");
            grid.add(nome, 0, 0);
            grid.add(new Label(l.getEmail()), 1, 0);
            grid.add(new Label(l.getTelefono()), 1, 1);
            grid.add(new Label("Indirizzo:\t\t\t"+l.getIndirizzo()), 0, 2);
            grid.add(new Label("Data di nascita:\t"+l.getDataNascita()), 0, 3);
            grid.add(new Label("Zone disponibilità:\t"+l.stampaZoneDisponibilita()), 0, 4);

            //inset (int top, int left, int bottom, int right)
            grid.setPadding(new Insets(10, 10, 10, 10));
            VBox.setMargin(grid, new Insets(0, 20, 0, 20));
            aggiungiGestione(l, null, grid, root, lista);


            //lista.getChildren().add(grid);
        }
    }
}
