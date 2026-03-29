package Macchinette;

import Exceptions.CassaInsufficienteException;
import Exceptions.MoneteMancantiException;

/**
 * Interfaccia che definisce il contratto per gli algoritmi di calcolo del resto.
 * <p>
 * Questa astrazione permette di disaccoppiare la logica di gestione del distributore
 * (che sa <i>quando</i> dare il resto) dalla logica algoritmica (che sa <i>come</i> comporlo).
 * <p>
 * Implementando questa interfaccia è possibile definire diverse politiche di erogazione
 * (es. "Greedy", "Smallest First", "Inventory Balancing") e scambiarle dinamicamente
 * senza modificare il codice del {@link DistributoreAutomatico}.
 */
public interface StrategiaResto {

    /**
     * Determina la combinazione ottimale di monete da prelevare per soddisfare un importo target.
     * <p>
     * Questo metodo deve operare in modo <b>funzionale</b>: non deve modificare lo stato
     * dell'aggregato {@code cassa} passato in input, ma deve restituire una nuova istanza
     * di {@link Aggregato} contenente esclusivamente le monete selezionate per il resto.
     *
     * @param cassa        L'aggregato che rappresenta il fondo cassa disponibile (inclusi eventuali crediti temporanei).
     * @param daRestituire L'importo target che si desidera raggiungere.
     * @return Un nuovo {@link Aggregato} contenente le monete che costituiscono il resto.
     * @throws NullPointerException        se {@code cassa} o {@code daRestituire} sono {@code null}.
     * @throws CassaInsufficienteException se il saldo totale della cassa è insufficiente.
     * @throws MoneteMancantiException     se mancano i tagli specifici per la composizione esatta.
     */
    Aggregato calcolaResto(Aggregato cassa, Importo daRestituire) throws CassaInsufficienteException, MoneteMancantiException;
}