package concurrency.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class ForkJoinPoolExample {

    private static List<Loan> loans;

    public static void main(String[] args) {
        generateList();
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        forkJoinPool.invoke(new LoanAction(loans));
        System.out.println("Work finished");
        printLoanContracts();
    }


    public static class LoanAction extends RecursiveAction {

        private final static int THRESHOLD = 5;

        private final List<Loan> list;

        public LoanAction(List<Loan> list) {
            this.list = list;
        }

        @Override
        protected void compute() {
            if (list.size() > THRESHOLD) {
                LoanAction first = new LoanAction(list.subList(0, list.size() / 2));
                LoanAction second = new LoanAction(list.subList(list.size() / 2, list.size()));
                ForkJoinTask.invokeAll(first, second);
            } else {

                for (int i = 0; i < list.size(); i++) {
                    //marking loan as processed
                    list.get(i).setContractNumber(list.get(i).getContractNumber() + " : processed by " + this.hashCode());
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
    }

    public static void printLoanContracts() {
        if (loans != null) {
            loans.forEach(l -> System.out.println(l.getContractNumber()));
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
