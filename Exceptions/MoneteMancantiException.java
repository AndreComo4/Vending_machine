package Exceptions;

/**
 * Eccezione controllata che segnala l'impossibilità di comporre l'importo esatto del resto a causa della mancanza dei tagli necessari.
 * <p>
 * Questa eccezione viene sollevata quando, pur avendo un saldo totale sufficiente (o addirittura superiore),
 * il distributore non possiede la corretta combinazione di monete fisiche (denominazioni) per soddisfare
 * matematicamente la richiesta di resto.
 * <p>
 * <b>Esempio pratico:</b> Il resto dovuto è 30 centesimi, ma la cassa contiene solo una moneta da 50 centesimi.
 * Il valore è sufficiente (50 > 30), ma l'operazione fallisce per incompatibilità dei tagli.
 */
@SuppressWarnings("serial")
public class MoneteMancantiException extends EccezioniDistributore {
   /**
     * Costruisce una nuova istanza dell'eccezione con il messaggio standard "change".
     * <p>
     * Questo messaggio viene utilizzato per indicare
     * un fallimento nella logica di erogazione del resto dovuto alla carenza di monete specifiche (spiccioli).
     *
     * <p><b>Scenari di errore:</b></p>
     * Questa eccezione viene sollevata dalle strategie di resto (es. {@code StrategiaGreedy}) quando:
     * <ul>
     * <li>Il saldo totale è sufficiente, ma non esiste una combinazione di monete disponibili
     * che somma esattamente all'importo richiesto.</li>
     * <li>L'algoritmo di calcolo (Greedy, Low, etc.) termina l'iterazione lasciando un residuo
     * da pagare non nullo ({@code restoMancante > 0}).</li>
     * </ul>
     */
    public MoneteMancantiException() {
        super("change");
    }
}