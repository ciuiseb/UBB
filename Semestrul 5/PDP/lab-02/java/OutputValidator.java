import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class OutputValidator {

    private static final File outputFile = new File("../common/output.txt");

    public OutputValidator() {
    }

    static public void validate(int[][] result) {
        try {
            if (outputFile.length() == 0) {
                writeResult(result);
                return;
            }

            Scanner sc = new Scanner(outputFile);
            boolean identical = true;

            for (int i = 0; i < result.length && identical; i++) {
                for (int j = 0; j < result[0].length; j++) {
                    if (!sc.hasNextInt()) {
                        identical = false;
                        break;
                    }
                    int oldVal = sc.nextInt();
                    if (oldVal != result[i][j]) {
                        identical = false;
                        break;
                    }
                }
            }
            sc.close();

            if (!identical) {
                System.out.println("Warning: result differs from previous run!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void writeResult(int[][] result) {
        try (PrintWriter pw = new PrintWriter(outputFile)) {
            for (int[] row : result) {
                for (int val : row) {
                    pw.print(val + " ");
                }
                pw.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

