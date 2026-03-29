package Exceptions;

/**
 * Eccezione controllata che segnala l'assenza di prodotti all'interno di un binario (Underflow).
 * <p>
 * Questa eccezione indica che l'operazione richiesta (tipicamente un acquisto o un prelievo)
 * non può essere completata perché la colonna selezionata non contiene unità fisiche del prodotto.
 * È l'equivalente logico di "Prodotto Esaurito" (Out of Stock).
 */
@SuppressWarnings("serial")
public class BinarioVuotoException extends EccezioniDistributore {
    /**
     * Costruisce una nuova istanza dell'eccezione con il messaggio standard "empty".
     * <p>
     * Questo messaggio viene utilizzato per notificare al Client
     * che lo slot selezionato è momentaneamente indisponibile per esaurimento scorte.
     *
     * <p><b>Scenari di errore:</b></p>
     * Questa eccezione viene sollevata nei seguenti casi:
     * <ul>
     * <li>Quando un utente seleziona un binario valido il cui contatore prodotti è a <b>zero</b> ({@code quantita == 0}).</li>
     * <li>Quando il sistema tenta internamente di invocare il metodo di erogazione fisica ({@code erogaProdotto()})
     * su un binario già vuoto.</li>
     * </ul>
     */
    public BinarioVuotoException() {
        super("empty");
    }
}
