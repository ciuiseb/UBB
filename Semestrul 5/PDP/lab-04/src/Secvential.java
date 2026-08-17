import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Secvential {
    public static void main(String[] args) {
        DLL finalGrades = new DLL();

        long startTime = System.nanoTime();
        for (int i = 1; i <= 10; ++i) {
            readGradesFromProject(i, finalGrades);
        }
        long endTime = System.nanoTime();
        System.out.println("Time: " + (endTime - startTime) / 1_000_000 + " ms");

        Validator.validate(finalGrades.getSorted());
    }

    private static void readGradesFromProject(int projectIndex, DLL gradesList) {
        String fileName = "files/proiect-" + projectIndex;
        File file = new File(fileName);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) { // Safety check
                    int studentId = scanner.nextInt();
                    int grade = scanner.nextInt();

                    gradesList.updateGrade(studentId, grade);
                } else {
                    scanner.next();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        }
    }
}