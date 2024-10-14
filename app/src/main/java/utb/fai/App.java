package utb.fai;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Použití: java App <port> <max_threads>");
            return;
        }

        int port;
        int maxThreads;

        try {
            port = Integer.parseInt(args[0]);
            maxThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Neplatný port nebo počet vláken.");
            return;
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(maxThreads);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server běží na portu " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nové připojení od " + clientSocket.getInetAddress());

                    ClientThread clientThread = new ClientThread(clientSocket);
                    threadPool.execute(clientThread);
                } catch (IOException e) {
                    System.out.println("Chyba při přijímání spojení.");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Chyba při vytváření ServerSocketu.");
            e.printStackTrace();
        } finally {

            threadPool.shutdown();
        }
    }
}
