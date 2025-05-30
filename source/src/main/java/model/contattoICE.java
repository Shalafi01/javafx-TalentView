package model;

public class contattoICE extends persona{

    public contattoICE(String nome, String cognome, String email, String telefono) {
        super(nome, cognome, email, telefono);
        //persona non richiede che il telefono sia obbligatorio, ma contattoICE sì
        if(telefono.equals(""))
            throw new IllegalFieldException("Il campo telefono non è valido");
    }

    public contattoICE(){
        super();
    }
}