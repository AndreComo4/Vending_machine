package Macchinette;

import java.util.Objects;

/**
 * Rappresenta un singolo prodotto vendibile dal distributore automatico.
 * <p>
 * Un Prodotto è un'entità <b>immutabile</b> caratterizzata da tre proprietà fondamentali:
 * <ul>
 * <li><b>Nome:</b> Descrizione testuale (es. "CocaCola").</li>
 * <li><b>Prezzo:</b> Costo monetario per l'acquisto (es. 1.50 euro).</li>
 * <li><b>Taglia:</b> Dimensione fisica dell'imballaggio (es. M).</li>
 * </ul>
 * I prodotti sono confrontabili (Comparable) per permettere l'ordinamento nei listini.
 */
public final class Prodotto implements Comparable<Prodotto> {
    
    /** Il nome descrittivo del prodotto */  
    private final String nome;

    /** Il costo monetario del prodotto */
    private final Importo prezzo;

    /** La dimensione fisica del prodotto */
    private final Taglia taglia;

    /*-
     *
     * INVARIANTE DI RAPPRESENTAZIONE (RI):
     * Lo stato di un oggetto Prodotto è valido se e solo se:
     *
     * 1. Validità dei Campi:
     * - Il campo 'nome' deve essere una stringa definita (non nulla) e contenere caratteri
     *   (non vuota). Il prodotto deve avere un'identità descrittiva.
     * - Il campo 'prezzo' deve essere un oggetto valido (non nullo) che definisca
     *   il valore economico del bene.
     * - Il campo 'taglia' deve essere specificato (non nullo) per determinare
     *   dimensione del prodotto nel distributore.
     *
     * 2. Immutabilità:
     * - Tutti i campi sono final e riferiscono a tipi immutabili (String, Importo, Enum).
     *
     * FUNZIONE DI ASTRAZIONE (AF):
     * Un'istanza di Prodotto rappresenta astrattamente un'unità di merce
     * disponibile per la vendita nel distributore automatico.
     *
     * Formalmente, lo stato dell'oggetto mappa una tripla immutabile P = <N, C, T>: 
     * 
     *  AF(N, C, T): (<N, C, T>)
     *
     * - N (nome): Rappresenta l'identità commerciale del prodotto (es. "Acqua", "Snack").
     *   Distingue semanticamente il bene dagli altri.
     *
     * - C (prezzo): Rappresenta il valore di scambio economico (Cost) richiesto
     *   all'utente per ottenere il possesso del bene.
     *
     * - T (taglia): Rappresenta le proprietà spaziali e volumetriche del bene fisico.
     *   Definisce il vincolo di compatibilità per l'inserimento nei binari del distributore
     *   (es. un prodotto 'Large' non entra in un binario 'Small').
     * 
     */

    /**
     * Costruisce un nuovo Prodotto specificando tutte le sue caratteristiche.
     *
     * <p><b>Correttezza: </b></p>
     * <ul>
     * <li><b>Nome:</b> Non deve essere {@code null} né vuoto o composto solo da spazi.</li>
     * <li><b>Prezzo:</b> Non deve essere {@code null}.</li>
     * <li><b>Taglia:</b> Non deve essere {@code null}.</li>
     * </ul>
     *
     * @param nome   Il nome del prodotto.
     * @param prezzo Il prezzo di vendita.
     * @param taglia La taglia fisica.
     * @throws NullPointerException     se uno qualsiasi dei parametri è {@code null}.
     * @throws IllegalArgumentException se il nome è vuoto.
     */
    public Prodotto(String nome, Importo prezzo, Taglia taglia) {
        if (nome == null) throw new NullPointerException("Il nome non può essere nullo");
        if (prezzo == null) throw new NullPointerException("Il prezzo non può essere nullo");
        if (taglia == null) throw new NullPointerException("La taglia non può essere nulla");
        if (nome.trim().isEmpty()) throw new IllegalArgumentException("Il nome non può essere vuoto");
        
        this.nome = nome;
        this.prezzo = prezzo;
        this.taglia = taglia;
    }

    /**
     * Costruisce un nuovo Prodotto a partire da una stringa formattata.
     * <p>
     * Questo costruttore permette di istanziare rapidamente un prodotto parsando
     * una singola stringa che contiene tutti i dati necessari separati dal carattere
     * pipe ({@code |}).
     * <br>Formato atteso: {@code Nome|Prezzo|Taglia}
     * <br>Esempio: {@code "Acqua Naturale|0.50|S"}
     *
     * <p><b>Correttezza:</b></p>
     * <ul>
     * <li><b>Integrità:</b> La stringa di input non deve essere {@code null}.</li>
     * <li><b>Formato:</b> La stringa deve contenere esattamente 3 sezioni separate da {@code |}.</li>
     * <li><b>Validità dei Dati:</b>
     * <ul>
     * <li>Il <b>Nome</b> non deve essere vuoto o composto solo da spazi.</li>
     * <li>Il <b>Prezzo</b> deve essere convertibile in un {@link Importo} valido.</li>
     * <li>La <b>Taglia</b> deve corrispondere a uno dei valori dell'enum {@link Taglia} (S, M, L).</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @param s La stringa di configurazione nel formato {@code "Nome|Prezzo|Taglia"}.
     * @throws NullPointerException     se la stringa {@code s} è {@code null}.
     * @throws IllegalArgumentException se il formato della stringa non è corretto (es. numero errato di separatori)
     * o se uno dei valori parsati non è valido.
     */
    public Prodotto(String s) {
        if (s == null) throw new NullPointerException("La stringa prodotto non può essere null");
        String[] parts = s.split("\\|");
        if (parts.length != 3) throw new IllegalArgumentException("Formato prodotto errato. Atteso: Nome|Prezzo|Taglia");
        
        String nomeParsed = parts[0].trim();
        if (nomeParsed.isEmpty()) throw new IllegalArgumentException("Nome vuoto");

        this.nome = nomeParsed;
        this.prezzo = new Importo(parts[1].trim());
        
        try {
            this.taglia = Taglia.valueOf(parts[2].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Taglia non valida: " + parts[2]);
        }
    }

    /**
     * Restituisce il nome del prodotto.
     *
     * @return La stringa rappresentante il nome.
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce il prezzo di vendita del prodotto.
     *
     * @return L'oggetto {@link Importo} del prezzo.
     */
    public Importo getImporto() {
        return this.prezzo;
    }

    /**
     * Restituisce la taglia fisica del prodotto.
     *
     * @return Il valore enumerativo della taglia (S, M, L).
     */
    public Taglia getTaglia() {
        return this.taglia;
    }

    /**
     * Calcola l'hash code del prodotto.
     *
     * @return L'hash code basato su nome, prezzo e taglia.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nome, prezzo, taglia);
    }

    /**
     * Verifica l'uguaglianza logica tra due prodotti.
     * <p>
     * Due prodotti sono uguali se hanno lo stesso nome, lo stesso prezzo e la stessa taglia.
     *
     * @param obj L'oggetto da confrontare.
     * @return {@code true} se i prodotti sono identici.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Prodotto other)) return false;
        
        return Objects.equals(this.nome, other.nome) &&
               Objects.equals(this.prezzo, other.prezzo) &&
               Objects.equals(this.taglia, other.taglia);
    }


    /**
     * Restituisce una rappresentazione stringa del prodotto.
     * <p>
     * Formato: {@code <Nome, Prezzo, Taglia>}
     *
     * @return La stringa formattata.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<");
        sb.append(nome);
        sb.append(", ");
        sb.append(prezzo);
        sb.append(", ");
        sb.append(taglia);
        sb.append(">");

        return sb.toString();
    }

    /**
     * Confronta questo prodotto con un altro per stabilire un ordinamento.
     * <p>
     * Criteri di ordinamento (in ordine di priorità):
     * <ul>
     * <li><b>Taglia:</b> Crescente (S prima di M, ecc.).</li>
     * <li><b>Nome:</b> Ordine alfabetico.</li>
     * <li><b>Prezzo:</b> Crescente.</li>
     * </ul>
     *
     * @param other Il prodotto da confrontare.
     * @return Un intero negativo, zero o positivo.
     * @throws NullPointerException se {@code other} è {@code null}.
     */
    @Override
    public int compareTo(Prodotto other) {
        if (other == null) throw new NullPointerException("Impossibile comparare con null");
        int compareTaglia = this.taglia.compareTo(other.taglia);
        if (compareTaglia != 0) {
            return compareTaglia;
        }
        int compareNome = this.nome.compareTo(other.nome);
        if (compareNome != 0) {
            return compareNome;
        }
        return this.prezzo.compareTo(other.prezzo);
    }

}
