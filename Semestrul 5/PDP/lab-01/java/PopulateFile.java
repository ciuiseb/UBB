import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class PopulateFile {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        int k = Integer.parseInt(args[2]);

        sc.close();

        File file = new File("../common/data.txt");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println(n + " " + m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    pw.print((rand.nextInt(100) + 1) + " ");
                }
                pw.println();
            }

            pw.println(k + " " + k);
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    pw.print((rand.nextInt(10) + 1) + " ");
                }
                pw.println();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File outFile = new File("../common/output.txt");
        if (outFile.exists()) {
            try (PrintWriter pw = new PrintWriter(outFile)) {
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
