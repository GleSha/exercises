package concurrency.synchronizers;

import java.util.concurrent.Exchanger;

public class ExchangerExample {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<String>();
        Thread user = new Thread(new UseString(exchanger));
        Thread generator = new Thread(new MakeString(exchanger));
        user.start();
        generator.start();
        try {
            generator.join();
            System.out.println("END GENERATOR");
            user.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static class MakeString implements Runnable {
        private final Exchanger<String> exchanger;
        String str;

        public MakeString(Exchanger<String> exchanger) {
            this.exchanger = exchanger;
            str = new String();
        }

        public void run() {
            char ch = 'A';
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 5; j++) {
                    str += (char) ch++;
                }
                try {
                    str = exchanger.exchange(str);
                } catch (InterruptedException exc) {
                    System.out.println(exc);
                }
            }
        }
    }

    public static class UseString implements Runnable {
        private final Exchanger<String> exchanger;
        String str;

        public UseString(Exchanger<String> exchanger) {
            this.exchanger = exchanger;
        }

        public void run() {
            try {
                while (true) {
                    str = exchanger.exchange(new String());
                    System.out.println("Received: " + str);
                }
            } catch (InterruptedException e) {
                System.out.println("User has been interrupted");
            }
        }
    }

}

