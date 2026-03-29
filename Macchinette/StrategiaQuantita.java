package Macchinette;

import java.util.ArrayList;
import java.util.List;

import Exceptions.CassaInsufficienteException;
import Exceptions.MoneteMancantiException;

/**
 * Implementazione della strategia di calcolo del resto basata sulla <b>Quantità</b> (Inventory Balancing).
 * <p>
 * Questa strategia mira a <b>ottimizzare e bilanciare l'inventario</b> del distributore.
 * L'algoritmo privilegia l'utilizzo dei tagli di moneta presenti in maggior numero ("sovrabbondanti"),
 * preservando i tagli più rari.
 * <p>
 * L'effetto a lungo termine di questa strategia è il "livellamento" delle quantità dei vari tagli di 
 * monete, riducendo il rischio che un taglio specifico si esaurisca prematuramente.
 */
public class StrategiaQuantita implements StrategiaResto {

    /**
     * Costruttore pubblico vuoto
     */
    public StrategiaQuantita(){}

    /*-
     * 
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * RI(this) = true.
     * La classe è stateless (priva di campi o stato interno).
     * Di conseguenza, qualsiasi istanza della classe è sempre valida per definizione,
     * non esistendo variabili che possano assumere valori inconsistenti.
     * 
     * FUNZIONE DI ASTRAZIONE (AF):
     * AF(this):
     * Un'istanza di questa classe rappresenta la politica decisionale "Inventory Balancing" (Bilanciamento Scorte)
     * applicata al problema del resto.
     * Astrattamente, l'oggetto agisce come un operatore funzionale dinamico f(Cassa, Debito) -> Resto,
     * caratterizzato dalla seguente euristica di conservazione:
     * Al fine di preservare i tagli più rari e livellare le scorte del distributore,
     * l'algoritmo riconfigura dinamicamente la priorità di scelta ad ogni transazione,
     * selezionando le monete primariamente in base alla loro abbondanza (dal taglio più frequente al più scarso)
     * e secondariamente in base al valore (Greedy standard a parità di quantità).
     *
     */

    /**
     * Calcola l'insieme di monete da restituire dando priorità alla disponibilità fisica.
     * 
     * <p>
     * <b>Algoritmo di Selezione:</b>
     * <ol>
     * <li><b>Ordinamento Primario (Quantità):</b> Le monete vengono ordinate in base alla quantità posseduta
     * in ordine <b>decrescente</b> (dai tagli più abbondanti a quelli più scarsi).</li>
     * <li><b>Ordinamento Secondario (Valore):</b> A parità di quantità, viene preferito il taglio
     * di <b>valore maggiore</b> (comportamento Greedy standard).</li>
     * <li><b>Erogazione:</b> Una volta ordinata la lista, l'algoritmo scorre i tagli e preleva
     * il massimo numero possibile di monete per coprire il debito residuo.</li>
     * </ol>
     *
     * <p><b>Correttezza:</b></p>
     * <ul>
     * <li><b>Totale:</b> Verifica che il saldo totale della cassa sia sufficiente.</li>
     * <li><b>Composizione:</b> Verifica che la combinazione dei tagli (selezionati in questo specifico ordine)
     * permetta di formare la cifra esatta.</li>
     * </ul>
     *
     * @param cassa        L'aggregato contenente le monete disponibili (Fondo Cassa).
     * @param daRestituire L'importo target da raggiungere.
     * @return Un nuovo {@link Aggregato} con le monete selezionate per il resto.
     * @throws NullPointerException        se {@code cassa} o {@code daRestituire} sono {@code null}.
     * @throws CassaInsufficienteException se il valore totale della cassa è inferiore all'importo richiesto.
     * @throws MoneteMancantiException     se non è possibile comporre il resto esatto con l'ordine di priorità stabilito.
     */
    public Aggregato calcolaResto(Aggregato cassa, Importo daRestituire) throws CassaInsufficienteException, MoneteMancantiException {
        if (cassa == null || daRestituire == null) throw new NullPointerException();

        if (daRestituire.compareTo(new Importo(0, 0)) == 0) return new Aggregato();

        if (cassa.getValoreTotale().compareTo(daRestituire) < 0) throw new CassaInsufficienteException();

        Importo restoMancante = daRestituire;
        Aggregato risultato = new Aggregato();
        
        List<Moneta> moneteOrdinate = new ArrayList<>();
        for (Moneta m : cassa) moneteOrdinate.add(m);

        for (int i = 0; i < moneteOrdinate.size(); i++) {
            for (int j = 0; j < moneteOrdinate.size() - 1; j++) {
                Moneta m1 = moneteOrdinate.get(j);
                Moneta m2 = moneteOrdinate.get(j + 1);
                int q1 = cassa.getQuantita(m1);
                int q2 = cassa.getQuantita(m2);
                boolean swap = false;
                if (q2 > q1) swap = true;
                else if (q2 == q1 && m2.compareTo(m1) > 0) swap = true;

                if (swap) {
                    moneteOrdinate.set(j, m2);
                    moneteOrdinate.set(j + 1, m1);
                }
            }
        }

        for (Moneta m : moneteOrdinate) {
            if (restoMancante.compareTo(new Importo(0, 0)) == 0) break;

            Importo valMoneta = m.getValore();
            int quantitaDisponibile = cassa.getQuantita(m);
            int necessarie = restoMancante.divide(valMoneta);

            if (necessarie > 0) {
                int daPrendere = Math.min(necessarie, quantitaDisponibile);
                if (daPrendere > 0) {
                    risultato = risultato.aggiungiMonete(m, daPrendere);
                    restoMancante = restoMancante.subtract(valMoneta.multiply(daPrendere));
                }
            }
        }

        if (restoMancante.compareTo(new Importo(0, 0)) > 0) throw new MoneteMancantiException();
        return risultato;
    }
}
