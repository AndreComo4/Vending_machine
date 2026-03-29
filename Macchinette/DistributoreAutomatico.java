package Macchinette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Exceptions.BinarioNonEsistenteException;
import Exceptions.BinarioVuotoException;
import Exceptions.CassaInsufficienteException;
import Exceptions.CreditoInsufficienteException;
import Exceptions.MoneteMancantiException;

/**
 * Rappresenta il nucleo logico e fisico di un Distributore Automatico.
 * <p>
 * Questa classe agisce come <i>facciata</i> del sistema, coordinando:
 * <ul>
 * <li><b>Inventario:</b> Gestione dei binari (colonne) contenenti i prodotti.</li>
 * <li><b>Economia:</b> Gestione del fondo cassa (amministrativo) e del credito corrente (utente).</li>
 * <li><b>Logica di Vendita:</b> Orchestrazione delle transazioni e calcolo del resto (tramite Strategy).</li>
 * </ul>
 * La classe è <b>mutabile</b> in quanto il suo stato evolve con le operazioni di vendita e ricarica.
 */
public class DistributoreAutomatico {
    
    /** L'inventario fisico: una lista di colonne (binari) contenenti i prodotti. */
    private final List<Binario> binari;

    /** Il "tesoro" del distributore: tutte le monete accumulate dalle vendite precedenti. */
    private Aggregato fondoCassa;

    /** Le monete inserite dall'utente per la transazione corrente (non ancora incassate). */
    private Aggregato credito;

    /** Il "cervello" che decide come erogare il resto. */
    private final StrategiaResto strategia;

    /*-
     *
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * Lo stato del Distributore è valido se e solo se sono soddisfatte le seguenti condizioni:
     *
     * 1. Validità Strutturale (Inventario):
     * - La lista 'binari' non deve essere nulla.
     * - La lista deve contenere almeno un elemento (non esiste un distributore senza slot).
     * - Nessun elemento all'interno della lista può essere nullo (integrità della sequenza).
     *
     * 2. Validità Economica (Stato Finanziario):
     * - 'fondoCassa' non deve essere nullo (anche se vuoto/0€, l'oggetto contenitore deve esistere).
     * - 'credito' non deve essere nullo (anche se 0€, l'oggetto contenitore deve esistere).
     *
     * 3. Validità Comportamentale (Strategia):
     * - 'strategia' non deve essere nulla. Il distributore deve sempre possedere
     *    una regola definita per il calcolo del resto.
     * 
     * FUNZIONE DI ASTRAZIONE (AF):
     * AF(binari, fondoCassa, credito, strategia):
     * Un'istanza di questa classe rappresenta un Distributore Automatico fisico funzionante:
     * - 'binari': Rappresenta l'inventario fisico ordinato degli slot.
     *    La lista mappa posizioni logiche a colonne fisiche: l'elemento all'indice k
     *    corrisponde al "Binario n.(k+1)" selezionabile dall'utente.
     * - 'fondoCassa': Rappresenta il "serbatoio" interno di monete (Safe/Hopper),
     *    costituito dall'accumulo delle vendite passate e disponibile per i resti.
     * - 'credito': Rappresenta il valore temporaneo inserito dall'utente per la transazione
     *    in corso (le monete attualmente nel "limbo", non ancora incassate né restituite).
     * - 'strategia': Rappresenta la scheda logica o il firmware che decide l'algoritmo
     *    di erogazione del resto (es. minimizzare le monete o svuotare i tagli piccoli).
     */

    /**
     * Inizializza un distributore vuoto con una configurazione dell'inventario <b>omogenea</b>.
     * <p>
     * Questo costruttore crea un distributore in cui tutti i binari (colonne) possiedono
     * le stesse specifiche fisiche: medesima capacità massima e medesima taglia accettata.
     *
     * <p><b>Stato Iniziale: </b></p>
     * <ul>
     * <li><b>Inventario:</b> Viene allocata una lista di {@code numeroBinari} binari, inizialmente vuoti di prodotti.</li>
     * <li><b>Economia:</b> Il fondo cassa e il credito utente vengono inizializzati come aggregati vuoti (valore 0).</li>
     * <li><b>Logica:</b> Viene impostata la strategia di resto specificata.</li>
     * </ul>
     *
     * @param numeroBinari   Il numero totale di binari da installare nel distributore (deve essere > 0).
     * @param capacitaBinari La capacità massima di stoccaggio per <i>ogni</i> singolo binario.
     * @param tagliaBinari   La taglia fisica massima accettata da <i>tutti</i> i binari (es. M).
     * @param strategia      L'algoritmo da utilizzare per il calcolo del resto.
     * @throws IllegalArgumentException se {@code numeroBinari} è minore o uguale a zero.
     * @throws NullPointerException     se {@code strategia} è {@code null}.
     */
    public DistributoreAutomatico(int numeroBinari, int capacitaBinari, Taglia tagliaBinari, StrategiaResto strategia) {
        if (numeroBinari <= 0) throw new IllegalArgumentException("Il numero di binari deve essere positivo");
        if (strategia == null) throw new NullPointerException("La strategia non può essere null");

        this.strategia = strategia;
        this.fondoCassa = new Aggregato();
        this.credito = new Aggregato();

        this.binari = new ArrayList<>();
        for (int i = 0; i < numeroBinari; i++) {
            this.binari.add(new Binario(capacitaBinari, tagliaBinari));
        }
    }

    /**
     * Inizializza un distributore con una configurazione dell'inventario <b>personalizzata ed eterogenea</b>.
     * <p>
     * A differenza del costruttore basato su parametri numerici, questo metodo permette di
     * installare binari con caratteristiche fisiche diverse tra loro (es. capacità variabili
     * o taglie differenti per ogni colonna).
     * <p>
     * Questo è un costruttore di convenienza che delega l'inizializzazione al costruttore principale,
     * configurando lo stato iniziale come segue:
     * <ul>
     * <li><b>Inventario:</b> I binari vengono installati rispettando rigorosamente l'ordine della lista fornita
     * (l'elemento all'indice 0 corrisponde al binario fisico n.1).</li>
     * <li><b>Economia:</b> Il fondo cassa e il credito vengono impostati come aggregati vuoti (valore 0).</li>
     * </ul>
     *
     * @param binari    La lista specifica dei binari da installare.
     * @param strategia L'algoritmo di calcolo del resto da utilizzare.
     * @throws IllegalArgumentException se la lista {@code binari} è vuota.
     * @throws NullPointerException     se {@code binari} è {@code null} o se {@code strategia} è {@code null}.
     */
    public DistributoreAutomatico(List<Binario> binari, StrategiaResto strategia) {
        this(binari, new Aggregato(), strategia);
    }

    /**
     * Costruttore primario che consente di inizializzare un distributore con uno stato completo e predefinito.
     * <p>
     * Questo costruttore è ideale per scenari di test o per il ripristino di stato (es. caricamento da configurazione),
     * poiché permette di creare una macchina che possiede già un fondo cassa operativo.
     *
     * <p><b>Dettagli Implementativi:</b></p>
     * <ul>
     * <li>Viene effettuata una <b>copia difensiva</b> della lista {@code binari} per garantire l'incapsulamento
     * e proteggere lo stato interno da modifiche esterne alla collezione.</li>
     * <li>Il credito utente viene sempre inizializzato come aggregato vuoto (0), indipendentemente dagli altri parametri.</li>
     * </ul>
     *
     * @param binari     La lista dei binari da installare.
     * @param fondoCassa L'aggregato di monete che costituisce il fondo iniziale.
     * @param strategia  L'algoritmo di calcolo del resto da utilizzare.
     * @throws IllegalArgumentException se la lista {@code binari} è vuota.
     * @throws NullPointerException     se uno qualsiasi dei parametri passati è {@code null}.
     */
    public DistributoreAutomatico(List<Binario> binari, Aggregato fondoCassa, StrategiaResto strategia) {
        if (binari == null || binari.isEmpty()) throw new IllegalArgumentException("La lista binari non può essere vuota");
        if (fondoCassa == null) throw new NullPointerException("Il fondo cassa non può essere null");
        if (strategia == null) throw new NullPointerException("La strategia non può essere null");
        
        this.binari = new ArrayList<>(binari);
        this.fondoCassa = fondoCassa; 
        this.credito = new Aggregato();
        this.strategia = strategia;
    }

    /**
     * Restituisce il valore totale accumulato nel fondo cassa del distributore.
     * @return l'importo totale in cassa.
     */
    public Importo getSaldoCassa() {
        return fondoCassa.getValoreTotale();
    }

    /**
     * Aggiunge monete al fondo cassa.
     * @param monete L'aggregato di monete da aggiungere.
     * @throws NullPointerException se l'aggregato è null.
     */
    public void aggiungiAlFondoCassa(Aggregato monete) {
        if (monete == null) throw new NullPointerException("Impossibile aggiungere un aggregato nullo");
        this.fondoCassa = this.fondoCassa.sommAggregato(monete);
    }

    /**
     * Svuota integralmente il fondo cassa.
     * @return L'aggregato di monete che erano presenti in cassa.
     */
    public Aggregato svuotaFondoCassa() {
        Aggregato prelievo = this.fondoCassa;
        this.fondoCassa = new Aggregato();
        return prelievo;
    }

    /**
     * Restituisce il valore totale delle monete inserite finora dall'utente (Credito temporaneo).
     * @return l'importo del credito attuale.
     */
    public Importo getValoreCredito() {
        return this.credito.getValoreTotale();
    }

    /**
     * Restituisce una vista unmodifiable (sola lettura) della lista dei binari.
     * Utile per visualizzare lo stato dell'inventario.
     * @return la lista dei binari.
     */
    public List<Binario> getBinari() {
        return Collections.unmodifiableList(binari);
    }

    /**
     * L'utente preme il pulsante "Annulla / Restituzione Monete".
     * Il distributore restituisce esattamente ciò che è stato inserito.
     * @return l'aggregato di monete che costituivano il credito.
     */
    public Aggregato restituisciCredito() {
        Aggregato daRestituire = this.credito;
        this.credito = new Aggregato();
        return daRestituire;
    }

    /**
     * Carica una quantità specifica di un prodotto distribuendola tra i binari disponibili.
     * 
     * <p>
     * <b>Logica di Caricamento:</b>
     * Il metodo scorre l'inventario sequenzialmente (dal primo all'ultimo binario).
     * Quando trova un binario <b>compatibile</b> e con <b>spazio disponibile</b>,
     * vi inserisce il massimo numero possibile di unità. Se la quantità da caricare
     * eccede la capacità residua del binario corrente, il rimanente viene
     * distribuito nei binari successivi idonei.
     *
     * <p><b>Correttezza: </b></p>
     * Un binario è considerato idoneo all'inserimento se:
     * <ul>
     * <li><b>Taglia:</b> La taglia fisica del binario è maggiore o uguale a quella del prodotto.</li>
     * <li><b>Tipologia:</b> Il binario è vuoto OPPURE contiene già lo stesso identico prodotto.</li>
     * </ul>
     *
     * @param p              Il prodotto da caricare nell'inventario.
     * @param quantitaTotale La quantità totale di pezzi che si desidera inserire.
     * @return La quantità rimanente che <b>non</b> è stato possibile caricare per esaurimento dello spazio totale
     * (restituisce 0 se l'operazione è stata completata integralmente).
     * @throws NullPointerException     se il prodotto {@code p} è {@code null}.
     * @throws IllegalArgumentException se {@code quantitaTotale} è negativa.
     * @throws IllegalStateException    se si verifica un errore imprevisto di incoerenza interna durante il caricamento.
     */
    public int caricaProdotto(Prodotto p, int quantitaTotale) {
        if (p == null) throw new NullPointerException("Il prodotto non può essere null");
        if (quantitaTotale < 0) throw new IllegalArgumentException("La quantità non può essere negativa");

        int rimanente = quantitaTotale;

        for (Binario b : binari) {
            if (rimanente == 0) break;

            boolean tagliaOk = p.getTaglia().compareTo(b.getTaglia()) <= 0;
            
            boolean tipoOk = (b.getQuantita() == 0) || (b.getProdotto() != null && b.getProdotto().equals(p));
            
            int spazioLibero = b.getCapacita() - b.getQuantita();
            
            if (tagliaOk && tipoOk && spazioLibero > 0) {
                int daInserireQui = Math.min(rimanente, spazioLibero);
                
                try {
                    b.caricaProdotti(p, daInserireQui);
                    rimanente -= daInserireQui;
                } catch (Exception e) {
                    throw new IllegalStateException("Errore inatteso durante il caricamento: " + e.getMessage());
                }
            }
        }
        
        return rimanente;
    }

    /**
     * Esegue la transazione completa di acquisto di un prodotto.
     * 
     * <p>
     * Il metodo gestisce l'intero ciclo di vita della vendita, dalla verifica dei prerequisiti
     * all'aggiornamento dello stato interno del distributore.
     *
     * <p><b>Logica Transazionale:</b></p>
     * L'operazione è atomica: se una qualsiasi delle verifiche fallisce (prodotto esaurito,
     * credito insufficiente, impossibilità di dare il resto), viene sollevata un'eccezione
     * e lo stato del distributore (inventario e cassa) rimane invariato.
     *
     * <p><b>Correttezza: </b></p>
     * <ol>
     * <li><b>Validazione Input:</b> Verifica esistenza binario e validità pagamento.</li>
     * <li><b>Verifica Disponibilità:</b> Controlla presenza prodotto nel binario.</li>
     * <li><b>Verifica Economica:</b> Controlla sufficienza credito ({@code pagamento >= prezzo}).</li>
     * <li><b>Simulazione Incasso:</b> Calcola una cassa temporanea unendo il fondo attuale al pagamento.</li>
     * <li><b>Calcolo Resto:</b> Tenta di calcolare il resto usando la cassa temporanea e la strategia.</li>
     * <li><b>Commit:</b> Se tutto ha successo, eroga il prodotto e aggiorna il fondo cassa reale.</li>
     * </ol>
     *
     * @param indiceBinario L'indice del binario selezionato dall'utente (formato 1-based: 1, 2, 3...).
     * @param pagamento     Le monete inserite dall'utente per questa transazione.
     * @return Un oggetto {@link Erogazione} contenente il prodotto fisico e l'aggregato di monete del resto.
     * @throws BinarioNonEsistenteException  se l'indice è fuori range.
     * @throws BinarioVuotoException         se il binario è vuoto.
     * @throws CreditoInsufficienteException se il pagamento non copre il prezzo.
     * @throws CassaInsufficienteException   se il valore totale in cassa non basta per il resto.
     * @throws MoneteMancantiException       se mancano i tagli specifici per il resto.
     * @throws NullPointerException          se {@code pagamento} è {@code null}.
     */
    public Erogazione acquistaProdotto(int indiceBinario, Aggregato pagamento) throws BinarioVuotoException, CreditoInsufficienteException, CassaInsufficienteException, MoneteMancantiException, BinarioNonEsistenteException {
        if (pagamento == null) throw new NullPointerException("L'aggregato di pagamento non può essere null");
        int indiceReale = indiceBinario - 1;
        if (indiceReale < 0 || indiceReale >= binari.size()) {
            throw new BinarioNonEsistenteException();
        }
        Binario binario = binari.get(indiceReale);

        if (binario.getQuantita() == 0) throw new BinarioVuotoException();
        Prodotto prodotto = binario.getProdotto();
        Importo valCredito = pagamento.getValoreTotale();
        Importo valPrezzo = prodotto.getImporto();

        if (valCredito.compareTo(valPrezzo) < 0) throw new CreditoInsufficienteException();

        Importo importoResto = valCredito.subtract(valPrezzo);
        Aggregato cassaDisponibile = this.fondoCassa.sommAggregato(pagamento);
        Aggregato restoCalcolato = strategia.calcolaResto(cassaDisponibile, importoResto);
        Prodotto pErogato = binario.erogaProdotto();

        this.fondoCassa = cassaDisponibile.sottraiAggregato(restoCalcolato);

        return new Erogazione(pErogato, restoCalcolato);
    }

    /**
     * Restituisce una rappresentazione testuale formattata dello stato corrente del distributore.
     * <p>
     * Il formato della stringa è specifico per l'interfaccia utente (Client):
     * <br>{@code "? [Credito] | [NomeProdotto] | [Prezzo] | ..."}
     * <ul>
     * <li>Inizia con il simbolo {@code ?} seguito dal valore del credito attuale (in unità).</li>
     * <li>Elenca in sequenza i prodotti disponibili, separati da {@code " | "}.</li>
     * <li>Vengono inclusi nella stringa <b>solo i binari non vuoti</b> (che contengono prodotti).</li>
     * </ul>
     *
     * @return La stringa formattata che descrive credito e inventario.
     */
    @Override
    public String toString() {
        return "Distributore [credito=" + credito + ", cassa=" + fondoCassa + ", binari=" + binari + "]";
    }

    /**
     * Calcola l'hash code del distributore basandosi sul suo intero stato interno.
     * <p>
     * Il calcolo include: l'inventario (binari), il fondo cassa, il credito corrente
     * e la strategia di resto impostata.
     *
     * @return Il valore hash calcolato.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(binari, fondoCassa, credito, strategia);
    }

    /**
     * Verifica l'uguaglianza profonda tra questo distributore e un altro oggetto.
     * <p>
     * Due distributori sono considerati uguali se e solo se si trovano nell'esatto
     * medesimo stato fisico e logico:
     * <ul>
     * <li>Stessa configurazione e contenuto dei binari (Inventario).</li>
     * <li>Stesso ammontare e composizione del fondo cassa.</li>
     * <li>Stesso credito temporaneo inserito.</li>
     * <li>Stessa strategia di calcolo del resto.</li>
     * </ul>
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se gli oggetti sono equivalenti, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DistributoreAutomatico)) return false;

        DistributoreAutomatico other = (DistributoreAutomatico) obj;

        return java.util.Objects.equals(this.binari, other.binari) &&
               java.util.Objects.equals(this.fondoCassa, other.fondoCassa) &&
               java.util.Objects.equals(this.credito, other.credito) &&
               java.util.Objects.equals(this.strategia, other.strategia);
    }
}
