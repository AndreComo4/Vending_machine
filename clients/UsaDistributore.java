package clients;

import Macchinette.*;
import java.util.*;

import Exceptions.EccezioniDistributore;

public class UsaDistributore {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            if (!sc.hasNextLine()) return;
            List<Binario> bins = new ArrayList<>();
            for (String s : sc.nextLine().split(",")) {
                if (s.trim().isEmpty()) continue;
                String[] t = s.trim().split("\\|");
                bins.add(new Binario(Integer.parseInt(t[0].trim()), Taglia.valueOf(t[1].trim().toUpperCase())));
            }
            String fl = sc.hasNextLine() ? sc.nextLine() : null;
            Aggregato fondo = (fl != null && !fl.isEmpty()) ? new Aggregato(fl) : new Aggregato();
            StrategiaResto strat = new StrategiaGreedy();
            String buf = null;
            if (sc.hasNextLine()) {
                String l = sc.nextLine().trim();
                if (l.equals("L")) strat = new StrategiaLow();
                else buf = l;
            }
            DistributoreAutomatico d = new DistributoreAutomatico(bins, fondo, strat);
            while (buf != null || sc.hasNextLine()) {
                String line = (buf != null) ? buf : sc.nextLine().trim();
                buf = null; 
                if (line.isEmpty()) continue;
                try {
                    String params = line.length() > 1 ? line.substring(1).trim() : "";
                    if (line.startsWith("+")) {
                        String[] p = params.split(",", 2);
                        System.out.println("+ " + d.caricaProdotto(new Prodotto(p[1].trim()), Integer.parseInt(p[0].trim())));
                    } else if (line.startsWith("-")) {
                        String[] p = params.split(",", 2);
                        int indice = Integer.parseInt(p[0].trim());
                        Aggregato pagamento = new Aggregato(p[1].trim());
                        Erogazione esito = d.acquistaProdotto(indice + 1, pagamento);
                        System.out.println("- " + esito.getResto());
                    } else if (line.startsWith("?")) {
                        for (int i = 0; i < d.getBinari().size(); i++) {
                            Binario b = d.getBinari().get(i);
                            if (b.getQuantita() > 0) System.out.println("? " + i + " | " + b.getProdotto().getNome() + " | " + b.getProdotto().getImporto());
                        }
                    }
                } catch (EccezioniDistributore e) {
                    System.out.println("- " + e.getMessage());
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
    }
}