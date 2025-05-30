package model;

import java.text.SimpleDateFormat;
import java.util.*;

public class lavoro {

    private List<Date> periodo = new ArrayList<>();
    private String nomeAzienda, luogo;
    private List<String> mansioni;
    private double retribuzione;

    public lavoro(String nomeAzienda, String luogo, String retribuzione, List<String> mansioni, List<String> periodo) {
        this.nomeAzienda = campoObbligatorio(nomeAzienda, "Il campo nome azienda non è valido");
        this.luogo  = campoObbligatorio(luogo, "Il campo luogo non è valido");

        //prova a convertire da String a Double, se da errore lancia un eccezione IllegalField
        try{
            if(retribuzione.equals(""))
                throw new IllegalArgumentException();
            this.retribuzione = Double.parseDouble(retribuzione);
        }catch(Exception e) {throw new IllegalFieldException("Il campo retribuzione non è valido");}

        if(mansioni.size()>0)
            this.mansioni = mansioni;
        else
            throw new IllegalFieldException("Il campo mansioni non è valido");

        if ((periodo.isEmpty()) || (periodo.size() % 2 != 0))
            throw new IllegalFieldException("Il campo periodo non è valido");
        try {
            for (String s : periodo)
                this.periodo.add(new SimpleDateFormat("dd/MM/yyyy").parse(s));
        } catch (Exception e) {
            throw new IllegalFieldException("Il campo periodo non è valido");
        }

        Calendar c = Calendar.getInstance();
        for (int i = 0; i < this.periodo.size(); i += 2)
            if (this.periodo.get(i).after(this.periodo.get(i + 1)))
                throw new IllegalFieldException("Il valore di inizio non può essere successivo a quello di fine");

        for (int i = 0; i < this.periodo.size(); i += 2) {
            c.setTime(this.periodo.get(i));
            if (c.get(Calendar.YEAR) < 2017)
                throw new IllegalFieldException("Il periodo deve risalire agli ultimi 5 anni");
            if (this.periodo.get(i + 1).after(new Date()))
                throw new IllegalFieldException("Questo lavoro non è ancora terminato");
        }
    }

    public lavoro(){}

    public List<String> getMansioni() {
        return mansioni;
    }

    public String stampaMansioni()
    {
        String result="";
        for(String s:mansioni)
            result+=s+", ";
        return result.substring(0, result.length()-2);
    }

    public String stampaPeriodo() {
        return new SimpleDateFormat("dd/MM/yyyy").format(periodo.get(0))+" - "+new SimpleDateFormat("dd/MM/yyyy").format(periodo.get(1));
    }

    public List<String> getPeriodo() {
        List<String> result = new ArrayList<>();
        for (Date d : periodo)
            result.add(new SimpleDateFormat("dd/MM/yyyy").format(d));
        return result;
    }

    public String getNomeAzienda() {
        return nomeAzienda;
    }

    public String getLuogo() {
        return luogo;
    }

    public double getRetribuzione() {
        return retribuzione;
    }

    private String campoObbligatorio(String value, String err){
        if(value.equals(""))
            throw new IllegalFieldException(err);
        return value;
    }

    //ordina i lavori
    static class comparatoreLavori implements Comparator<lavoro> {
        @Override
        public int compare(lavoro l1, lavoro l2) {
            int diff = l1.nomeAzienda.compareTo(l2.nomeAzienda);
            if(diff==0)
                diff=l1.luogo.compareTo(l2.luogo);
            if(diff==0)
                diff= (int)(l1.retribuzione - l2.retribuzione);
            if(diff==0)
                if(l1.mansioni.equals(l2.mansioni))
                    diff = 0;
                else diff=1;
            if(diff==0)
                if(l1.periodo.equals(l2.periodo))
                    diff = 0;
                else diff=1;
            return diff;
        }
    };

    @Override
    public String toString() {
        return "\nnome azienda: " + nomeAzienda +
                "\nluogo: " + luogo +
                "\nretribuzione: " + retribuzione +
                "\nmansione: "+mansioni+
                "\nperiodo: "+ new SimpleDateFormat("dd/MM/yyyy").format(periodo.get(0))+
                " - "+ new SimpleDateFormat("dd/MM/yyyy").format(periodo.get(1));
    }
}
