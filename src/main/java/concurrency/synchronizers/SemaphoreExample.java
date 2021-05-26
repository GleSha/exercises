package concurrency.synchronizers;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {

    private static final int MAX_CONNECTIONS = 5;
    //Connections to some databases
    private static final boolean[] DatabaseConnections = new boolean[MAX_CONNECTIONS];
    private static final Semaphore SEMAPHORE = new Semaphore(MAX_CONNECTIONS, true);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 7; i++) {
            new Thread(new ConnectionUser(i)).start();
            Thread.sleep(400);
        }
    }

    public static class ConnectionUser implements Runnable {
        private int userId;

        public ConnectionUser(int userId) {
            this.userId = userId;
        }

        @Override
        public void run() {
            System.out.printf("User %d wants to have a connection.\n", userId);
            try {
                SEMAPHORE.acquire();

                int connectionNumber = -1;

                synchronized (DatabaseConnections){
                    for (int i = 0; i < 5; i++)
                        if (!DatabaseConnections[i]) {
                            DatabaseConnections[i] = true;
                            connectionNumber = i;
                            System.out.printf("User %d acquired connection N%d.\n", userId, i);
                            break;
                        }
                }

                Thread.sleep(5000);

                synchronized (DatabaseConnections) {
                    DatabaseConnections[connectionNumber] = false;
                }

                SEMAPHORE.release();
                System.out.printf("User %d releases connection.\n", userId);
            } catch (InterruptedException e) {
            }
        }
    }

}
