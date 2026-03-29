package clients;

import java.util.Scanner;

import Exceptions.EccezioniDistributore;
import Macchinette.Aggregato;
import Macchinette.Importo;
import Macchinette.StrategiaGreedy;
import Macchinette.StrategiaLow;
import Macchinette.StrategiaResto;

public class CalcolaResti {
    public static void main(String[] args) {
        if (args.length != 2) return;
        StrategiaResto strategia = "H".equals(args[0]) ? new StrategiaGreedy() : ("L".equals(args[0]) ? new StrategiaLow() : null);
        if (strategia == null) return;
        Importo restoDovuto = new Importo(args[1]);
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                Aggregato disponibile = new Aggregato(line);
                try {
                    System.out.println(strategia.calcolaResto(disponibile, restoDovuto));
                } catch (EccezioniDistributore e) { 
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}