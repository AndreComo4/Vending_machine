package clients;

import Macchinette.*;
import java.util.Scanner;

import Exceptions.EccezioniDistributore;

public class CaricaBinario {
    public static void main(String[] args) {
        if (args.length < 2) return;
        Binario b = new Binario(Integer.parseInt(args[0]), Taglia.valueOf(args[1].trim().toUpperCase()));
        System.out.println(b);
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                try {
                    String[] parts = line.split(",", 2);
                    int qty = Integer.parseInt(parts[0].trim());
                    Prodotto p = new Prodotto(parts[1].trim());
                    b.caricaProdotti(p, qty);
                    System.out.println(b);
                } catch (EccezioniDistributore e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) { continue; }
            }
        }
    }
}