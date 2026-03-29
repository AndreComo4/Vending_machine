package clients;

import java.util.Scanner;

import Macchinette.Importo;

public class OperazioniImporti {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                try {
                    if (line.contains("+")) {
                        String[] parts = line.split("\\+");
                        Importo i1 = new Importo(parts[0].trim());
                        Importo i2 = new Importo(parts[1].trim());
                        System.out.println(fromCents(toCents(i1) + toCents(i2)));
                    } 
                    else if (line.contains("-")) {
                        String[] parts = line.split("-");
                        Importo i1 = new Importo(parts[0].trim());
                        Importo i2 = new Importo(parts[1].trim());
                        long diff = toCents(i1) - toCents(i2);
                        if (diff < 0) System.out.println("negative");
                        else System.out.println(fromCents(diff));
                    } 
                    else if (line.contains("*")) {
                        String[] parts = line.split("\\*");
                        Importo i1 = new Importo(parts[0].trim());
                        int n = Integer.parseInt(parts[1].trim());
                        System.out.println(fromCents(toCents(i1) * n));
                    } 
                    else if (line.contains("/")) {
                        String[] parts = line.split("/");
                        Importo i1 = new Importo(parts[0].trim());
                        Importo i2 = new Importo(parts[1].trim());
                        System.out.println(toCents(i1) / toCents(i2)); 
                    } 
                    else {
                        System.out.println("invalid");
                    }
                } catch (Exception e) {
                    System.out.println("invalid");
                }
            }
        }
    }
    private static long toCents(Importo i) {
        return i.getUnits() * 100L + i.getCents();
    }
    private static Importo fromCents(long totalCents) {
        return new Importo((int)(totalCents / 100), (int)(totalCents % 100));
    }
}