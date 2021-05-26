package concurrency.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ThreadPoolsExample {


    private static final int THREADS_COUNT = 4;

    private static List<Loan> loans;

    public static void main(String[] args) throws InterruptedException {
        generateList();
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);
        //splitting our data to avoid synchronization work
        executorService.execute(new LoanTask(0, 10));
        executorService.execute(new LoanTask(10, 20));
        executorService.execute(new LoanTask(20, 30));
        executorService.execute(new LoanTask(30, 40));
        executorService.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Some other work");
                Thread.sleep(1000);
                System.out.println("Some other work finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        try {
            System.out.println();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        printLoanContracts();
    }


    public static void printLoanContracts() {
        if (loans != null) {
            loans.forEach(l -> System.out.println(l.getContractNumber()));
        }
    }


    public static class LoanTask implements Runnable {

        private final int startIndex;
        private final int endIndex;

        public LoanTask(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            for (int i = startIndex; i < endIndex; i++) {
                //marking loan as processed
                loans.get(i).setContractNumber(loans.get(i).getContractNumber() + " : " + startIndex + " -> " + endIndex);
                try {
                    //doing our hard and important work
                    //this work can be related to big data
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void generateList() {
        loans = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            loans.add(new Loan(ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble(),
                    ThreadLocalRandom.current().nextInt(), String.valueOf(ThreadLocalRandom.current().nextDouble())));
        }
    }

    public static class Loan {
        private double amount;
        private double percentRate;
        private int months;
        private String contractNumber;

        public Loan(double amount, double percentRate, int months, String contractNumber) {
            this.amount = amount;
            this.percentRate = percentRate;
            this.months = months;
            this.contractNumber = contractNumber;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getPercentRate() {
            return percentRate;
        }

        public void setPercentRate(double percentRate) {
            this.percentRate = percentRate;
        }

        public int getMonths() {
            return months;
        }

        public void setMonths(int months) {
            this.months = months;
        }

        public String getContractNumber() {
            return contractNumber;
        }

        public void setContractNumber(String contractNumber) {
            this.contractNumber = contractNumber;
        }
    }
}
