package Macchinette;

/**
 * Rappresenta le dimensioni fisiche standard di un prodotto.
 * <p>
 * Questa enumerazione definisce le taglie accettate dai binari e dai prodotti
 * del distributore automatico. L'ordinamento naturale è definito dalla sequenza
 * di dichiarazione: <b>Small (S) &lt; Medium (M) &lt; Large (L)</b>.
 * <p>
 * Essendo un'enumerazione Java, le istanze sono intrinsecamente immutabili,
 * uniche.
 */
public enum Taglia {

    /**
     * Rappresenta la dimensione "Small".
     * È l'elemento minimo nell'ordinamento naturale.
     */
    S,

    /**
     * Rappresenta la dimensione "Medium".
     * Intermedio tra S e L.
     */
    M,

    /**
     * Rappresenta la dimensione "Large".
     * È l'elemento massimo nell'ordinamento naturale.
     */
    L;

    /*-
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * Lo stato di validità di Taglia è garantito dalla definizione stessa del tipo Enum,
     * tuttavia formalmente valgono i seguenti vincoli:
     *
     * 1. Chiusura del Dominio:
     * - Le uniche istanze ammissibili nel sistema sono ESCLUSIVAMENTE S, M, L.
     * - Non è possibile estendere o istanziare altri valori di questo tipo (finalità).
     *
     * 2. Integrità dell'Ordinamento:
     * - L'ordine di dichiarazione definisce una relazione d'ordine totale stretta (<)
     *   tale per cui: ordinal(S) < ordinal(M) < ordinal(L).
     * - Questa proprietà è fondamentale per le logiche di compatibilità (es. un prodotto S
     *   entra in un binario L, ma non viceversa).
     *
     * FUNZIONE DI ASTRAZIONE (AF):
     * Ogni costante dell'enumerazione astrae una specifica proprietà fisica spaziale:
     *
     * - Taglia.S -> Mappa il concetto di "ingombro ridotto" o "capacità minima".
     * - Taglia.M -> Mappa il concetto di "ingombro standard" o "capacità media".
     * - Taglia.L -> Mappa il concetto di "ingombro elevato" o "capacità massima".
     */
}