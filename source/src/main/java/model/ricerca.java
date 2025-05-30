package model;

import java.text.SimpleDateFormat;
import java.util.*;

public class ricerca
{
    private final SortedSet<lavoratore> lavoratori;
    private final Map<String, String> parametriRicerca;
    private final String tipoRicerca;

    public ricerca(SortedSet<lavoratore> lavoratori, Map<String, String>  parametriRicerca, String tipoRicerca) {
        this.lavoratori = lavoratori;
        this.parametriRicerca = parametriRicerca;
        this.tipoRicerca = tipoRicerca;
    }

    public SortedSet<lavoratore> applicaFiltri()
    {
        //se tutti i parametri di ricerca sono null restituisce il Set lavoratori senza modificarlo
        if(!parametriRicerca.values().stream().allMatch(Objects::isNull))
        {
            if(parametriRicerca.get("automunito") != null)
                if((!parametriRicerca.get("automunito").equalsIgnoreCase("si"))&&(!parametriRicerca.get("automunito").equalsIgnoreCase("no")))
                    throw new IllegalFieldException("Valori validi per automunito: \"Si\", \"No\"");

            List<String> valoriPatente = Arrays.asList("NO", "M", "A", "B1", "B", "C1", "C", "D1", "D", "BE", "C1E", "CE", "D1E", "DE", "T", "F");
            if(parametriRicerca.get("patente") != null)
                if(!valoriPatente.contains(parametriRicerca.get("patente").toUpperCase()))
                    throw new IllegalFieldException("Valori validi per la patente: "+valoriPatente.toString());

            if(((parametriRicerca.get("inizioPeriodo") == null) && (parametriRicerca.get("finePeriodo") != null)) ||
                    ((parametriRicerca.get("inizioPeriodo") != null) && (parametriRicerca.get("finePeriodo") == null)))
                        throw new IllegalFieldException("Il periodo di disponibilità deve avere un inizio e una fine");

            //qui inserisce tutti i lavoratori che non soddisfano la query di ricerca
            SortedSet<lavoratore> daTenere = new TreeSet<>(new persona.comparatorePersone());
            for (lavoratore l : lavoratori)
            {
                //conta quanti parametri soddisfano le condizioni e quanti no per poter applicare AND o OR
                int[] controller = new int[]{0, 0};  //tenuti - scartati

                controller = containsIgnoreCase(parametriRicerca.get("nome"), l.getNome(), controller);
                controller = containsIgnoreCase(parametriRicerca.get("cognome"), l.getCognome(), controller);
                controller = containsIgnoreCase(parametriRicerca.get("patente"), l.getPatente(), controller);
                controller = containsIgnoreCase(parametriRicerca.get("automunito"), l.getAutomunito(), controller);
                controller = containsIgnoreCase(parametriRicerca.get("indirizzo"), l.getIndirizzo(), controller);

                if(parametriRicerca.get("lingueParlate") != null){
                    boolean c = false;
                    for(String s: l.getLingueParlate())
                        if(s.equalsIgnoreCase(parametriRicerca.get("lingueParlate")))
                            c = true;
                    controller = new int[]{controller[0], controller[1]+1};
                    if(c)
                        controller = new int[]{controller[0]+1, controller[1]-1};
                }

                //verifica che corrisponda almeno una mansione di un lavoro
                if (parametriRicerca.get("mansioni") != null) {
                    boolean c = false;
                    for (lavoro r : l.getLavori())
                        for(String s:r.getMansioni())
                            if (s.toLowerCase().contains(parametriRicerca.get("mansioni").toLowerCase()))
                                c = true;
                    controller = new int[]{controller[0], controller[1] + 1};
                    if (c)
                        controller = new int[]{controller[0] + 1, controller[1] - 1};
                }

                if ((parametriRicerca.get("inizioPeriodo") != null) && (parametriRicerca.get("finePeriodo") != null)){
                    boolean c = false;
                    try {
                        Date inizio = new SimpleDateFormat("dd/MM/yyyy").parse(parametriRicerca.get("inizioPeriodo"));
                        Date fine = new SimpleDateFormat("dd/MM/yyyy").parse(parametriRicerca.get("finePeriodo"));

                        if(inizio.after(fine))
                            throw new IllegalArgumentException();

                        for (int i = 0; i < l.getPeriodiDisponibilita().size(); i += 2)
                            if (inizio.after(l.getPeriodiDisponibilita().get(i)) || inizio.equals(l.getPeriodiDisponibilita().get(i)))
                                if (fine.before(l.getPeriodiDisponibilita().get(i + 1)) || fine.equals(l.getPeriodiDisponibilita().get(i + 1)))
                                    c = true;
                        controller = new int[]{controller[0], controller[1] + 1};
                        if (c)
                            controller = new int[]{controller[0] + 1, controller[1] - 1};
                    } catch (IllegalArgumentException e){throw new IllegalFieldException("La fine del periodo precede l'inizio");}
                    catch (Exception e){throw new IllegalFieldException("Il campo inizio periodo non è valido");}
                }

                //se si usa una ricerca AND il lavoratore va tenuto se tutte le condizioni sono vere
                if ((tipoRicerca.equals("AND")) && (controller[1] == 0))
                    daTenere.add(l);
                //se si usa una ricerca OR il lavoratore va tenuto se almeno una condizione è vera
                else if ((tipoRicerca.equals("OR")) && (controller[0] > 0))
                    daTenere.add(l);
            }
            return daTenere;
        }
        return lavoratori;
    }

    private int[] containsIgnoreCase(String value, String s, int[] controller)
    {
        if(value != null && s!= null){
            if(s.toLowerCase().contains(value.toLowerCase()))
                return new int[]{controller[0]+1, controller[1]};
            return new int[]{controller[0], controller[1]+1};
        }
        return controller;
    }
}