package mpp.practic;


import java.util.Arrays;
import java.util.Random;



public class Main {

    public static class My_Thread extends Thread {
        int[] a, b, c_seq;
        int start, p, n;
        My_Thread(int[] a, int[] b, int[] c_seq, int start, int p, int n) {
            this.a = a;
            this.b = b;
            this.c_seq = c_seq;
            this.start = start;
            this.p = p;
            this.n = n;
        }
        @Override
        public void run() {
            for (int i = start; i < n; i += p) {
                c_seq[i] = a[i] + b[i];
            }
        }
    }

    private static final int VEC_SIZE = 1000000;
    private static final int P = 10;



    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        int[] a = generateArray(VEC_SIZE, 10);
        int[] b = generateArray(VEC_SIZE, 10);
        int[] c_seq = new int[VEC_SIZE];



        for  (int i = 0; i < VEC_SIZE; i++) {
            c_seq[i] = a[i] + b[i];
        }

        long endTime = System.currentTimeMillis();
        if(VEC_SIZE <= 10) {
            System.out.println(Arrays.toString(a));
            System.out.println(Arrays.toString(b));
            System.out.println(Arrays.toString(c_seq));
        }

        System.out.println("Sequential time: " + (endTime - startTime) + " ms");

        int[] c_parallel = new int[VEC_SIZE];

        long startTimeParallel = System.currentTimeMillis();
        My_Thread[] threads  = new My_Thread[P];
        for(int i = 0; i < P; i++) {
            threads[i] = new My_Thread(a, b, c_parallel, i, P, VEC_SIZE);
            threads[i].start();
        }

        for(int i = 0; i < P; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTimeParallel = System.currentTimeMillis();

        System.out.println("Parallel time: " + (endTimeParallel - startTimeParallel) + " ms");
        if(VEC_SIZE <= 10) {
            System.out.println(Arrays.toString(c_parallel));
        }



    }
    private static int[] generateArray(int size, int upperBound) {
        int[] vec =  new int[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            vec[i] = rand.nextInt(upperBound);
        }
        return vec;
    }
}