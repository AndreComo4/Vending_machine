package Exceptions;

/**
 * Eccezione controllata che segnala l'insufficienza del valore monetario totale nel distributore.
 * <p>
 * Questa eccezione indica un problema di <b>liquidità complessiva</b>: il distributore (fondo cassa + credito inserito)
 * non possiede fisicamente un ammontare totale sufficiente a coprire la cifra del resto dovuto.
 * <p>
 * Si distingue da {@link MoneteMancantiException} perché in questo caso il problema non è la
 * combinazione dei tagli, ma proprio la mancanza matematica del valore necessario.
 */
@SuppressWarnings("serial")
public class CassaInsufficienteException extends EccezioniDistributore {
    /**
     * Costruisce una nuova istanza dell'eccezione con il messaggio standard "value".
     * <p>
     * Questo messaggio viene utilizzato per indicare
     * che il saldo totale della macchina è inferiore all'importo da restituire.
     *
     * <p><b>Scenari di errore:</b></p>
     * Questa eccezione viene sollevata dalle strategie di resto (es. {@code StrategiaGreedy}) quando:
     * <ul>
     * <li>Il controllo preliminare sul totale fallisce:
     * {@code cassa.getValoreTotale() < importoResto}.</li>
     * </ul>
     */
    public CassaInsufficienteException() {
        super("value");
    }
}