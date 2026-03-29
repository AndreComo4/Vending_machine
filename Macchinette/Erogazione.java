package Macchinette;

/**
 * Rappresenta il risultato finale di una transazione di acquisto.
 * <p>
 * Questa classe è un contenitore <b>immutabile</b> che accoppia il bene fisico erogato
 * (il {@link Prodotto}) con il cambiamento monetario restituito (il {@link Aggregato} del resto).
 * <p>
 * Viene utilizzata dal Distributore Automatico per restituire in un'unica soluzione
 * l'esito dell'operazione {@code acquistaProdotto}.
 */
public class Erogazione {

    /** L'articolo fisico oggetto della transazione. */
    private final Prodotto prodotto;

    /** L'aggregato di monete che costituisce il resto (può essere vuoto se il resto è 0). */
    private final Aggregato resto;

    /*-
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * Lo stato interno di un oggetto Erogazione è valido se e solo se soddisfa
     * le seguenti proprietà di esistenza:
     *
     * 1. Definizione del Bene (Non-Nullità del Prodotto):
     * - Il campo 'prodotto' non deve mai essere null.
     * - Motivazione: Un'istanza di Erogazione certifica l'avvenuta vendita;
     *   non esiste erogazione senza un bene trasferito all'utente.
     *
     * 2. Definizione del Resto (Non-Nullità dell'Aggregato):
     * - Il campo 'resto' non deve mai essere null.
     * - Motivazione: Il resto nullo (0) deve essere rappresentato esplicitamente
     *   da un Aggregato vuoto (Empty Set), non dall'assenza di riferimento.
     *
     * FUNZIONE DI ASTRAZIONE (AF):
     * Un'istanza di Erogazione rappresenta astrattamente una coppia ordinata (Tupla)
     * risultato della funzione di transazione:
     *
     * E = <P, R>
     *
     * Dove:
     * - P ('prodotto'): Mappa l'unità di merce fisicamente rilasciata nel vassoio.
     * - R ('resto'): Mappa il multinsieme di monete restituite come differenza
     *   tra il valore versato e il prezzo del prodotto.
     */

    /**
     * Costruisce un nuovo oggetto risultato Erogazione.
     * <p>
     * Assicura che l'oggetto sia creato in uno stato consistente verificando
     * che i parametri non siano nulli.
     *
     * @param prodotto Il prodotto dispensato.
     * @param resto    L'insieme delle monete di resto (se il resto è zero, passare un Aggregato vuoto).
     * @throws NullPointerException se {@code prodotto} o {@code resto} sono {@code null}.
     */
    public Erogazione(Prodotto prodotto, Aggregato resto) {
        if (prodotto == null) throw new NullPointerException("Il prodotto erogato non può essere null");
        if (resto == null) throw new NullPointerException("Il resto non può essere null (usare Aggregato vuoto per 0)");
        
        this.prodotto = prodotto;
        this.resto = resto;
    }

    /**
     * Restituisce il prodotto acquistato ed erogato.
     *
     * @return L'oggetto {@link Prodotto}.
     */
    public Prodotto getProdotto() {
        return prodotto;
    }

    /**
     * Restituisce il resto erogato sotto forma di aggregato di monete.
     *
     * @return Un oggetto {@link Aggregato} (non null, eventualmente vuoto).
     */
    public Aggregato getResto() {
        return resto;
    }

    /**
     * Restituisce una rappresentazione stringa dell'erogazione.
     *
     * @return Una stringa descrittiva contenente info su prodotto e resto.
     */
    @Override
    public String toString() {
        return "Prodotto erogato: " + prodotto + " | Resto: " + resto;
    }
}
