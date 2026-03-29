package Exceptions;

/**
 * Eccezione controllata che segnala un'incompatibilità fisica di dimensioni (Taglia) tra il prodotto e il binario.
 * <p>
 * Questa eccezione viene sollevata durante il caricamento se si tenta di inserire un prodotto
 * che ha una taglia superiore a quella supportata fisicamente dallo slot di destinazione.
 * <p>
 * È un vincolo fisico inderogabile: non è possibile inserire un prodotto "Large" in un binario progettato per prodotti "Small".
 */
@SuppressWarnings("serial")
public class TagliaNonValidaException extends EccezioniDistributore {
    /**
     * Costruisce una nuova istanza dell'eccezione con il messaggio standard "size".
     * <p>
     * Questo messaggio viene utilizzato dal protocollo del sistema per indicare
     * che il prodotto è troppo ingombrante per il binario selezionato.
     *
     * <p><b>Scenari di errore:</b></p>
     * Questa eccezione viene sollevata nel metodo di caricamento se:
     * <ul>
     * <li>La taglia del prodotto in ingresso è strettamente maggiore della taglia del binario
     * ({@code prodotto.taglia.compareTo(binario.taglia) > 0}).</li>
     * </ul>
     */
    public TagliaNonValidaException() {
        super("size");
    }
}