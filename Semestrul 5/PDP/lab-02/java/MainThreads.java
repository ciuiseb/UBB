import java.io.InputStream;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;


public class MainThreads {
    public static class MyThread extends Thread {
        int[][] image;
        int[][] kernel;
        int start, end;

        CyclicBarrier barrier;

        MyThread(int[][] image, int[][] kernel, int start, int end, CyclicBarrier barrier) {
            this.image = image;
            this.kernel = kernel;
            this.start = start;
            this.end = end;
            this.barrier = barrier; 
        }

        @Override
        public void run() {
            try {
                int[] prevRow = null;
                if (this.start > 0) {
                    prevRow = image[start - 1].clone();
                }
                int[] lastRow = null;
                if (this.end < image.length) {
                    lastRow = image[end].clone();
                }
                barrier.await();

                int imageCols = image[0].length;
                int kernelSize = kernel.length;
                int halfSize = kernelSize / 2;
                int[] prevRowOriginal = prevRow;


                for (int i = start; i < end; i++) {
                    int[] currentRowOriginal = image[i].clone();
                    int[] nextRowOriginal = (i + 1 == end) ? lastRow : image[i + 1];
                    for (int j = 0; j < imageCols; j++) {
                        int sum = 0;
                        // ki = 0
                        if (prevRowOriginal != null) {
                            for (int kj = 0; kj < kernelSize; kj++) {
                                int readCol = j + kj - halfSize;
                                if (readCol >= 0 && readCol < imageCols) {
                                    sum += prevRowOriginal[readCol] * kernel[0][kj];
                                }
                            }
                        }
                        // ki = 1
                        for (int kj = 0; kj < kernelSize; kj++) {
                            int readCol = j + kj - halfSize;
                            if (readCol >= 0 && readCol < imageCols) {
                                sum += currentRowOriginal[readCol] * kernel[1][kj];
                            }
                        }

                        // ki = 2
                        if (nextRowOriginal != null) {
                            for (int kj = 0; kj < kernelSize; kj++) {
                                int readCol = j + kj - halfSize;
                                if (readCol >= 0 && readCol < imageCols) {
                                    sum += nextRowOriginal[readCol] * kernel[2][kj];
                                }
                            }
                        }
                        image[i][j] = sum;
                    }
                    prevRowOriginal = currentRowOriginal;
                }

            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
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
        convolve(input, kernel, p);
        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - startTime) + " ms");

        OutputValidator.validate(input);
    }

    public static void convolve(int[][] image, int[][] kernel, int p) throws InterruptedException {
        int imageRows = image.length;
        int kernelSize = kernel.length;

        Thread[] threads = new Thread[p];

        CyclicBarrier barrier = new CyclicBarrier(p);

        int rowsPerThread = imageRows / p;
        int currentRow = 0;

        for (int t = 0; t < p; t++) {
            int start = currentRow;
            int end = (t == p - 1) ? imageRows : start + rowsPerThread;
            currentRow = end;
            threads[t] = new MyThread(image, kernel, start, end, barrier);
        }
        for (int t = 0; t < p; t++) {
            threads[t].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
