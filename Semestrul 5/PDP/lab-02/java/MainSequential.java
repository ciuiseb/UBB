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
        convolve(input, kernel);
        long endTime = System.currentTimeMillis();
        OutputValidator.validate(input);
    }

    public static void convolve(int[][] image, int[][] kernel) {
        int imageRows = image.length;
        int imageCols = image[0].length;
        int kernelSize = kernel.length;
        int halfSize = kernelSize / 2;

        int[] prevRowCopy = null;

        for (int i = 0; i < imageRows; i++) {

            int[] currentRowCopy = image[i].clone();

            for (int j = 0; j < imageCols; j++) {
                int sum = 0;

                // randul de de-a supra - lucram cucopia
                if (prevRowCopy != null) {
                    for (int kj = 0; kj < kernelSize; kj++) {
                        int readCol = j + kj - halfSize;
                        if (readCol >= 0 && readCol < imageCols) {
                            sum += prevRowCopy[readCol] * kernel[0][kj];
                        }
                    }
                }

                //  randul curent - idem
                for (int kj = 0; kj < kernelSize; kj++) {
                    int readCol = j + kj - halfSize;
                    if (readCol >= 0 && readCol < imageCols) {
                        sum += currentRowCopy[readCol] * kernel[1][kj];
                    }
                }

                //  ki = 2 - e nemodificat
                if (i + 1 < imageRows) {
                    for (int kj = 0; kj < kernelSize; kj++) {
                        int readCol = j + kj - halfSize;
                        if (readCol >= 0 && readCol < imageCols) {
                            sum += image[i + 1][readCol] * kernel[2][kj];
                        }
                    }
                }

                image[i][j] = sum;
            }
            prevRowCopy = currentRowCopy;
        }
    }
}