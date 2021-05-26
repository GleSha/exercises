package concurrency.synchronizers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class CyclicBarrierExample {

    private static ArrayList<Long> data;

    public static void main(String[] args) {
        try {
            concurrentIterationTasks(9, 3);
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


    public static void concurrentIterationTasks(int size, int resultsToCollect)
            throws InterruptedException, BrokenBarrierException {
        final CyclicBarrier barrier = new CyclicBarrier(resultsToCollect, new DataProcessor());
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            threads.add(new Thread(new Solver(i, barrier)));
        }
        prepareData(size);
        for (int i = 0; i < size; i++) {
            threads.get(i).start();
            Thread.sleep(500);
        }
    }

    public static class DataProcessor implements Runnable {
        @Override
        public void run() {
            try {

                System.out.println("Results collecting:");
                Thread.sleep(1000);
                System.out.println("Process end");
            } catch (InterruptedException e) {
            }
        }
    }

    public static void prepareData(int size) {
        data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(ThreadLocalRandom.current().nextInt() / (i + 1L));
        }
        System.out.println("Data prepared:");
        data.forEach(System.out::println);
    }


    public static class Solver implements Runnable {

        private final int dataIndex;
        private final CyclicBarrier barrier;

        public Solver(int dataIndex, CyclicBarrier barrier) {
            this.dataIndex = dataIndex;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                //we may need some preparations for our computations
                Thread.sleep(250);
                //getting needed data
                Long neededData = data.get(dataIndex);
                //do some work...
                Thread.sleep(250);
                //result
                if (neededData == 0) {
                    neededData = 1L;
                }
                Long result = System.nanoTime() / neededData;
                System.out.printf("Worker with dataIndex %d result: %d\n", dataIndex, result);
                barrier.await();
                System.out.printf("Worker with dataIndex %d finished work\n", dataIndex);
            } catch (InterruptedException | BrokenBarrierException ignored) {
                ignored.printStackTrace();
            }
        }
    }

}
