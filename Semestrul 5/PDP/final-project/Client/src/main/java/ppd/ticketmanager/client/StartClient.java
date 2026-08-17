package ppd.ticketmanager.client;

import ppd.ticketmanager.server.Server;

public class StartClient {
    public static void startLoad() {
        new Thread(() -> {
            while (true) {
                try {

                    Thread.sleep(2000);

                    Client client = new Client();
                    new Thread(client).start();

                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}