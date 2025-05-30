package model;

import java.util.List;
import java.util.SortedSet;

public interface databaseDAO{
    void addDipendente(String nome, String cognome, String email, String telefono, String luogoNascita, String dataNascita, String nazionalita, String login, String password);
    void addLavoratore(String nome, String cognome, String email, String telefono, String luogoNascita, String dataNascita, String nazionalita, String indirizzo, String patente, String automunito, List<String> lingueParlate, List<String> zoneDisponibilita, List<String> periodiDisponibilita, SortedSet<contattoICE> contatti, SortedSet<lavoro> lavori);

    SortedSet<lavoratore> getLavoratori();
    SortedSet<dipendente> getDipendenti();
    lavoratore getSelected();
    void setSelected(lavoratore l);
    Object getDaModificare();
    Object getDaAggiungere();
    void setDaModificare(Object value);
    void setDaAggiungere(Object value);
}