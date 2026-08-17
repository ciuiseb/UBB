import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Paralel {
    static final DLL finalGrades = new DLL();
    static final Queue<GradeNode> taskQueue = new LinkedList<>();

    static boolean readersFinished = false;

    public static void main(String[] args) throws InterruptedException {
        int processesCount = 8;
        int readersCount = 1;
        int workersCount = processesCount - readersCount;

        Thread[] readers = new Thread[readersCount];
        Thread[] workers = new Thread[workersCount];

        long startTime = System.nanoTime();

        for (int i = 0; i < workersCount; i++) {
            workers[i] = new Thread(new WorkerThread());
            workers[i].start();
        }

        int filesPerReader = 10 / readersCount;
        for (int i = 0; i < readersCount; ++i) {
            int start = (i * filesPerReader) + 1;
            int end = (i + 1) * filesPerReader;
            readers[i] = new Thread(new ReaderThread(start, end));
            readers[i].start();
        }

        for (int i = 0; i < readersCount; i++) {
            readers[i].join();
        }

        synchronized (taskQueue) {
            readersFinished = true;
            taskQueue.notifyAll();
        }

        for (int i = 0; i < workersCount; i++) {
            workers[i].join();
        }

        long endTime = System.nanoTime();
        System.out.println("Time: " + (endTime - startTime) / 1_000_000 + " ms");

        Validator.validate(finalGrades.getSorted());
    }

    static class ReaderThread implements Runnable {
        int start, end;

        public ReaderThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                try (Scanner scanner = new Scanner(new File("files/proiect-" + i))) {
                    while (scanner.hasNext()) {
                        if (scanner.hasNextInt()) {
                            int id = scanner.nextInt();
                            int grade = scanner.nextInt();

                            synchronized (taskQueue) {
                                taskQueue.add(new GradeNode(id, grade));
                                taskQueue.notify();
                            }
                        } else {
                            scanner.next();
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + i);
                }
            }
        }
    }

    static class WorkerThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                GradeNode nodeToProcess = null;

                synchronized (taskQueue) {
                    while (taskQueue.isEmpty() && !readersFinished) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    if (taskQueue.isEmpty() && readersFinished) {
                        return;
                    }
                    nodeToProcess = taskQueue.poll();
                }

                if (nodeToProcess != null) {
                    synchronized (finalGrades) {
                        finalGrades.updateGrade(nodeToProcess.studentId, nodeToProcess.grade);
                    }
                }
            }
        }
    }
}