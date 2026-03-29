package Exceptions;

/**
 * Eccezione controllata che segnala l'insufficienza del credito inserito dall'utente.
 * <p>
 * Questa eccezione viene sollevata durante la fase di acquisto quando il valore totale
 * delle monete inserite (il credito temporaneo) è inferiore al prezzo del prodotto selezionato.
 * Indica sostanzialmente che l'utente "non ha pagato abbastanza" per l'articolo desiderato.
 */
@SuppressWarnings("serial")
public class CreditoInsufficienteException extends EccezioniDistributore {
    /**
     * Costruisce una nuova istanza dell'eccezione con il messaggio standard "value".
     * <p>
     * Questo messaggio viene utilizzato per notificare al Client
     * che l'importo fornito non raggiunge la soglia di prezzo richiesta per l'erogazione.
     *
     * <p><b>Scenari di errore:</b></p>
     * Questa eccezione viene sollevata nel metodo di acquisto se:
     * <ul>
     * <li>Il confronto tra il credito accumulato e il prezzo del prodotto fallisce:
     * {@code credito < prezzoProdotto}.</li>
     * </ul>
     */
    public CreditoInsufficienteException() {
        super("value");
    }
}
