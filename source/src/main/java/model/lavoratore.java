package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class lavoratore extends persona{

    private String luogoNascita, nazionalita, indirizzo, patente, automunito;
    private Date dataNascita;
    private List<Date> periodiDisponibilita = new ArrayList<>();
    private List<String> lingueParlate, zoneDisponibilita;
    private SortedSet<lavoro> lavori;
    private SortedSet<contattoICE> contattiICE;

    public lavoratore(String nome, String cognome, String email, String telefono, String luogoNascita, String dataNascita,
                      String nazionalita, String indirizzo, String patente, String automunito,
                      List<String> lingueParlate, List<String> zoneDisponibilita, List<String> periodiDisponibilita,
                      SortedSet<contattoICE> contattiICE, SortedSet<lavoro> lavori) throws ParseException
    {
        super(nome, cognome, email, telefono);
        this.luogoNascita = campoObbligatorio(luogoNascita, "Il campo luogo di nascita non è valido");
        this.nazionalita = campoObbligatorio(nazionalita, "Il campo nazionalità non è valido");
        this.indirizzo = campoObbligatorio(indirizzo, "Il campo indirizzo non è valido");

        this.patente = valoreValido(patente, "patente", "No", "M", "A", "B1", "B", "C1", "C", "D1", "D", "BE", "C1E", "CE", "D1E", "DE", "T", "F");
        this.automunito = valoreValido(automunito, "automunito", "Si", "No");

        this.lingueParlate = rimuoviDuplicati(lingueParlate);
        this.zoneDisponibilita  = rimuoviDuplicati(zoneDisponibilita);
        this.contattiICE = contattiICE;
        this.lavori = Objects.requireNonNullElseGet(lavori, () -> new TreeSet<>(new lavoro.comparatoreLavori()));

        if((periodiDisponibilita.size()%2 != 0) || (periodiDisponibilita.size() == 0))
            throw new IllegalFieldException("Il periodo di disponibilità deve avere un inizio e una fine");

        //rimuove i duplicati
        Set<List<String>> set = new HashSet<>();
        for(int i=0; i<periodiDisponibilita.size(); i=i+2) {
            List<String> list = new ArrayList<>();
            list.add(periodiDisponibilita.get(i));
            list.add(periodiDisponibilita.get(i+1));
            set.add(list);
        }
        periodiDisponibilita.clear();
        for(List<String> l:set)
            periodiDisponibilita.addAll(l);

        for (String s : periodiDisponibilita)
            this.periodiDisponibilita.add(dataValida(s, "Il campo periodi disponibilità non è valido"));
        for(int i=0;i< this.periodiDisponibilita.size();i+=2){
            if(this.periodiDisponibilita.get(i).after(this.periodiDisponibilita.get(i+1)))
                throw new IllegalFieldException("Il valore di inizio non può essere successivo a quello di fine");
            if(this.periodiDisponibilita.get(i+1).after(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2100")))
                throw new IllegalFieldException("Il periodo di disponibilità è troppo lungo");
        }

        this.dataNascita = dataValida(dataNascita, "Il campo data di nascita non è valido");
        if(this.dataNascita.after(new Date()))
            throw new IllegalFieldException("Questo lavoratore non è ancora nato");
    }

    public lavoratore(){}

    public SortedSet<contattoICE> getContattiICE() {
        return contattiICE;
    }

    public void setContattiICE(contattoICE c) {
        this.contattiICE = new TreeSet<>(new persona.comparatorePersone());
        this.contattiICE.add(c);
    }

    public SortedSet<lavoro> getLavori() {
        return lavori;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getPatente() {
        return patente;
    }

    public String getAutomunito() {
        return automunito;
    }

    public List<String> getLingueParlate() {
        return lingueParlate;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public List<String> getPeriodiDisponibilitaString() {
        List<String> result = new ArrayList<>();
        for (Date d : periodiDisponibilita)
            result.add(new SimpleDateFormat("dd/MM/yyyy").format(d));
        return result;
    }

    public List<Date> getPeriodiDisponibilita(){return periodiDisponibilita;}

    public List<String> getZoneDisponibilita() {return zoneDisponibilita;}

    public String getDataNascita() {return new SimpleDateFormat("dd/MM/yyyy").format(dataNascita);}

    public String stampaLingueParlate() {
        String result="";
        for(String s:lingueParlate)
            result += s + ", ";
        return result.substring(0, result.length()-2);
    }

    public String stampaPeriodiDisponibilita() {
        String result="";
        for(int i=0; i<periodiDisponibilita.size(); i++)
        {
            String s = new SimpleDateFormat("dd/MM/yyyy").format(periodiDisponibilita.get(i));
            result += s;
            if(i%2==0)
                result+= " - ";
            else
                result+= "\n\t\t\t\t\t";
        }
        return result.substring(0, result.length()-2);
    }

    public String stampaZoneDisponibilita() {
        String result="";
        for(String s:zoneDisponibilita)
            result+=s+", ";
        return result.substring(0, result.length()-2);
    }

    public Date dataValida(String value, String err){
        Date d;
        try{
            d = new SimpleDateFormat("dd/MM/yyyy").parse(value);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            if(c.get(Calendar.YEAR) < 1900)
                throw new IllegalArgumentException();
        }catch(Exception e) {
            throw new IllegalFieldException(err);
        }
        return d;
    }

    private String valoreValido(String value, String err, String... values){
        Set<String> vv = new HashSet<>(Arrays.asList(values));
        boolean c=false;
        for(String s:vv)
            if(s.toLowerCase().contains(value.toLowerCase()))
                c=true;
        if(c)
            return value.toUpperCase().charAt(0) + value.substring(1);
        else
            throw new IllegalFieldException("I valori validi per "+err+" sono: "+Arrays.toString(values));

    }

    private List<String> rimuoviDuplicati(List<String> list){
        Set<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    @Override
    public String toString() {
        return super.toString()+
                "\nluogo di nascita: "+luogoNascita+
                "\ndata di nascita: "+new SimpleDateFormat("dd/MM/yyyy").format(dataNascita) +
                "\nnazionalita: "+nazionalita+
                "\nindirizzo: "+indirizzo+
                "\npatente: "+patente+
                "\nautomunito: "+automunito+
                "\nlingue parlate: "+lingueParlate+
                "\nzone disponibilita: "+zoneDisponibilita+
                "\nperiodi disponibilita: "+periodiDisponibilita+
                "\n\n===========================================\n";
    }
}