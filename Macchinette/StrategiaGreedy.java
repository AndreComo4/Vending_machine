package Macchinette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Exceptions.CassaInsufficienteException;
import Exceptions.MoneteMancantiException;

/**
 * Implementazione della strategia di calcolo del resto basata sull'algoritmo <b>Greedy</b> (Avido).
 * <p>
 * Questo algoritmo persegue l'obiettivo di <b>minimizzare il numero totale di monete restituite</b>.
 * La logica consiste nello scegliere, ad ogni passo, la moneta di taglio più alto disponibile
 * che sia minore o uguale al residuo da restituire.
 */
public class StrategiaGreedy implements StrategiaResto {

    /**
     * Costruttore pubblico vuoto
     */
    public StrategiaGreedy(){}

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
     * Un'istanza di questa classe non rappresenta un'entità fisica o un contenitore di dati, 
     * bensì concretizzazione a oggetto della politica decisionale "Greedy" (Strategia Avida) 
     * applicata al problema del resto.
     * Astrattamente, l'oggetto agisce come una funzione pura f(Cassa, Debito) -> Resto, definita dalla seguente euristica:
     * Al fine di minimizzare la cardinalità dell'insieme restituito (ovvero il numero totale di monete),
     * l'algoritmo seleziona iterativamente la moneta di valore massimo disponibile nella Cassa 
     * che sia inferiore o uguale al debito residuo, ripetendo il processo fino all'estinzione del debito.
     *
     */
    
    /**
     * Calcola l'insieme di monete da restituire prelevandole dal fondo cassa secondo l'approccio Greedy.
     * 
     * <p>
     * <b>Funzionamento Algoritmo:</b>
     * <ol>
     * <li>Ordina i tipi di monete disponibili in cassa dal taglio più grande al più piccolo.</li>
     * <li>Per ogni taglio, calcola il massimo numero di pezzi utilizzabili senza superare il debito residuo.</li>
     * <li>Sottrae l'importo parziale e passa al taglio successivo.</li>
     * <li>Se al termine del ciclo il debito non è azzerato, solleva un'eccezione.</li>
     * </ol>
     *
     * <p><b>Correttezza:</b></p>
     * <ul>
     * <li><b>Totale:</b> Verifica preliminarmente che il valore totale della cassa sia sufficiente a coprire l'importo.</li>
     * <li><b>Composizione:</b> Verifica che, pur avendo valore sufficiente, la combinazione dei tagli
     * permetta di formare la cifra esatta (es. impossibile dare 3 centesimi se si hanno solo pezzi da 2).</li>
     * </ul>
     *
     * @param cassa        L'aggregato che rappresenta il fondo cassa disponibile.
     * @param daRestituire L'importo target che deve essere raggiunto.
     * @return Un nuovo {@link Aggregato} contenente le monete che compongono il resto.
     * @throws NullPointerException        se {@code cassa} o {@code daRestituire} sono {@code null}.
     * @throws CassaInsufficienteException se il valore totale della cassa è inferiore all'importo richiesto.
     * @throws MoneteMancantiException     se la cassa ha valore sufficiente, ma la strategia greedy fallisce nel comporre l'importo esatto.
     */
    public Aggregato calcolaResto(Aggregato cassa, Importo daRestituire) throws CassaInsufficienteException, MoneteMancantiException {
        if (cassa == null || daRestituire == null) throw new NullPointerException("Cassa e importo null");

        if (daRestituire.compareTo(new Importo(0, 0)) == 0) return new Aggregato();

        if (cassa.getValoreTotale().compareTo(daRestituire) < 0) throw new CassaInsufficienteException();

        Importo restoMancante = daRestituire;
        Aggregato risultato = new Aggregato();
        
        List<Moneta> tipiMoneta = new ArrayList<>();
        for (Moneta m : cassa) tipiMoneta.add(m);
        Collections.reverse(tipiMoneta);

        for (Moneta m : tipiMoneta) {
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