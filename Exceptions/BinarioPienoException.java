package Exceptions;

/**
 * Eccezione controllata che segnala il superamento della capacità massima di un binario.
 * <p>
 * Questa eccezione viene sollevata durante le operazioni di caricamento (es. in {@code caricaProdotto})
 * quando si tenta di inserire una quantità di merce superiore allo spazio fisico residuo
 * disponibile nella colonna.
 */
@SuppressWarnings("serial")
public class BinarioPienoException extends EccezioniDistributore {
    /**
     * Costruisce una nuova istanza dell'eccezione con il messaggio standard "capacity".
     * <p>
     * Questo messaggio viene utilizzato per indicare
     * un errore di riempimento eccessivo del binario.
     *
     * <p><b>Scenari di errore:</b></p>
     * Questa eccezione viene tipicamente sollevata nei seguenti casi:
     * <ul>
     * <li>Quando si tenta di aggiungere prodotti ad un binario che ha già raggiunto la sua <b>capacità massima</b>.</li>
     * <li>Quando si tenta di caricare un lotto di prodotti la cui quantità, sommata a quella già presente,
     * <b>eccede il limite fisico</b> del binario ({@code quantitaPresente + nuoviProdotti > capacita}).</li>
     * </ul>
     */
    public BinarioPienoException() {
        super("capacity");
    }
}
