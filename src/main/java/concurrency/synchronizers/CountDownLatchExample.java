package concurrency.synchronizers;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class CountDownLatchExample {

    private static ArrayList<Long> data;

    public static void main(String[] args) {
        try {
            concurrentTasks(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void concurrentTasks(int size)
            throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            Thread solver = new Thread(new Solver(i, startGate, endGate));
            solver.start();
        }
        //prepare our data
        prepareData(size);
        //then run computations
        startGate.countDown();
        //and waiting for result
        endGate.await();
        System.out.println("Work is done:");
        data.forEach(System.out::println);
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
        private final CountDownLatch startGate;
        private final CountDownLatch endGate;

        public Solver(int dataIndex, CountDownLatch startGate, CountDownLatch endGate) {
            this.dataIndex = dataIndex;
            this.startGate = startGate;
            this.endGate = endGate;
        }

        @Override
        public void run() {
            try {
                //we may need some preparations for our computations
                Thread.sleep(250);
                startGate.await();
                try {
                    //getting needed data
                    Long neededData = data.get(dataIndex);
                    //do some work...
                    Thread.sleep(250);
                    //result
                    if (neededData == 0) {
                        neededData = 1L;
                    }
                    Long result = System.nanoTime() / neededData;
                    data.set(dataIndex, result);
                    System.out.printf("Worker with dataIndex %d result: %d\n", dataIndex, result);
                }
                finally {
                    endGate.countDown();
                }
            } catch (InterruptedException ignored) {
                ignored.printStackTrace();
            }
        }
    }

}
