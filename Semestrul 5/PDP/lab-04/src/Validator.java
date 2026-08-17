import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Validator {

    private static final String RESULT_FILE_PATH = "files/result.txt";

    public static void validate(DLL list) {
        File resultFile = new File(RESULT_FILE_PATH);

        var currentNode = list.getHead();

        if (!resultFile.exists() || resultFile.length() == 0) {
            writeResultsToFile(list);
            return;
        }


        try (Scanner fileScanner = new Scanner(resultFile)) {

            while (fileScanner.hasNextLine()) {
                String fileLine = fileScanner.nextLine();

                if (currentNode == null) {
                    System.err.println("\n[VALIDATOR] Fail");
                    return;
                }

                String currentLine = currentNode.studentId + ": " + currentNode.grade;

                if (!fileLine.equals(currentLine)) {
                    System.err.println("\n[VALIDATOR] Fail");
                    return;
                }

                currentNode = currentNode.next;
            }

            if (currentNode != null) {
                System.err.println("\n[VALIDATOR] Fail");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Eroare la citirea fișierului: " + RESULT_FILE_PATH);
        }
    }

    private static void writeResultsToFile(DLL list) {
        try (FileWriter writer = new FileWriter(RESULT_FILE_PATH)) {
            var current = list.getHead();

            while (current != null) {
                String line = current.studentId + ": " + current.grade;
                writer.write(line + "\n");
                current = current.next;
            }

        } catch (IOException e) {
            System.err.println("Eroare la scrierea rezultatului în fișier: " + e.getMessage());
        }
    }
}