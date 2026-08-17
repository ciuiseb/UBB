import java.io.InputStream;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainSequential {
    public static void main(String[] args) {
        InputStream is = null;
        try {
            is = new FileInputStream("../common/data.txt");
        } catch (FileNotFoundException e) {
            System.err.println("Could not find data file: " + e.getMessage());
            return;
        }

        assert is != null;
        Scanner sc = new Scanner(is);
        int imageRows = sc.nextInt();
        int imageCols = sc.nextInt();

        int[][] input = new int[imageRows][imageCols];
        for (int i = 0; i < imageRows; i++) {
            for (int j = 0; j < imageCols; j++) {
                input[i][j] = sc.nextInt();
            }
        }

        int kernelRows = sc.nextInt();
        int kernelCols = sc.nextInt();

        int[][] kernel = new int[kernelRows][kernelCols];
        for (int i = 0; i < kernelRows; i++) {
            for (int j = 0; j < kernelCols; j++) {
                kernel[i][j] = sc.nextInt();
            }
        }

        sc.close();

        long startTime = System.currentTimeMillis();
        int[][] result = convolve(input, kernel);
        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - startTime) + " ms");
        OutputValidator.validate(result);
    }
    public static int[][] convolve(int[][] image, int[][] kernel) {
        int imageRows = image.length;
        int imageCols = image[0].length;
        int kernelSize = kernel.length;

        int[][] result = new int[imageRows][imageCols];

        for (int i = 0; i < imageRows; i++) {
            for (int j = 0; j < imageCols; j++) {
                int sum = 0;
                for (int ki = 0; ki < kernelSize; ki++) {
                    for (int kj = 0; kj < kernelSize; kj++) {
                        if ((i + ki) < imageRows && (j + kj) < imageCols)
                            sum += image[i + ki][j + kj] * kernel[ki][kj];
                    }
                }
                result[i][j] = sum;
            }
        }

        return result;
    }
}