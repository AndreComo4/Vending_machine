package Macchinette;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Una {@code Moneta} è un'entità immutabile che rappresenta una moneta fisica
 * con un valore definito. Formalmente, una moneta è denotata come un oggetto che
 * incapsula un singolo {@link Importo} appartenente a un insieme predefinito e
 * finito di tagli validi.
 * <p>
 * Poiché è immutabile, una volta creata un'istanza di {@code Moneta}, il suo valore
 * non può cambiare. Questa classe garantisce che non possano esistere istanze
 * rappresentanti monete contraffatte o inesistenti (es. una moneta da 3 Euro).
 */
public final class Moneta implements Comparable<Moneta> {
    
    /** Il valore monetario della moneta */
    private final Importo valore;

    /**
     * Elenco dei tagli di monete validi per la valuta corrente.
     * <p>
     * È una {@code List} immutabile per garantire che la definizione di "moneta valida"
     * non possa essere alterata durante l'esecuzione (es. aggiungendo monete false).
     */
    private static final List<Importo> TAGLI_VALIDI = Collections.unmodifiableList(Arrays.asList(
        new Importo(0, 1),  // 1 cent
        new Importo(0, 2),  // 2 cent
        new Importo(0, 5),  // 5 cent
        new Importo(0, 10), // 10 cent
        new Importo(0, 20), // 20 cent
        new Importo(0, 50), // 50 cent
        new Importo(1, 0),  // 1 unit
        new Importo(2, 0)   // 2 units
    ));

    /*-
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * Lo stato interno di un oggetto Moneta è valido se e solo se soddisfa
     * le seguenti condizioni:
     *
     * 1. Proprietà di Esistenza:
     * - Il campo {@code valore} non deve mai essere null.
     * - Una moneta fisica deve necessariamente possedere un valore intrinseco.
     *
     * 2. Proprietà di Validità del Taglio:
     * - Il {@code valore} contenuto nell'oggetto deve corrispondere esattamente
     *   a uno degli importi elencati nella lista TAGLI_VALIDI (0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.0, 2.0).
     * - Questo vincolo assicura che il sistema gestisca solo valuta legale.
     *
     * 3. Immutabilità:
     * - Una volta creata, la Moneta non cambia stato.
     *
     * FUNZIONE DI ASTRAZIONE (AF):
     * Un'istanza di Moneta rappresenta astrattamente un pezzo di metallo coniato
     * (una moneta) esistente nel mondo reale utilizzabile nel distributore.
     * 
     * AF(c): una moneta di valore c
     *
     * - Valore Facciale:
     *   La variabile d'istanza valore mappa direttamente il valore nominale
     *   inciso sulla faccia della moneta fisica.
     */

    /**
     * Costruisce una nuova istanza di {@code Moneta} con il valore specificato.
     * <p><b>Correttezza:</b></p>
     * Questo costruttore stabilisce l'invariante di rappresentazione verificando che:
     * <ul>
     * <li>Il parametro {@code valore} non sia {@code null}.</li>
     * <li>Il parametro {@code valore} sia presente nell'insieme dei {@code TAGLI_VALIDI}.</li>
     * </ul>
     * Essendo il campo {@code valore} dichiarato {@code final} e la classe priva di mutatori,
     * l'immutabilità è garantita dopo la costruzione.
     *
     * @param valore L'importo che rappresenta il taglio della moneta.
     * @throws NullPointerException se {@code valore} è {@code null}.
     * @throws IllegalArgumentException se {@code valore} non corrisponde a un taglio valido
     * definito in {@link #TAGLI_VALIDI}.
     */
    public Moneta(Importo valore) {
        if (valore == null) throw new NullPointerException("Il valore della moneta non può essere nullo.");
        if (!TAGLI_VALIDI.contains(valore)) throw new IllegalArgumentException("Taglio non valido");
        this.valore = valore;
    }

    /**
     * Restituisce il valore monetario della moneta.
     *
     * @return L'oggetto {@link Importo} corrispondente al valore della moneta.
     */
    public Importo getValore() {
        return this.valore;
    }

    /**
     * Restituisce il valore hash per questa moneta.
     * <p>
     * L'hash code è calcolato basandosi esclusivamente sul valore dell'importo incapsulato.
     *
     * @return Il valore hash per questa moneta.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(valore);
    }

    /**
     * Confronta questa {@code Moneta} con l'oggetto specificato per l'uguaglianza.
     * <p>
     * Due monete sono considerate uguali se incapsulano lo stesso identico valore monetario
     * (secondo il metodo {@code equals} di {@link Importo}).
     *
     * @param obj L'oggetto da confrontare con questa moneta.
     * @return {@code true} se l'oggetto specificato è una {@code Moneta} uguale, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Moneta other)) return false;
        return Objects.equals(this.valore, other.valore);
    }

    /**
     * Confronta questa moneta con un'altra in base al valore.
     * <p>
     * L'ordinamento naturale delle monete segue l'ordinamento naturale dei loro importi
     * (dal taglio più piccolo al più grande).
     *
     * @param other La moneta da confrontare.
     * @return Un intero negativo, zero o positivo se questa moneta è rispettivamente
     * minore, uguale o maggiore della moneta specificata.
     */
    @Override
    public int compareTo(Moneta other) {
        return this.valore.compareTo(other.getValore());
    }

    /**
     * Restituisce una rappresentazione in stringa della moneta.
     * <p>
     * Il formato restituito è delegato al metodo {@code toString} dell'oggetto {@link Importo}
     * sottostante (es. "1 euro", "50 cents").
     *
     * @return Una stringa che rappresenta il valore della moneta.
     */
    @Override
    public String toString() {
        return valore.toString();
    }

}
