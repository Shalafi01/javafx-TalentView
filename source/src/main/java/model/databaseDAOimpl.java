package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;

public class databaseDAOimpl implements databaseDAO
{
    private final SortedSet<lavoratore> lavoratori = new TreeSet(new persona.comparatorePersone());
    private final SortedSet<dipendente> dipendenti = new TreeSet(new persona.comparatorePersone());
    private final String percorso = "database.json";

    private lavoratore selected;
    private Object daModificare=null, daAggiungere=null;

    public databaseDAOimpl() {
        importDipendenti();
        importLavoratori();
    }

    public void addDipendente(String nome, String cognome, String email, String telefono, String luogoNascita, String dataNascita, String nazionalita, String login, String password){
        try{
            dipendente d = new dipendente(nome, cognome, email, telefono, luogoNascita, dataNascita, nazionalita, login, password);
            dipendenti.add(d);
        }catch (Exception e){e.printStackTrace();}
    }

    private void importDipendenti()
    {
        try {
            JSONParser parser = new JSONParser();                                       //permette lettura di dati JSON sotto forma di Stream
            JSONObject object = (JSONObject) parser.parse(new FileReader(percorso));    //importa come JSON Object l'intero contenuto del file
            JSONArray dipendentiJSON = (JSONArray)object.get("dipendenti");             //Crea un JSON Array per il campo dipendenti
            String[][] dati = new String[dipendentiJSON.size()][9];                     //Crea una matrice in cui importare i dati e instanziare successivamente un oggetto dipendente
            for (int i = 0; i < dipendentiJSON.size(); i++) {
                JSONObject jsonObject = (JSONObject) dipendentiJSON.get(i);                      //importa dati per ogni dipendente
                dati[i][0] = (String) jsonObject.get("nome");
                dati[i][1] = (String) jsonObject.get("cognome");
                dati[i][2] = (String) jsonObject.get("email");
                dati[i][3] = (String) jsonObject.get("telefono");
                dati[i][4] = (String) jsonObject.get("luogoNascita");
                dati[i][5] = (String) jsonObject.get("dataNascita");
                dati[i][6] = (String) jsonObject.get("nazionalita");
                dati[i][7] = (String) jsonObject.get("login");
                dati[i][8] = (String) jsonObject.get("password");
                addDipendente(dati[i][0], dati[i][1], dati[i][2], dati[i][3], dati[i][4], dati[i][5], dati[i][6], dati[i][7], dati[i][8]);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void addLavoratore(String nome, String cognome, String email, String telefono, String luogoNascita, String dataNascita, String nazionalita, String indirizzo, String patente, String automunito, List<String> lingueParlate, List<String> zoneDisponibilita, List<String> periodiDisponibilita, SortedSet<contattoICE> contatti, SortedSet<lavoro> lavori)
    {
        try{
            lavoratore l = new lavoratore(nome, cognome, email, telefono, luogoNascita, dataNascita, nazionalita, indirizzo, patente, automunito, lingueParlate, zoneDisponibilita, periodiDisponibilita, contatti, lavori);
            lavoratori.add(l);
        }catch (Exception e){e.printStackTrace();}
    }

    private void importLavoratori()
    {
        try {
            JSONParser parser = new JSONParser();                                       //permette lettura di dati JSON sotto forma di Stream
            JSONObject object = (JSONObject) parser.parse(new FileReader(percorso));    //importa come JSON Object l'intero contenuto del file
            JSONArray lavoratoriJSON = (JSONArray)object.get("lavoratori");             //Crea un JSON Array per il campo dipendenti
            String[][] dati = new String[lavoratoriJSON.size()][10];                    //Crea una matrice in cui importare i dati e istanziare successivamente un oggetto dipendente
            for (int i = 0; i < lavoratoriJSON.size(); i++)
            {
                //importa dati per ogni dipendente
                JSONObject Lavoratore = (JSONObject) lavoratoriJSON.get(i);
                dati[i][0] = (String) Lavoratore.get("nome");
                dati[i][1] = (String) Lavoratore.get("cognome");
                dati[i][2] = (String) Lavoratore.get("email");
                dati[i][3] = (String) Lavoratore.get("telefono");
                dati[i][4] = (String) Lavoratore.get("luogoNascita");
                dati[i][5] = (String) Lavoratore.get("dataNascita");
                dati[i][6] = (String) Lavoratore.get("nazionalita");
                dati[i][7] = (String) Lavoratore.get("indirizzo");
                dati[i][8] = (String) Lavoratore.get("patente");
                dati[i][9] = (String) Lavoratore.get("automunito");

                //Estrae gli array finché iterator trova dei record
                List<String> lingue = estraiStringArray(((JSONArray)Lavoratore.get("lingueParlate")).iterator());
                List<String> zone = estraiStringArray(((JSONArray)Lavoratore.get("zoneDisponibilita")).iterator());
                List<String> periodi = estraiStringArray(((JSONArray)Lavoratore.get("periodiDisponibilita")).iterator());

                SortedSet<contattoICE> contatti = new TreeSet<>(new persona.comparatorePersone());
                JSONArray contattiJSON = (JSONArray) Lavoratore.get("contattiICE");     //Crea un JSON Array per il tutti i contatti d'emergenza
                for (Object o : contattiJSON) {
                    JSONObject contatto = (JSONObject) o;                               //crea un oggetto contatto per ogni contatto
                    String[] datiContatto = new String[4];                              //crea un array per memorizzare i dati importati
                    datiContatto[0] = (String) contatto.get("nome");
                    datiContatto[1] = (String) contatto.get("cognome");
                    datiContatto[2] = (String) contatto.get("email");
                    datiContatto[3] = (String) contatto.get("telefono");
                    contatti.add(addContattoICE(datiContatto));                       //tenta di creare un nuovo contatto con i dati estratti e di
                }                                                                       //aggiungerlo al set locale "contatti" per poter instanziare l'oggetto lavoratore

                SortedSet<lavoro> lavori = new TreeSet<>(new lavoro.comparatoreLavori());
                JSONArray lavoriJSON = (JSONArray) Lavoratore.get("lavori");            //Crea un JSON Array per il tutti i lavori
                for (Object o : lavoriJSON) {
                    JSONObject lavoro = (JSONObject) o;                                 //crea un oggetto lavoro per ogni lavoro
                    String[] datiLavoro = new String[4];                                //crea un array per memorizzare i dati importati
                    datiLavoro[0] = (String) lavoro.get("nomeAzienda");
                    datiLavoro[1] = (String) lavoro.get("luogo");
                    datiLavoro[2] = (String) lavoro.get("retribuzione");

                    List<String> periodo = estraiStringArray(((JSONArray)lavoro.get("periodo")).iterator());            //estrae gli array con metodo precedente
                    List<String> mansioni = estraiStringArray(((JSONArray)lavoro.get("mansioni")).iterator());

                    lavori.add(addLavoro(datiLavoro, periodo, mansioni));             //tenta di creare un nuovo contatto con i dati estratti e di
                }                                                                       //aggiungerlo al set locale "lavori" per poter instanziare l'oggetto lavoratore
                //crea un nuovo dipendente con i dati estratti
                addLavoratore(dati[i][0], dati[i][1], dati[i][2], dati[i][3], dati[i][4], dati[i][5], dati[i][6], dati[i][7], dati[i][8], dati[i][9], lingue, zone, periodi, contatti, lavori);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    //funzione di supporto, estrae array di tipo String
    private List<String> estraiStringArray(Iterator iterator){
        List<String> list = new ArrayList<>();
        while (iterator.hasNext())
            list.add(iterator.next().toString());
        return list;
    }

    private contattoICE addContattoICE(String[] datiContatto){
        //prova a creare un nuovo contattoICE con i dati forniti
        try{
            return new contattoICE(datiContatto[0], datiContatto[1], datiContatto[2], datiContatto[3]);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    private lavoro addLavoro(String[] datiLavoro, List<String> periodi, List<String> mansioni){
        //prova a creare un nuovo lavoro con i dati forniti
        try{
            return new lavoro(datiLavoro[0], datiLavoro[1], datiLavoro[2], mansioni, periodi);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    @Override
    public SortedSet<lavoratore> getLavoratori() {
        return lavoratori;
    }

    @Override
    public SortedSet<dipendente> getDipendenti() {
        return dipendenti;
    }

    @Override
    public lavoratore getSelected() {
        return selected;
    }

    @Override
    public void setSelected(lavoratore l) {
        selected = l;
    }

    @Override
    public Object getDaModificare() {
        return daModificare;
    }

    @Override
    public Object getDaAggiungere() {
        return daAggiungere;
    }

    @Override
    public void setDaModificare(Object value) {
        daModificare = value;
    }

    @Override
    public void setDaAggiungere(Object value) {
        daAggiungere = value;
    }
}
