import java.io.InputStream;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainThreads {
    public static class MyThread extends Thread {
        int[][] input, kernel, res;
        int start, end;

        MyThread(int[][] input, int[][] kernel, int[][] res, int start, int end) {
            this.input = input;
            this.kernel = kernel;
            this.res = res;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            int imageRows = input.length;
            int imageCols = input[0].length;
            int kernelSize = kernel.length;

            int resultCols = imageCols;

            for (int i = start; i < end; i++) {
                for (int j = 0; j < resultCols; j++) {
                    int sum = 0;
                    for (int ki = 0; ki < kernelSize; ki++) {
                        for (int kj = 0; kj < kernelSize; kj++) {
                            if ((i + ki) < imageRows && (j + kj) < imageCols)
                                sum += input[i + ki][j + kj] * kernel[ki][kj];
                        }
                    }
                    res[i][j] = sum;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int p = Integer.parseInt(args[0]);

        InputStream is = null;
        try {
            is = new FileInputStream("../common/data.txt");
        } catch (FileNotFoundException e) {
            System.err.println("Could not find data file: " + e.getMessage());
            return;
        }

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
        int[][] result = convolve(input, kernel, p);
        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - startTime) + " ms");

        OutputValidator.validate(result);
    }

    public static int[][] convolve(int[][] image, int[][] kernel, int p) throws InterruptedException {
        int kernelSize = kernel.length;
        int resultRows = image.length;
        int resultCols = image[0].length;
        int[][] result = new int[resultRows][resultCols];

        Thread[] threads = new Thread[p];
        int rowsPerThread = (resultRows + p - 1) / p;

        for (int t = 0; t < p; t++) {
            int start = t * rowsPerThread;
            int end = Math.min(start + rowsPerThread, resultRows);
            threads[t] = new Thread(new MyThread(image, kernel, result, start, end));
            threads[t].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        return result;
    }
}
