package clients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import Macchinette.Prodotto;

public class OrdinaProdotti {
    public static void main(String[] args) {
        List<Prodotto> prodotti = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                try {
                    prodotti.add(new Prodotto(scanner.nextLine().trim()));
                } catch (Exception e) { continue; }
            }
        }
        Collections.sort(prodotti);
        for (Prodotto p : prodotti) System.out.println(p);
    }
}