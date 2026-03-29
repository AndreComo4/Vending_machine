package Exceptions;

/**
 * Eccezione controllata che segnala il tentativo di accesso a un binario (slot) non esistente.
 * <p>
 * Questa eccezione viene sollevata dal distributore quando viene richiesto un indice
 * che non corrisponde a nessuna colonna fisica configurata (es. indice negativo o oltre il limite).
 * <p>
 * Sostituisce l'uso generico di {@link IndexOutOfBoundsException} per fornire un'eccezione
 * semantica specifica del dominio.
 */
@SuppressWarnings("serial")
public class BinarioNonEsistenteException extends EccezioniDistributore {
    /**
     * Costruisce una nuova istanza dell'eccezione con il messaggio standard "slot".
     * <p>
     * Il messaggio "slot" è utilizzato dal Client per notificare all'utente che
     * il numero selezionato non è valido.
     */
    public BinarioNonEsistenteException() {
        super("slot");
    }
}
