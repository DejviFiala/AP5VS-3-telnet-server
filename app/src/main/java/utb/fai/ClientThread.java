package utb.fai;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(clientSocket.getOutputStream())
            )
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                // Odeslání přijaté zprávy zpět klientovi (Echo)
                out.write(message);
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Klient odpojen: " + clientSocket.getInetAddress());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Chyba při uzavírání socketu.");
                e.printStackTrace();
            }
        }
    }
}
