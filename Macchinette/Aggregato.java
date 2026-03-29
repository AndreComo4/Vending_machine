package Macchinette;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import Exceptions.CassaInsufficienteException;
import Exceptions.CreditoInsufficienteException;

/**
 * Rappresenta un insieme immutabile di monete, formalmente modellato come un <b>Multiset</b>.
 * <p>
 * A differenza di un semplice insieme, un aggregato tiene traccia della cardinalità
 * (quantità) di ciascun tipo di moneta, mappando ogni {@link Moneta} a un intero positivo.
 * Questa astrazione permette di modellare concetti concreti del dominio come il
 * "Fondo Cassa" di un distributore (insieme fisico di monete) o il "Credito" inserito
 * da un utente.
 * <p>
 * Essendo <b>immutabile</b>, qualsiasi operazione additiva o sottrattiva restituisce una
 * nuova istanza di {@code Aggregato} che differisce dall'{@code Aggregato} precedente, 
 * quindi non ne modifica lo stato.
 */
public class Aggregato implements Iterable<Moneta> {
    
    /** Struttura dati interna. Mappa definita da: Tipo Moneta -> Quantità */
    private final Map<Moneta, Integer> monete;

    /*-
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * Lo stato dell'oggetto è valido se e solo se sono rispettate le seguenti condizioni:
     *
     * 1. Integrità Strutturale:
     * - La mappa interna 'monete' non deve mai essere nulla.
     *
     * 2. Validità delle Chiavi (Tipi):
     * - Tutte le chiavi presenti nella mappa devono essere oggetti Moneta validi
     *   (non null e conformi alla loro Invariante di Rappresentazione).
     *
     * 3. Validità dei Valori (Quantità):
     * - Ogni valore nella mappa deve essere un intero strettamente positivo (> 0).
     *   Non ha senso logico memorizzare che si possiedono "0 monete" o quantità negative.
     * - Se una moneta non è presente, la sua chiave non deve esistere nella mappa.
     *
     * FUNZIONE DI ASTRAZIONE (AF):
     * Un'istanza di Aggregato rappresenta astrattamente un <b>Multinsieme (Multiset)</b>
     *
     * Formalmente, lo stato astratto A è definito come un insieme di coppie ordinate:
     *
     * A = { (m, q) | m ∈ Moneta, q ∈ N⁺ }
     *
     * Dove per ogni coppia (m, q) presente nell'aggregato:
     * - m rappresenta il tipo di moneta (valore facciale/taglio), corrispondente
     *   a una chiave presente nella mappa interna monete.
     * - q rappresenta la cardinalità (o molteplicità) di tale moneta, corrispondente
     *   al valore intero associato alla chiave m nella mappa.
     *
     * Interpretazione del dominio:
     * L'assenza di una moneta m dalla mappa implica che la sua cardinalità nel
     * multinsieme è 0 (q = 0).
     */

    /**
     * Costruisce un {@code Aggregato} vuoto.
     * <p>
     * Inizializza un contenitore senza alcuna moneta (valore totale 0).
     */
    public Aggregato() {
        this.monete = new TreeMap<>();
    }

    /**
     * Costruttore primario (interno) per la gestione dell'immutabilità.
     * <p>
     * Questo costruttore è il "cuore" della classe. Viene invocato internamente da tutti
     * i metodi che modificano lo stato (come {@code aggiungiMoneta} o {@code sottraiAggregato}).
     * Riceve una mappa calcolata, ne crea una copia "unmodifiable" e la incapsula.
     *
     * <p><b>Motivazione della visibilità privata:</b></p>
     * Poiché la mappa in ingresso viene copiata in una nuova {@code TreeMap} (garantendo l'ordinamento)
     * e poi resa unmodifiable, si impedisce che riferimenti esterni possano alterare lo stato
     * dell'aggregato dopo la costruzione.
     *
     * @param map La mappa sorgente contenente le associazioni Moneta-Quantità.
     */
    private Aggregato(Map<Moneta, Integer> map) {
        this.monete = Collections.unmodifiableMap(new TreeMap<>(map));
    }

    /**
     * Costruisce un Aggregato a partire da una rappresentazione in stringa.
     * <p>
     * Il formato atteso è una lista di coppie quantità-valore separate da virgola.
     * <br>Esempio: {@code "2 x 0.50, 1 x 1.00"}
     *
     * <p><b>Correttezza: </b></p>
     * <ul>
     * <li>La stringa può essere vuota (crea aggregato vuoto).</li>
     * <li>Se non vuota, deve rispettare il formato {@code N x Valore}.</li>
     * <li>Parti malformate vengono ignorate.</li>
     * </ul>
     *
     * @param s La stringa di configurazione.
     */
    public Aggregato(String s) {
        this(parseMap(s));
    }

    /**
     * Metodo ausiliario statico per il parsing della stringa di input.
     * <p>
     * Questo metodo puro converte una rappresentazione testuale nella struttura dati interna
     * necessaria per costruire un Aggregato.
     * 
     * Converte questa rappresentazione testuale in una mappa conforme alla struttura interna.
     * Filtra automaticamente input non validi per garantire che la mappa risultante
     * rispetti l'Invariante di Rappresentazione.
     * 
     * <p>
     * <b>Logica di Parsing:</b>
     * Il metodo scompone la stringa in segmenti (separati da virgola) e tenta di convertire
     * ciascuno in una coppia (Quantità, Moneta). Se un segmento risulta malformato
     * (es. sintassi errata, numeri non validi), viene <b>ignorato</b>,
     * garantendo che le parti valide vengano comunque caricate.
     *
     * @param s La stringa da parsare (può essere {@code null} o vuota).
     * @return Una mappa contenente le coppie Moneta-Quantità valide estratte.
     */
    private static Map<Moneta, Integer> parseMap(String s) {
        Map<Moneta, Integer> map = new TreeMap<>();
        if (s == null || s.trim().isEmpty()) return map;

        String[] pezzi = s.split(",");
        for (String pezzo : pezzi) {
            if (pezzo.trim().isEmpty()) continue;
            String[] dati = pezzo.trim().split("x"); 
            if (dati.length != 2) continue;
            
            try {
                int n = Integer.parseInt(dati[0].trim());
                String importoString = dati[1].trim();
                Importo val = new Importo(importoString); 
                Moneta m = new Moneta(val); 
                map.put(m, map.getOrDefault(m, 0) + n);
            } catch (Exception e) { continue; }
        }
        return map;
    }

    /**
     * Calcola il valore monetario totale contenuto nell'aggregato.
     * <p>
     * Esegue una somma pesata: \( \sum (ValoreMoneta_i \times Quantita_i) \).
     *
     * @return Oggetto {@link Importo} che rappresenta il totale.
     */
    public Importo getValoreTotale() {
        Importo totale = new Importo(0, 0);
        for (Map.Entry<Moneta, Integer> entry : monete.entrySet()) {
            Importo valoreMoneta = entry.getKey().getValore();
            int quantita = entry.getValue();
            totale = totale.add(valoreMoneta.multiply(quantita));
        }
        return totale;
    }

    /**
     * Restituisce la quantità di pezzi posseduti per un determinato taglio di moneta.
     *
     * @param m La moneta di cui verificare la quantità.
     * @return Il numero di monete presenti (0 se la moneta non è contenuta nell'aggregato).
     */
    public int getQuantita(Moneta m) {
        return monete.getOrDefault(m, 0);
    }

    /**
     * Restituisce un nuovo aggregato incrementando di 1 la quantità della moneta specificata.
     * <p>
     * Questo è un <b>metodo di convenienza</b> che delega
     * l'operazione al metodo {@link #aggiungiMonete(Moneta, int)} con quantità 1.
     * È ideale per modellare l'inserimento sequenziale di monete (es. un utente che inserisce
     * monete nella fessura).
     *
     * @param m La moneta da aggiungere.
     * @return Un nuovo oggetto {@code Aggregato} contenente la moneta aggiunta.
     * @throws NullPointerException se la moneta {@code m} è {@code null}.
     */
    public Aggregato aggiungiMoneta(Moneta m) {
        if (m == null) throw new NullPointerException("Impossibile aggiungere una moneta nulla");
        return aggiungiMonete(m, 1);
    }

    /**
     * Restituisce un nuovo aggregato incrementando di N unità la quantità della moneta specificata.
     * <p>
     * Questo metodo è utile per operazioni di caricamento massivo. 
     * Essendo la classe immutabile, viene creata
     * e restituita una nuova istanza con la mappa aggiornata.
     *
     * <p><b>Comportamento:</b></p>
     * <ul>
     * <li>Se {@code quantita <= 0}, non viene effettuata alcuna modifica e viene restituito
     * l'oggetto corrente ({@code this}).</li>
     * <li>Se la moneta è già presente, la nuova quantità viene sommata a quella esistente.</li>
     * </ul>
     *
     * @param m La moneta da aggiungere.
     * @param quantita Il numero di pezzi da aggiungere.
     * @return Un nuovo oggetto {@code Aggregato} con le quantità aggiornate.
     */
    public Aggregato aggiungiMonete(Moneta m, int quantita) {
        if (quantita <= 0) return this;

        Map<Moneta, Integer> nuovaMappa = new TreeMap<>(this.monete);
        nuovaMappa.put(m, nuovaMappa.getOrDefault(m, 0) + quantita);

        return new Aggregato(nuovaMappa);
    }

    /**
     * Unisce il contenuto di questo aggregato con un altro (Unione di Multiset).
     * <p>
     * L'operazione esegue una somma vettoriale delle quantità: per ogni taglio di moneta,
     * la quantità nel nuovo aggregato sarà la somma delle quantità presenti nei due operandi.
     * <br>Utile per operazioni come l'incasso del credito utente nel fondo cassa.
     *
     * <p><b>Correttezza: </b></p>
     * <ul>
     * <li>Verifica che l'aggregato da sommare non sia {@code null}.</li>
     * </ul>
     *
     * @param altroAggregato L'aggregato da sommare a questo.
     * @return Un nuovo {@code Aggregato} contenente tutte le monete di entrambi.
     * @throws NullPointerException se {@code altroAggregato} è {@code null}.
     */
    public Aggregato sommAggregato(Aggregato altroAggregato) {
        if (altroAggregato == null) throw new NullPointerException("L'aggregato da aggiungere non può essere nullo");

        Map<Moneta, Integer> nuovaMappa = new TreeMap<>(this.monete);

        for (Moneta key : altroAggregato.monete.keySet()) {
            int qtyDaAggiungere = altroAggregato.monete.get(key);
            int qtyAttuale = nuovaMappa.getOrDefault(key, 0);
            nuovaMappa.put(key, qtyAttuale + qtyDaAggiungere);
        }

        return new Aggregato(nuovaMappa);
    }

    /**
     * Crea un nuovo aggregato sottraendo il contenuto esatto di un secondo aggregato.
     * <p>
     * Questa operazione modella la rimozione fisica di monete (es. erogazione del resto).
     * Verifica rigorosamente la disponibilità fisica dei pezzi.
     *
     * <p><b>Correttezza:</b></p>
     * L'operazione riesce se e solo se:
     * <ul>
     * <li>Il valore totale posseduto è maggiore o uguale a quello da sottrarre.</li>
     * <li>Per ogni taglio richiesto in {@code altroAggregato}, questo aggregato ne possiede
     * una quantità sufficiente ({@code qtyPosseduta >= qtyRichiesta}).</li>
     * </ul>
     *
     * <p><b>Preservazione dell'Invariante:</b></p>
     * Se la sottrazione porta la quantità di una moneta esattamente a zero, la chiave viene
     * rimossa dalla mappa risultante, rispettando la condizione RI che vieta valori nulli o non positivi.
     *
     * @param altroAggregato L'aggregato che rappresenta le monete da rimuovere.
     * @return Un nuovo {@code Aggregato} rappresentante la differenza.
     * @throws NullPointerException se {@code altroAggregato} è {@code null}.
     * @throws CreditoInsufficienteException se il valore totale economico non è sufficiente.
     * @throws CassaInsufficienteException se il valore totale è sufficiente, ma mancano i pezzi fisici specifici
     * (es. richiesto resto di 50 cent, ma ho solo 2 pezzi da 20 cent).
     */
    public Aggregato sottraiAggregato(Aggregato altroAggregato) throws CreditoInsufficienteException, CassaInsufficienteException {
        if (altroAggregato == null) throw new NullPointerException("L'aggregato da sottrarre non può essere nullo");

        long mioTotaleMonete = this.getValoreTotale().toTotalCents();
        long altroTotaleMonete = altroAggregato.getValoreTotale().toTotalCents();

        if (mioTotaleMonete < altroTotaleMonete) {
            throw new CreditoInsufficienteException();
        } 

        Map<Moneta, Integer> nuovaMappa = new TreeMap<>(this.monete);

        for (Moneta key : altroAggregato.monete.keySet()) {
            
            int qtyDaRimuovere = altroAggregato.monete.get(key);
            int qtyChePossiedo = nuovaMappa.getOrDefault(key, 0);

            if (qtyChePossiedo < qtyDaRimuovere) {
                throw new CassaInsufficienteException();
            }
            
            int nuovaQty = qtyChePossiedo - qtyDaRimuovere;

            if (nuovaQty == 0) {
                nuovaMappa.remove(key);
            } else {
                nuovaMappa.put(key, nuovaQty);
            }
        }

        return new Aggregato(nuovaMappa);
    }

    /**
     * Restituisce un iteratore sui tipi di monete presenti nell'aggregato.
     * <p>
     * L'iteratore scorre le chiavi della mappa interna (gli oggetti {@link Moneta}).
     * <br><b>Ordinamento:</b> Grazie all'uso interno di {@code TreeMap}, l'ordine di
     * iterazione è garantito essere <b>crescente per valore</b> (dal taglio più piccolo al più grande).
     *
     * @return Un iteratore tipizzato su {@code Moneta}.
     */
    @Override
    public Iterator<Moneta> iterator() {
        return monete.keySet().iterator();
    }

    /**
     * Restituisce una rappresentazione testuale leggibile dell'aggregato.
     * <p>
     * Il formato elenca le monete presenti con le rispettive quantità.
     * <br>Formato: {@code <Quantità x Valore, Quantità x Valore, ...>}
     * <br>Esempio: {@code <2 x 0.50, 1 x 1.00>}
     *
     * @return La stringa descrittiva dell'oggetto.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<");
        boolean first = true;
        for (Moneta key : monete.keySet()) {
            if (!first) {
                sb.append(", ");
            }
            int quantita = monete.get(key);
            sb.append(quantita).append(" x ").append(key);  
            first = false;
        }
        sb.append(">");
        return sb.toString();
    }

    /**
     * Calcola l'hash code dell'aggregato.
     * <p>
     * L'hash code è generato basandosi sul contenuto della mappa interna.
     * Due aggregati con le stesse monete e le stesse quantità avranno lo stesso hash code.
     *
     * @return Il valore hash calcolato.
     */
    @Override
    public int hashCode() {
        return Objects.hash(monete);
    }

    /**
     * Verifica l'uguaglianza logica tra questo aggregato e un altro oggetto.
     * <p>
     * Due oggetti {@code Aggregato} sono considerati uguali se e solo se:
     * <ul>
     * <li>Sono della stessa classe.</li>
     * <li>Contengono esattamente gli stessi tipi di monete.</li>
     * <li>Per ogni tipo di moneta, la quantità posseduta è identica.</li>
     * </ul>
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se gli aggregati sono identici nel contenuto, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Aggregato other)) return false;
        return Objects.equals(this.monete, other.monete);
    }

}
