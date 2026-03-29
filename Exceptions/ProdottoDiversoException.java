package Exceptions;

/**
 * Eccezione controllata che segnala una violazione del vincolo di omogeneità del prodotto all'interno di un binario.
 * <p>
 * Questa eccezione viene sollevata durante le operazioni di rifornimento (caricamento) se si tenta
 * di inserire un tipo di prodotto diverso da quello già attualmente stoccato nello slot.
 * <p>
 * Un binario fisico non può contenere prodotti misti: se contiene "Acqua", non è possibile aggiungere "Bibita"
 * finché il binario non viene completamente svuotato.
 */
@SuppressWarnings("serial")
public class ProdottoDiversoException extends EccezioniDistributore {
    /**
     * Costruisce una nuova istanza dell'eccezione con il messaggio standard "item".
     * <p>
     * Questo messaggio viene utilizzato per indicare
     * un'incompatibilità tra la merce in ingresso e quella già presente.
     *
     * <p><b>Scenari di errore:</b></p>
     * Questa eccezione viene sollevata se:
     * <ul>
     * <li>Il binario non è vuoto ({@code quantita > 0}).</li>
     * <li>Il prodotto passato al metodo di caricamento non è uguale ({@code !equals()})
     * a quello già presente nello stato del binario.</li>
     * </ul>
     */
    public ProdottoDiversoException() {
        super("item");
    }
}
