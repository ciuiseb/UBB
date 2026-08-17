import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class Generator {
    public static void main(String[] args) {
        clearResultFile();

        int numStudents = 200;
        generateGradeFiles(numStudents);
    }

    private static void clearResultFile() {
        String resultPath = "files/result.txt";
        File file = new File(resultPath);
        try {

            try (FileWriter writer = new FileWriter(file)) {
                writer.write("");
            }
            System.out.println("Result file cleared successfully.");

        } catch (IOException e) {
            System.err.println("Could not clear result file: " + e.getMessage());
        }
    }
    private static void generateGradeFiles(int numStudents) {
        Random random = new Random();

        for (int i = 1; i <= 10; i++) {
            String fileName =  "files/proiect-" + i;
            int coveragePercent = random.nextInt(61) + 40;

            int numGrades = (int) Math.ceil(numStudents * (coveragePercent / 100.0));

            if (numGrades > numStudents) {
                numGrades = numStudents;
            }

            ArrayList<Integer> allStudentIds = new ArrayList<>();
            for (int studentId = 1; studentId <= numStudents; studentId++) {
                allStudentIds.add(studentId);
            }
            Collections.shuffle(allStudentIds);
            ArrayList<Integer> finalStudentList = new ArrayList<>(allStudentIds.subList(0, numGrades));

            try (FileWriter writer = new FileWriter(fileName)) {
                for (int studentId : finalStudentList) {
                    int grade = random.nextInt(10) + 1;
                    writer.write(studentId + " " + grade + "\n");
                }
            } catch (IOException e) {
                System.err.println("Eroare la scrierea fișierului " + fileName + ": " + e.getMessage());
            }
        }
    }
}