package clients;

import Macchinette.Aggregato;
import java.util.Scanner;

import Exceptions.CassaInsufficienteException;
import Exceptions.CreditoInsufficienteException;

public class OperazioniAggregati {
    public static void main(String[] args) {
        Aggregato aggregato = new Aggregato();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                char op = line.charAt(0);
                Aggregato delta = new Aggregato(line.substring(1).trim());
                if (op == '+') {
                    aggregato = aggregato.sommAggregato(delta);
                    System.out.println(aggregato);
                } else if (op == '-') {
                    try {
                        aggregato = aggregato.sottraiAggregato(delta);
                        System.out.println(aggregato);
                    } catch (CassaInsufficienteException e) {
                        System.out.println("coins");
                    } catch (CreditoInsufficienteException e) {
                        System.out.println("value");
                    } catch (Exception e) {}
                }
            }
        }
    }
}