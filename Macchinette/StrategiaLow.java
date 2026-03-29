package Macchinette;

import Exceptions.CassaInsufficienteException;
import Exceptions.MoneteMancantiException;

/**
 * Implementazione della strategia "Low" (Smallest First) per il calcolo del resto.
 * <p>
 * Come specificato dai requisiti, questa strategia è progettata <b>per "svuotare" il distributore
 * dalla moneta di piccolo taglio in eccesso accumulata nel tempo</b>.
 * <p>
 * L'algoritmo privilegia l'erogazione di monete di taglio piccolo: scorrendo i tagli
 * dal più basso al più alto, cerca di utilizzare il numero massimo possibile di monete
 * piccole per coprire l'importo.
 */
public class StrategiaLow implements StrategiaResto {

    /**
     * Costruttore pubblico vuoto 
     */
    public StrategiaLow(){}

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
     * Un'istanza di questa classe rappresenta la politica decisionale "Low" (o Smallest-First) 
     * applicata al problema del resto.
     * Astrattamente, l'oggetto agisce come un operatore funzionale puro f(Cassa, Debito) -> Resto, 
     * caratterizzato dalla seguente euristica di smaltimento:
     * Al fine di ridurre l'accumulo di monete di piccolo taglio nel distributore (svuotamento cassa),
     * l'algoritmo inverte la logica convenzionale e seleziona le monete seguendo l'ordinamento crescente di valore 
     * (dal taglio più piccolo al più grande), saturando il debito con la massima quantità possibile 
     * di monete 'piccole' prima di ricorrere ai tagli di valore superiore.
     *
     */
    
    /**
     * Calcola l'insieme di monete da restituire dando priorità ai tagli più piccoli.
     * 
     * <p>
     * <b>Algoritmo:</b>
     * <ol>
     * <li>Sfrutta l'ordinamento naturale dell'{@link Aggregato} (garantito da {@code TreeMap}),
     * che itera le monete in ordine crescente di valore (da 1 cent a 2 euro).</li>
     * <li>Per ogni taglio incontrato (partendo dal più basso), preleva il massimo numero di pezzi
     * utilizzabili per coprire il debito residuo.</li>
     * <li>Procede verso i tagli più alti solo se il resto non è stato ancora completamente coperto.</li>
     * </ol>
     *
     * <p><b>Correttezza:</b></p>
     * <ul>
     * <li><b>Totale:</b> Verifica preliminarmente che il valore totale della cassa sia sufficiente.</li>
     * <li><b>Composizione:</b> Verifica che la combinazione dei tagli disponibili permetta di
     * formare la cifra esatta (anche se in questo caso è meno probabile fallire rispetto al Greedy,
     * poiché si usano molto i tagli piccoli).</li>
     * </ul>
     *
     * @param cassa        L'aggregato che rappresenta il fondo cassa disponibile.
     * @param daRestituire L'importo target che deve essere raggiunto.
     * @return Un nuovo {@link Aggregato} contenente le monete che compongono il resto.
     * @throws NullPointerException        se {@code cassa} o {@code daRestituire} sono {@code null}.
     * @throws CassaInsufficienteException se il valore totale della cassa è inferiore all'importo richiesto.
     * @throws MoneteMancantiException     se la cassa ha valore sufficiente, ma la strategia fallisce nel comporre l'importo esatto.
     */
    public Aggregato calcolaResto(Aggregato cassa, Importo daRestituire) throws CassaInsufficienteException, MoneteMancantiException {
        if (cassa == null || daRestituire == null) throw new NullPointerException();

        if (daRestituire.compareTo(new Importo(0, 0)) == 0) return new Aggregato();

        if (cassa.getValoreTotale().compareTo(daRestituire) < 0) throw new CassaInsufficienteException();

        Importo restoMancante = daRestituire;
        Aggregato risultato = new Aggregato();

        for (Moneta m : cassa) {
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
