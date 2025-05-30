package model;

import java.util.Comparator;
import java.util.regex.Pattern;

public abstract class persona {
    private final String nome, cognome, email, telefono;

    public persona(String nome, String cognome, String email, String telefono) {
        //richiede che il campo non sia vuoto o lancia un errore
        this.nome = campoObbligatorio(nome, "Il campo nome non è valido");
        this.cognome = campoObbligatorio(cognome, "Il campo cognome non è valido");

        //richiede che il campo rispetti il pattern o lancia un errore
        this.email = checkPattern("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", email, "Il campo email non è valido");
        if(telefono.equals(""))
            this.telefono=telefono;
        else
            this.telefono = checkPattern("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$", telefono, "Il campo telefono non è valido");
    }

    public persona(){
        this.nome="";
        this.cognome="";
        this.email="";
        this.telefono="";
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String campoObbligatorio(String value, String err){
        if(value.equals(""))
            throw new IllegalFieldException(err);
        return value;
    }

    private String checkPattern(String pattern, String value, String err){
        Pattern p = Pattern.compile(pattern);
        if (!p.matcher(value).matches())
            throw new IllegalFieldException(err);
        return value;
    }

    //Ordina prima per cognome poi per nome
    static class comparatorePersone implements Comparator<persona> {
        @Override
        public int compare(persona p1, persona p2) {
            int diff = p1.getCognome().compareTo(p2.getCognome());
            if (diff == 0)
                diff = p1.getNome().compareTo(p2.getNome());
            if (diff == 0)
                diff = p1.getEmail().compareTo(p2.getEmail());
            if (diff == 0)
                diff = p1.getTelefono().compareTo(p2.getTelefono());
            return diff;
        }
    }

    @Override
    public String toString() {
        return "nome: " + nome +
                "\ncognome: " + cognome +
                "\nemail: " + email +
                "\ntelefono: " + telefono;
    }
}
