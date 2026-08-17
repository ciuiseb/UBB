import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Paralel {
    static final DLL finalGrades = new DLL();
    static final DLL sortedGrades = new DLL();
    static final DLL plagiatori = new DLL();

    static final CoadaLimitata taskQueue = new CoadaLimitata(50);

    public static void main(String[] args) throws InterruptedException {
//        int processesCount = Integer.parseInt(args[0]);
//        int readersCount = Integer.parseInt(args[1]);
        int processesCount = 8;
        int readersCount = 1;
        int workersCount = processesCount - readersCount;


        Thread[] workers = new Thread[workersCount];

        long startTime = System.nanoTime();

        for (int i = 0; i < workersCount; i++) {
            workers[i] = new Thread(new WorkerThread());
            workers[i].start();
        }

        ExecutorService executor = Executors.newFixedThreadPool(readersCount);

        for (int i = 1; i <= 10; i++) {
            int fileIndex = i;
            executor.submit(() -> readTask("files/proiect-" + fileIndex));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        taskQueue.setFinished();

        for (int i = 0; i < workersCount; i++) {
            workers[i].join();
        }

        Thread[] sorters = new Thread[workersCount];
        for (int i = 0; i < workersCount; i++) {
            sorters[i] = new Thread(new SorterThread());
            sorters[i].start();
        }

        for (int i = 0; i < workersCount; i++) {
            sorters[i].join();
        }

        long endTime = System.nanoTime();
        System.out.println("Time: " + (endTime - startTime) / 1_000_000 + " ms");
        plagiatori.printForward();
        Validator.validate(sortedGrades);
    }

    public static void readTask(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {

            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    int id = scanner.nextInt();
                    int grade = scanner.nextInt();
                    taskQueue.put(new GradeNode(id, grade));
                } else {
                    scanner.next();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Nu am gasit fisierul: " + filePath);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static class WorkerThread implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    GradeNode node = taskQueue.take();
                    if (node == null) break;
                    finalGrades.updateGrade(node.studentId, node.grade);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class SorterThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                GradeNode node = finalGrades.poll();
                if (node == null) break;

                if (node.plagiat) {
                    plagiatori.insertSorted(node.studentId, node.grade);
                } else {
                    sortedGrades.insertSorted(node.studentId, node.grade);
                }
            }
        }
    }

    static class CoadaLimitata {
        private final java.util.LinkedList<GradeNode> queue = new java.util.LinkedList<>();
        private final int capacity;
        private final Lock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();
        private boolean finished = false;

        public CoadaLimitata(int capacity) {
            this.capacity = capacity;
        }

        public void put(GradeNode node) throws InterruptedException {
            lock.lock();
            try {
                while (queue.size() >= capacity) {
                    notFull.await();
                }
                queue.add(node);
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        public GradeNode take() throws InterruptedException {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    if (finished) return null;
                    notEmpty.await();
                }
                GradeNode node = queue.removeFirst();
                notFull.signal();
                return node;
            } finally {
                lock.unlock();
            }
        }

        public void setFinished() {
            lock.lock();
            try {
                finished = true;
                notEmpty.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}