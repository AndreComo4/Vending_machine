package Exceptions;

/**
 * Classe base astratta per tutte le eccezioni controllate (Checked Exceptions) specifiche del dominio Distributore Automatico.
 * <p>
 * Questa classe funge da <b>radice della gerarchia</b> degli errori applicativi.
 * Il suo scopo è raggruppare semanticamente tutte le problematiche che possono verificarsi
 * durante l'uso del distributore (es. credito insufficiente, binario vuoto, cassa vuota),
 * permettendo al client di intercettarle singolarmente o genericamente tramite un blocco
 * {@code catch (EccezioniDistributore e)}.
 */
@SuppressWarnings("serial")
public abstract class EccezioniDistributore extends Exception {
    /**
     * Inizializza una nuova eccezione del distributore con un messaggio descrittivo.
     * <p>
     * Poiché la classe è astratta, questo costruttore è inteso per essere invocato
     * esplicitamente dai costruttori delle sottoclassi concrete (es. tramite {@code super(message)}).
     *
     * @param message Il messaggio di dettaglio che descrive la causa specifica dell'errore
     * (recuperabile successivamente tramite {@link #getMessage()}).
     */
    public EccezioniDistributore(String message) {
        super(message);
    }
}
