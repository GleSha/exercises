package concurrency.synchronizers;

import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

public class PhaserExample {

    private static volatile int[][] data = new int[4][4];
    private static final int PHASES_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        Phaser phaser = new Phaser();
        Thread t1 = new Thread(new PhaserProcessor(phaser, 0, 0, 2, PHASES_COUNT, 1));
        Thread t2 = new Thread(new PhaserProcessor(phaser, 0, 2, 2, PHASES_COUNT, 2));
        Thread t3 = new Thread(new PhaserProcessor(phaser, 2, 0, 2, PHASES_COUNT, 3));
        Thread t4 = new Thread(new PhaserProcessor(phaser, 2, 2, 2, PHASES_COUNT, 4));
        phaser.register();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        Thread t5;
        int currentWorkThreadsCount = 4;
        while (phaser.getPhase() < PHASES_COUNT) {
            System.out.printf("Phase %d started\n", phaser.getPhase());
            if (phaser.getPhase() == 2) {
                t5 = new Thread(new PhaserCommonDataProcessor(phaser, currentWorkThreadsCount));
                t5.start();
            }
            while (phaser.getRegisteredParties() - phaser.getArrivedParties() > 1) {
                Thread.sleep(100);
            }

            System.out.printf("Phase %d finished\n", phaser.getPhase());
            printData();
            phaser.arrive();
        }

    }

    public static void printData() {
        for (int[] datum : data) {
            for (int j = 0; j < data.length; j++) {
                System.out.print("\t" + datum[j] + "\t");
            }
            System.out.println("\n");
        }
    }

    public static class PhaserCommonDataProcessor implements Runnable {
        private final Phaser phaser;
        private final int workersCount;

        public PhaserCommonDataProcessor(Phaser phaser, int workersCount) {
            this.phaser = phaser;
            this.workersCount = workersCount;
            this.phaser.register();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1500);
                System.out.println("From t5");
                printData();
                while (phaser.getArrivedParties() < workersCount) {
                    Thread.sleep(100);
                }
                for (int i = 0; i < 4; i++)
                    for (int j = 0; j < 4; j++)
                        data[i][j] = data[i][j] * data[i][j];
                System.out.println("t5 finished");
                phaser.arriveAndDeregister();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class PhaserProcessor implements Runnable {

        private final Phaser phaser;
        private final int rowStart;
        private final int colStart;
        private final int size;
        private final int phasesCount;
        private final int valueToAdd;

        public PhaserProcessor(Phaser phaser, int rowStart, int colStart, int size, int phasesCount, int valueToAdd) {
            this.phaser = phaser;
            this.rowStart = rowStart;
            this.colStart = colStart;
            this.size = size;
            this.phasesCount = phasesCount;
            this.valueToAdd = valueToAdd;
            this.phaser.register();
        }

        @Override
        public void run() {
            try {
                while (phaser.getPhase() < phasesCount) {
                    System.out.println("w: " + phaser.getPhase());
                    Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1000));
                    int rowEnd = rowStart + size;
                    int colEnd = colStart + size;
                    for (int i = rowStart; i < rowEnd; i++) {
                        for (int j = colStart; j < colEnd; j++) {
                            data[i][j] = data[i][j] + valueToAdd;
                        }
                    }
                    phaser.arriveAndAwaitAdvance();
                }
                System.out.println("No more phases");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
