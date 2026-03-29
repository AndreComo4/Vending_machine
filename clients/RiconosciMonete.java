package clients;

import java.util.Scanner;

import Macchinette.Importo;
import Macchinette.Moneta;

public class RiconosciMonete {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                try {
                    Moneta m = new Moneta(new Importo(scanner.nextLine().trim()));
                    System.out.println(m);
                } catch (Exception e) {
                    System.out.println("invalid");
                }
            }
        }
    }
}