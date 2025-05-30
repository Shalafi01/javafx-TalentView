package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class dipendente extends persona{

    private final String luogoNascita, nazionalita, login, password;
    private final Date dataNascita;

    public dipendente(String nome, String cognome, String email, String telefono, String luogoNascita, String dataNascita, String nazionalita, String login, String password) {
        super(nome, cognome, email, telefono);
        //persona non richiede che il telefono sia obbligatorio, ma contattoICE sì
        if(telefono.equals(""))
            throw new IllegalFieldException("Il campo telefono non è valido");
        this.luogoNascita = luogoNascita;
        this.nazionalita = nazionalita;
        this.login = login;
        this.password = password;

        //prova a convertire da String a Date, se da errore lancia un'eccezione IllegalField
        try{
            this.dataNascita = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascita);
        }catch(Exception e) {
            throw new IllegalFieldException("Il campo data di nascita non è valido");
        }
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return super.toString()+
                "\nluogo di nascita: "+luogoNascita+
                "\ndata di nascita: "+new SimpleDateFormat("dd/MM/yyyy").format(dataNascita) +
                "\nnazionalita: "+nazionalita+
                "\nlogin: "+login+
                "\npassword: "+password+"\n\n===========================================\n";
    }
}
