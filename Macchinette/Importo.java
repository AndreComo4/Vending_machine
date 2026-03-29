package Macchinette;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * RRappresenta un'astrazione immutabile di una quantità monetaria non negativa.
 * <p>
 * Un'istanza di {@code Importo} modella un valore economico discreto, caratterizzato
 * strutturalmente da due componenti intere: le <b>unità</b> (parte intera) e i
 * <b>centesimi</b> (parte frazionaria). La classe impone la normalizzazione della
 * rappresentazione, garantendo l'unicità dello stato per ogni dato valore.
 * <p>
 * Formalmente, un {@code Importo} corrisponde a un valore matematico V >= 0.
 */
public final class Importo implements Comparable<Importo> {
    
    /** Parte intera dell'importo */
    private final int units;

    /** Parte decimale dell'importo */
    private final int cents;

    /*-
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * Lo stato interno di un oggetto Importo è valido se e solo se soddisfa
     * le seguenti condizioni:
     *
     * 1. Proprietà di Non-Negatività:
     * - Il campo units deve essere sempre maggiore o uguale a zero (>= 0).
     * - Questa classe modella quantità di denaro non negative.
     *
     * 2. Proprietà di Normalizzazione (Forma Canonica):
     * - Il campo cents deve essere strettamente compreso nell'intervallo [0, 99].
     * - Questo vincolo garantisce l'unicità della rappresentazione: un valore come
     *   "105 centesimi" non è ammesso come stato interno valido, ma deve essere
     *   normalizzato in {@code units=1, cents=5}.
     *
     * 3. Immutabilità:
     * - Una volta istanziato, i valori di units e cents non possono cambiare.
     * - I campi sono dichiarati final garantendo l'immutabilità di tali campi.
     * 
     * FUNZIONE DI ASTRAZIONE (AF):
     * Un'istanza di Importo rappresenta astrattamente una quantità monetaria scalare V,
     * appartenente all'insieme dei numeri razionali non negativi (V ∈ Q, V >= 0).
     *
     * La funzione mappa lo stato concreto (units, cents) nel valore astratto V
     * secondo la seguente combinazione lineare:
     *
     * V = units + (cents * 0.01)
     *
     * Dove le componenti concrete assumono il seguente significato semantico:
     * - 'units': rappresenta la parte intera della valuta (macro-unità, es. Euro).
     *   Contribuisce al valore totale con peso 1.
     *
     * - 'cents': rappresenta la parte frazionaria centesimale (micro-unità, es. Centesimi).
     *   Contribuisce al valore totale con peso 0.01.
     */

    /**
     * Costruisce un nuovo oggetto {@code Importo} a partire da unità intere e centesimi.
     * <p>
     * Questo costruttore inizializza lo stato dell'oggetto assicurando che rispetti
     * rigorosamente l'Invariante di Rappresentazione (RI).
     * <p><b>Correttezza: </b></p>
     * <ul>
     * <li>Verifica che {@code units} sia non negativo ({@code units >= 0}).</li>
     * <li>Verifica che {@code cents} sia normalizzato, ovvero compreso nell'intervallo [0, 99].</li>
     * </ul>
     * <p>
     * Poiché i campi sono dichiarati {@code final}, questa inizializzazione stabilisce
     * l'immutabilità dell'istanza.
     *
     * @param units La parte intera dell'importo (deve essere &ge; 0).
     * @param cents La parte frazionaria dell'importo (deve essere tra 0 e 99).
     * @throws IllegalArgumentException se {@code units} è negativo.
     * @throws IllegalArgumentException se {@code cents} non è nell'intervallo valido [0, 99].
     */
    public Importo(int units, int cents) {
        if (units < 0) {
            throw new IllegalArgumentException("L'importo inserito non può essere negativo");
        }
        if (cents < 0 || cents > 99) {
            throw new IllegalArgumentException("I centesimi devono essere tra 0 e 99");
        }
        this.units = units;
        this.cents = cents;
    }

    /**
     * Costruisce un nuovo oggetto {@code Importo} parsandolo da una stringa.
     * <p>
     * Utilizza {@link BigDecimal} per garantire una conversione precisa senza
     * errori di virgola mobile.
     *
     * @param s La stringa che rappresenta l'importo (es. "12.50", "0.05").
     * @throws NullPointerException se la stringa {@code s} è {@code null}.
     * @throws IllegalArgumentException se il formato della stringa non è un numero valido
     * o se il risultato è un importo negativo.
     */
    public Importo(String s) {
        if (s == null) throw new NullPointerException();
        try {
            BigDecimal bd = new BigDecimal(s.trim());
            int totalCents = bd.multiply(BigDecimal.valueOf(100)).intValueExact();
            this.units = totalCents / 100;
            this.cents = totalCents % 100;
            if (this.units < 0 || this.cents < 0 || this.cents > 99)
                throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato importo non valido: " + s);
        }
    }

    /**
     * Costruttore privato di supporto per creare un {@code Importo} da un totale di centesimi.
     * <p>
     * Questo costruttore normalizza automaticamente il valore in unità e centesimi
     * conformi all'RI.
     *
     * @param totalCents Il totale in centesimi (deve essere >= 0).
     * @throws IllegalArgumentException se {@code totalCents} è negativo.
     */
    private Importo(int totalCents) {
        if (totalCents < 0) throw new IllegalArgumentException("Importo risultante negativo");
        this.units = totalCents / 100;
        this.cents = totalCents % 100;
    }

    /**
     * Restituisce la parte intera dell'importo.
     *
     * @return Il numero di unità intere.
     */
    public int getUnits() {
        return this.units;
    }

    /**
     * Restituisce la parte frazionaria dell'importo.
     *
     * @return Il numero di centesimi (sempre tra 0 e 99).
     */
    public int getCents() {
        return this.cents;
    }

    /**
     * ERestituisce un nuovo {@code Importo} ottenuto sommando il valore di questo oggetto
     * a quello dell'argomento specificato.
     * <p>
     * L'operazione richiede che l'argomento {@code other} sia un riferimento valido (non {@code null}).
     * Il metodo garantisce la creazione di una nuova istanza il cui valore monetario corrisponde
     * esattamente alla somma algebrica \( V_{this} + V_{other} \), lasciando inalterato lo stato
     * dell'oggetto corrente in virtù della sua immutabilità.
     *
     * @param other L'importo da aggiungere. Non deve essere {@code null}.
     * @return Un nuovo {@code Importo} che rappresenta la somma.
     * @throws NullPointerException se {@code other} è {@code null}.
     */
    public Importo add(Importo other) {
        if (other == null) throw new NullPointerException();
        return new Importo(this.toTotalCents() + other.toTotalCents());
    }

    /**
     * Restituisce un nuovo {@code Importo} ottenuto sottraendo il valore dell'argomento
     * specificato da quello di questo oggetto.
     * <p>
     * L'operazione è definita se e solo se l'argomento {@code other} è non nullo e il suo
     * valore non eccede quello corrente (\( V_{this} \ge V_{other} \)). Questo vincolo assicura
     * la proprietà di chiusura rispetto agli importi non negativi definita dall'Invariante di Classe.
     * Il risultato è una nuova istanza rappresentante la differenza \( V_{this} - V_{other} \).
     *
     * @param other L'importo da sottrarre. Non deve essere {@code null}.
     * @return Un nuovo {@code Importo} che rappresenta la differenza.
     * @throws NullPointerException se {@code other} è {@code null}.
     * @throws ArithmeticException se il risultato è negativo (la classe supporta solo importi non negativi).
     */
    public Importo subtract(Importo other) {
        if (other == null) throw new NullPointerException();
        int sub = this.toTotalCents() - other.toTotalCents();
        if (sub < 0) throw new ArithmeticException("La differenza negativa");
        return new Importo(sub);
    }

    /**
     * Calcola il prodotto scalare tra questo importo e un fattore intero non negativo.
     * <p>
     * Il metodo richiede che il moltiplicatore {@code n} sia maggiore o uguale a zero (\( n \ge 0 \)).
     * L'oggetto restituito incapsula il valore monetario risultante da \( V_{this} \times n \),
     * preservando l'invariante di non negatività.
     *
     * @param n Il moltiplicatore intero non negativo.
     * @return Un nuovo {@code Importo} risultato della moltiplicazione.
     * @throws IllegalArgumentException se {@code n} è negativo.
     */
    public Importo multiply(int n) {
        if (n < 0) throw new IllegalArgumentException("Moltiplicatore negativo");
        return new Importo(this.toTotalCents() * n);
    }

    /**
     * Calcola il quoziente della divisione intera tra il valore di questo importo e
     * quello del divisore specificato.
     * <p>
     * L'operazione richiede che il divisore {@code other} sia un riferimento valido e rappresenti
     * un valore strettamente positivo (\( V_{other} > 0 \)).
     * Il valore restituito corrisponde al numero massimo di volte (parte intera) che l'importo
     * {@code other} è contenuto in questo importo, formalmente \( \lfloor V_{this} / V_{other} \rfloor \).
     *
     * @param other Il divisore. Non deve essere {@code null}.
     * @return Il risultato intero della divisione.
     * @throws NullPointerException se {@code other} è {@code null}.
     * @throws ArithmeticException se {@code other} è zero.
     */
    public int divide(Importo other) {
        if (other == null) throw new NullPointerException();
        int myCents = this.toTotalCents();
        int otherCents = other.toTotalCents();
        if (otherCents == 0) throw new ArithmeticException("Divisione per zero");
        return myCents / otherCents;
    }

    /**
     * Restituisce il valore totale dell'importo convertito in centesimi.
     * <p>
     * Utile per operazioni aritmetiche in cui è necessario utilizzre 
     * l'importo totale in centesimi.
     *
     * @return Il totale in centesimi.
     */
    public int toTotalCents() {
        return this.units * 100 + this.cents;
    }

    /**
     * Calcola l'hash code per questo importo.
     * <p>
     * L'hash è basato sulla coppia (units, cents), coerente con {@link #equals(Object)}.
     *
     * @return Un intero hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(units, cents);
    }

    /**
     * Verifica l'uguaglianza tra due importi.
     * <p>
     * Due importi sono uguali se e solo se hanno le stesse unità e gli stessi centesimi.
     *
     * @param obj L'oggetto con cui confrontare.
     * @return {@code true} se i due oggetti rappresentano lo stesso valore monetario, {@code false} altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Importo other)) return false;
        return this.units == other.units && this.cents == other.cents;
    }

    /**
     * Restituisce una rappresentazione testuale dell'importo.
     * <p>
     * La stringa distingue tra singolare e plurale omettendo parti nulle:
     * <ul>
     * <li>"1 unit"</li>
     * <li>"2 units"</li>
     * <li>"50 cents"</li>
     * <li>"2 units 50 cents"</li>
     * <li>"0 units" (caso speciale per importo zero)</li>
     * </ul>
     *
     * @return La stringa formattata.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (units > 0) {
            sb.append(units).append(" unit");
            if (units > 1) {
                sb.append("s");
            }
        }

        if (units > 0 && cents > 0) {
            sb.append(" ");
        }

        if (cents > 0) {
            sb.append(cents).append(" cent");
            if (cents > 1) {
                sb.append("s");
            }
        }

        if (units == 0 && cents == 0) {
            return "0 units";
        }

        return sb.toString();
    }

    /**
     * Confronta questo importo con un altro.
     * <p>
     * L'ordinamento è basato sul valore totale (prima le unità, poi i centesimi).
     *
     * @param other L'importo da confrontare.
     * @return Un intero negativo, zero o positivo se questo importo è minore,
     * uguale o maggiore di {@code other}.
     * @throws NullPointerException se {@code other} è {@code null}.
     */
    @Override
    public int compareTo(Importo other) {
        if (other == null) throw new NullPointerException();
        int diffUnits = Integer.compare(this.units, other.units);
        if (diffUnits != 0) return diffUnits;
        return Integer.compare(this.cents, other.cents);
    }

}
