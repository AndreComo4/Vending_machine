package Macchinette;

import java.util.Objects;

import Exceptions.BinarioPienoException;
import Exceptions.BinarioVuotoException;
import Exceptions.ProdottoDiversoException;
import Exceptions.TagliaNonValidaException;

/**
 * Rappresenta una colonna fisica (binario) del distributore automatico.
 * <p>
 * Un binario è un contenitore <b>mutabile</b> con una capacità limitata e una
 * specifica dimensione fisica (Taglia). Funziona come una pila LIFO (<i>Last-In, 
 * First-Out</i>), simulando il caricamento frontale della merce. L'ultimo 
 * prodotto inserito è fisicamente il primo disponibile per l'erogazione.
 * <p>
 * 
 * <b>Vincoli di Integrità:</b><br>
 * Il binario impone due vincoli rigorosi sui prodotti ospitati per garantire la
 * correttezza fisica e commerciale:
 * <ul>
 * <li><b>Omogeneità di Tipo:</b> Il binario può contenere una sequenza di prodotti
 * solo se sono tutti identici (stesso nome e prezzo). Non è ammesso stoccare prodotti misti.</li>
 * <li><b>Compatibilità Dimensionale:</b> I prodotti caricati devono avere una {@link Taglia}
 * non superiore alla taglia del binario stesso (es. un binario {@code M} accetta prodotti {@code S} o {@code M},
 * ma non {@code L}).</li>
 * </ul>
 */
public class Binario {

    /** Capacità massima che un binario può avere */
    private final int capacita;

    /** La taglia fisica del binario */
    private final Taglia taglia;

    /** Il tipo di prodotto attualmente stoccato (null se vuoto) */
    private Prodotto prodottoContenuto;

    /** Il numero di prodotti presenti */
    private int quantita;

    /*-
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * Lo stato di un oggetto Binario è valido se e solo se:
     *
     * 1. Vincoli Fisici:
     * - La capacità del binario deve essere un numero intero strettamente positivo
     *   (deve esserci spazio per almeno un prodotto).
     * - La taglia fisica deve essere definita, ovvero il campo 'taglia' non può essere nullo.
     *
     * 2. Vincoli di Stato:
     * - La quantità attuale 'quantita' deve essere sempre compresa tra 0 e 'capacita' (estremi inclusi).
     *
     * 3. Coerenza Prodotto-Quantità:
     * - Stato Vuoto: Se 'quantita' è 0, allora 'prodottoContenuto' deve essere null.
     *   (Un binario vuoto non tiene traccia di alcun tipo di prodotto).
     * - Stato Pieno/Parziale: Se 'quantita' > 0, allora 'prodottoContenuto' non deve essere null ma deve avere un tipo assiociato.
     *
     * 4. Compatibilità Fisica:
     * - Se 'prodottoContenuto' è presente, la sua taglia deve essere minore o uguale
     *   alla taglia del binario (prodotto.taglia <= this.taglia).
     *
     * FUNZIONE DI ASTRAZIONE (AF):
     * Un'istanza di Binario rappresenta astrattamente una unità di stoccaggio
     * fisica del distributore, modellata come una Pila LIFO.
     *
     * Formalmente, lo stato astratto è descritto dalla tripla B = <C, T, S>:
     * 
     *  AF(C, T, S): (<C, T, S>)
     *
     * - C (Capacità): Rappresenta il limite fisico massimo di prodotti ospitabili.
     *   Mappa direttamente il campo 'capacita'.
     *
     * - T (Taglia Limite): Rappresenta il vincolo volumetrico dello slot.
     *   Mappa direttamente il campo 'taglia'.
     *
     * - S (Stato del Contenuto): Rappresenta la sequenza ordinata (LIFO) dei prodotti presenti.
     *   La rappresentazione concreta (composta dai campi 'prodottoContenuto' e 'quantita')
     *   mappa la sequenza astratta S definita come:
     *    - Se quantita == 0: S è la sequenza vuota.
     *    - Se quantita > 0: S è una sequenza di lunghezza 'quantita' dove ogni elemento
     *      e_i è identico a 'prodottoContenuto' (S = [P, P, ..., P]).
     */

    /**
     * Istanzia un nuovo binario vuoto configurato con le specifiche fisiche fornite.
     * <p>
     * Questo costruttore inizializza lo stato dell'oggetto garantendo che l'Invariante di
     * Rappresentazione (RI) sia soddisfatto fin dal primo momento. Al termine dell'esecuzione,
     * il binario esisterà, avrà una capienza fissata e una lista di prodotti inizializzata ma vuota.
     *
     * <p><b>Correttezza: </b></p>
     * <ul>
     * <li><b>Capacità:</b> Verifica che {@code capacita} sia un intero strettamente positivo.
     * Un binario deve fisicamente avere almeno uno slot disponibile per essere utile.</li>
     * <li><b>Taglia:</b> Verifica che {@code taglia} sia un oggetto valido (non null),
     * fondamentale per determinare in futuro quali prodotti potranno essere inseriti.</li>
     * </ul>
     *
     * <p><b>Effetti sull'istanza:</b></p>
     * <ul>
     * <li>Assegna i valori immutabili di capacità e taglia.</li>
     * <li>Inizializza la struttura dati interna.</li>
     * </ul>
     *
     * @param capacita Il numero massimo di prodotti che il binario potrà contenere ({@code > 0}).
     * @param taglia   La dimensione fisica (formato) accettata da questo binario (es. S, M, L).
     * @throws IllegalArgumentException se {@code capacita} è minore o uguale a zero.
     * @throws NullPointerException     se {@code taglia} è {@code null}.
     */
    public Binario(int capacita, Taglia taglia) {
        if (capacita <= 0) throw new IllegalArgumentException("La capacità deve essere positiva");
        if (taglia == null) throw new NullPointerException("La taglia non può essere null");

        this.capacita = capacita; 
        this.taglia = taglia;
        this.quantita = 0;
        this.prodottoContenuto = null;
    }

    /**
     * Restituisce la capacità totale del binario.
     *
     * @return Il numero massimo di prodotti che il binario può contenere.
     */
    public int getCapacita() { 
        return this.capacita; 
    }
    
    /**
     * Restituisce la taglia fisica del binario.
     *
     * @return L'oggetto {@link Taglia} associato al binario.
     */
    public Taglia getTaglia() { 
        return this.taglia; 
    }
    
    /**
     * Restituisce il numero di prodotti attualmente presenti.
     *
     * @return Un intero compreso tra 0 e {@code capacita}.
     */
    public int getQuantita() { 
        return this.quantita; 
    }

    /**
     * Restituisce un campione del prodotto contenuto, senza rimuoverlo.
     *
     * @return Il {@link Prodotto} presente, oppure {@code null} se il binario è vuoto.
     */
    public Prodotto getProdotto() {
        if (this.quantita == 0) return null;
        return this.prodottoContenuto;
    }

    /**
     * Tenta di caricare una specifica quantità di un prodotto all'interno del binario.
     * <p>
     * Questo metodo agisce come un <i>mutatore controllato</i> dello stato del binario.
     * L'operazione è atomica dal punto di vista logico: se una qualsiasi delle condizioni
     * di validità non è soddisfatta, lo stato del binario rimane inalterato e viene
     * sollevata l'eccezione appropriata.
     *
     * <p><b>Correttezza:</b></p>
     * <ul>
     * <li><b>Esistenza:</b> {@code p} non null e {@code numero > 0}.</li>
     * <li><b>Compatibilità Taglia:</b> La taglia del prodotto non deve superare quella del binario.</li>
     * <li><b>Spazio:</b> La somma dei prodotti presenti e dei proddoti da caricare non deve superare la capacità.</li>
     * <li><b>Omogeneità:</b> Se il binario non è vuoto, i prodotti caricati devono essere uguali a quelli presenti.</li>
     * </ul>
     *
     * @param p      Il prodotto da caricare nel binario.
     * @param numero Il numero di unità del prodotto da aggiungere.
     * @throws NullPointerException       se {@code p} è {@code null}.
     * @throws IllegalArgumentException   se {@code numero} è minore o uguale a zero.
     * @throws TagliaNonValidaException   se la taglia del prodotto è incompatibile con quella del binario.
     * @throws BinarioPienoException      se lo spazio residuo è insufficiente per caricare la quantità richiesta.
     * @throws ProdottoDiversoException   se il binario non è vuoto e si tenta di inserire un prodotto di tipo diverso.
     */
    public void caricaProdotti(Prodotto p, int numero) throws BinarioPienoException, TagliaNonValidaException, ProdottoDiversoException {
        if (p == null) throw new NullPointerException("Il prodotto non può essere null");
        if (numero <= 0) throw new IllegalArgumentException("Il numero di prodotti da caricare deve essere positivo");
        if (p.getTaglia().compareTo(this.taglia) > 0) throw new TagliaNonValidaException();
        
        if (this.quantita + numero > this.capacita) throw new BinarioPienoException();
        
        if (this.quantita > 0 && !this.prodottoContenuto.equals(p)) throw new ProdottoDiversoException();
        
        if (this.quantita == 0) {
            this.prodottoContenuto = p;
        }
        this.quantita += numero;
    }
    
    /**
     * Eroga (rimuove e restituisce) una singola unità di prodotto dal binario.
     * <p>
     * L'operazione simula l'estrazione fisica del prodotto in cima alla pila (LIFO)
     * e decrementa il contatore delle unità disponibili.
     *
     * Se l'erogazione riduce la quantità a zero, il riferimento interno {@code prodottoContenuto}
     * viene resettato a {@code null}. Questo passaggio è cruciale per ripristinare lo stato
     * di "binario vuoto", permettendo in futuro il caricamento di una tipologia di prodotto
     * diversa senza violare il vincolo di omogeneità.
     *
     * <p><b>Correttezza:</b></p>
     * <ul>
     * <li><b>Disponibilità:</b> Verifica che il binario contenga almeno un prodotto ({@code quantita > 0}).</li>
     * </ul>
     *
     * @return L'oggetto {@link Prodotto} erogato.
     * @throws BinarioVuotoException se il binario è vuoto al momento della richiesta del prodotto.
     */
    public Prodotto erogaProdotto() throws BinarioVuotoException {
        if (this.quantita == 0) throw new BinarioVuotoException();
        
        Prodotto p = this.prodottoContenuto;
        this.quantita--;
        
        if (this.quantita == 0) this.prodottoContenuto = null;
        
        return p;
    }

    /**
     * Restituisce una rappresentazione stringa dello stato del binario.
     * <p>
     * Formato: {@code <Prodotto, TagliaBinario, Quantità, Capacità>}
     * Se il binario è vuoto, il campo prodotto è "-".
     *
     * @return La stringa descrittiva.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        
        if (this.quantita == 0) {
            sb.append("-");
        } else {
            sb.append(this.prodottoContenuto.toString()); 
        }

        sb.append(", ");
        sb.append(this.taglia);
        sb.append(", ");
        sb.append(this.quantita);
        sb.append(", ");
        sb.append(this.capacita);
        sb.append(">");
        
        return sb.toString();
    }

    /**
     * Calcola l'hash code basato sullo stato corrente del binario.
     *
     * @return L'hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(capacita, taglia, prodottoContenuto, quantita);
    }

    /**
     * Verifica l'uguaglianza tra due binari.
     * <p>
     * Due binari sono uguali se hanno la stessa capacità, la stessa taglia
     * e contengono esattamente la stessa sequenza di prodotti.
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se i binari sono identici nello stato.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Binario other)) return false;
        return this.capacita == other.capacita && 
               this.quantita == other.quantita &&
               Objects.equals(this.taglia, other.taglia) && 
               Objects.equals(this.prodottoContenuto, other.prodottoContenuto);
    }
    
}
