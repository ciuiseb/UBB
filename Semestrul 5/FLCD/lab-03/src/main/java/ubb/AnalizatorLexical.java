package ubb;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizatorLexical {
    private final HashMap<String, Integer> tabelaCoduri;
    private BinarySearchTree IDs;
    private BinarySearchTree consts;
    private final List<String> fip;
    private final List<String> sourceProgram;

    AnalizatorLexical(String condingTablePath) {
        this.tabelaCoduri = new HashMap<>();
        this.IDs = new BinarySearchTree();
        this.consts = new BinarySearchTree();
        this.fip = new ArrayList<>();
        this.sourceProgram = new ArrayList<>();

        readSymbolsTable(condingTablePath);
    }


    void getFIPandTS(String sourceFile) {
        try {
            clearTables();

            readSourceFile(sourceFile);
            identificaIDs();
            identificaConsts();
            generateFIP();


            printResults();

        } catch (RuntimeException e) {
            System.err.println("Eroare fatala: " + e.getMessage());
        }
    }

    private void clearTables() {
        this.IDs = new BinarySearchTree();
        this.consts = new BinarySearchTree();
        this.fip.clear();
        this.sourceProgram.clear();
    }

    private void readSourceFile(String sourceFile) {
        try (InputStream is = getClass().getResourceAsStream("/" + sourceFile)) {
            assert is != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sourceProgram.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Fisierul nu a fost deschis: " + sourceFile, e);
        } catch (NullPointerException e) {
            throw new RuntimeException("Fisierul nu a fost gasit: " + sourceFile, e);
        }
    }

    private void identificaIDs() {
        var types = List.of("Numerus", "Ratio", "Struct");


        for (String line : sourceProgram) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) continue;

            List<String> simboluri = Arrays.stream(trimmedLine.split("(?=;)|\\s+")).toList();
            if (simboluri.isEmpty()) continue;


            if (types.contains(simboluri.getFirst())) {
                var ids = processIDsLine(simboluri, this.IDs);
                ids.forEach(this.IDs::insert);
            }
        }
    }

    private void identificaConsts() {
        String numberRegex = "[-+]?(?:\\d+(\\.\\d*)?|\\.\\d+)([eE][-+]?\\d+)?";
        Pattern numberPattern = Pattern.compile(numberRegex);

        for (String line : sourceProgram) {
            Matcher matcher = numberPattern.matcher(line);
            while (matcher.find()) {
                String foundNumber = matcher.group();
                this.consts.insert(foundNumber);
            }
        }
    }

    private void generateFIP() {
        var types = List.of("Numerus", "Ratio", "Struct");
        int lineNumber = 0;
        boolean inProgramBody = false;

        for (String line : sourceProgram) {
            lineNumber++;
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) continue;

            var testSimboluri = Arrays.stream(trimmedLine.split("(?=;)|\\s+")).toList();
            if (testSimboluri.isEmpty()) continue;


            if (!inProgramBody) {
                if (types.contains(testSimboluri.getFirst())) {
                    continue;
                } else {
                    inProgramBody = true;
                }
            }
            List<String> simboluri = tokenizeLine(trimmedLine);

            for (String s : simboluri) {
                if (s.isEmpty()) continue;
                try {
                    String pifEntry = mapSimbol(s);
                    this.fip.add(pifEntry);
                } catch (RuntimeException e) {
                    String errorMsg = String.format("Eroare Lexicala la linia %d: %s%n", lineNumber, e.getMessage());
                    throw new RuntimeException(errorMsg, e);
                }
            }
        }
    }

    private String mapSimbol(String simbol) {

        if (this.consts.contains(simbol))
            return String.format("(%s, %s)", "const", consts.get(simbol));


        if (this.IDs.contains(simbol))
            return String.format("(%s, %s)", "ID", IDs.get(simbol));


        Integer cod = tabelaCoduri.get(simbol);
        if (cod != null)
            return Integer.toString(cod);
        throw new RuntimeException("Simbol invalid: " + simbol);
    }

    private void printResults() {
        System.out.println("--- Forma Interna a Programului (FIP) ---");
        this.fip.forEach(System.out::println);

        System.out.println("\n--- Tabela Simboluri (Identificatori) ---");
        this.IDs.print();

        System.out.println("\n--- Tabela Simboluri (Constante) ---");
        this.consts.print();
    }

    private List<String> processIDsLine(List<String> line, BinarySearchTree IDs) {
        List<String> result = new ArrayList<>();
        var reg = "[a-z][a-zA-Z0-9]*";
        int i = 1;

        while (i < line.size()) {
            var simbol = line.get(i);
            if (simbol.equals(";")) {
                if (i == 1) throw new RuntimeException("Declaratie goala.");
                break;
            }

            if (simbol.matches(reg)) {
                if (IDs.contains(simbol)) {
                    throw new RuntimeException("Redeclararea ID: " + simbol);
                }
                result.add(simbol);
            } else {
                throw new RuntimeException("ID ilegal: " + simbol);
            }
            i++;

            if (i >= line.size()) break;
            if (line.get(i).equals(";")) break;

            if (!line.get(i).equals("et"))
                throw new RuntimeException("Lipseste 'et' in declaratie");
            i++;
        }
        return result;
    }

    private List<String> tokenizeLine(String line) {
        line = line.trim();

        return Arrays.stream(line.split("(?=;)|\\s+")).toList();
    }

    private void readSymbolsTable(String condingTablePath) {
        try (InputStream is = getClass().getResourceAsStream("/" + condingTablePath)) {
            assert is != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

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
            }
        } catch (IOException e) {
            throw new RuntimeException("Fisierul nu a fost deschis: " + condingTablePath, e);
        } catch (NullPointerException e) {
            throw new RuntimeException("Fisierul nu a fost gasit: " + condingTablePath, e);
        }
    }
}