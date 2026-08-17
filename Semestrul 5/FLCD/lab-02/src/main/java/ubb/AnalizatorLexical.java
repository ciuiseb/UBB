package ubb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AnalizatorLexical {
    private final HashMap<String, Integer> tabelaCoduri;
    private final List<String> IDs;

    AnalizatorLexical(String filePath) {
        tabelaCoduri = new HashMap<>();
        IDs = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream("/" + filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String token = parts[0].trim();
                    int cod = Integer.parseInt(parts[1].trim());
                    tabelaCoduri.put(token, cod);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Fisierul nu a fost gasit: " + filePath);
        }
    }

    void analizaSintactica(String sourceFileName) {

        try (InputStream is = getClass().getResourceAsStream("/" + sourceFileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            int lineIndex = 0;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                var simboluri = Arrays.stream(line.split("(?=;)|\\s+")).toList();
                try {
//                    System.out.println(simboluri);
                    System.out.println(processSequence(simboluri));
                } catch (RuntimeException e) {
                    System.out.println("Error on line " + lineIndex + ":" + e.getMessage());
                }


                ++lineIndex;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Fisierul nu a fost gasit: " + sourceFileName);
        }
    }

    private Integer mapSimbol(String simbol) {
        String numarRegex = "^-?\\d+(\\.\\d+)?$";
        if (simbol.matches(numarRegex)) return 1;
        if (IDs.contains(simbol)) return 0;
        Integer cod = tabelaCoduri.get(simbol);
        if (cod != null) return cod;

        throw new RuntimeException("Simbol invalid: " + simbol);
    }

    private List<Integer> processSequence(List<String> line) {
        var types = List.of("Numerus", "Ratio", "Struct");
        if (types.contains(line.getFirst())) {
            var reg = "[a-z][a-zA-Z0-9]*";
            int i = 1;
            boolean valid = true;
            while (i < line.size()) {
                var simbol = line.get(i);
                if (simbol.equals(";"))
                    throw new RuntimeException("Incorrect declaration of symbols");

                if (simbol.matches(reg)) {
                    if (IDs.contains(simbol)) {
                        throw new RuntimeException("Redeclaration of ID: " + simbol);
                    }
                    IDs.add(simbol);
                } else {
                    throw new RuntimeException("Illegal ID: " + simbol);
                }
                i++;
                if (line.get(i).equals(";")) break;
                if (!line.get(i).equals("et"))
                    throw new RuntimeException("Incorrect declaration of symbols");
                i++;
            }
        }
        return line.stream().map(this::mapSimbol).toList();
    }
}
