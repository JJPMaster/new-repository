package org.junie;

import java.io.*;
import java.net.*;

public class Client {

    private BufferedReader in;
    private PrintWriter out;

    public Client() {
    }

    private void run() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the IP Address of the Server:");
        String serverAddress = consoleReader.readLine();

        try {
            Socket socket = new Socket(serverAddress, 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String line = in.readLine();
                if (line.startsWith("SUBMITNAME")) {
                    System.out.print("Enter a unique username: ");
                    out.println(consoleReader.readLine());
                } else if (line.startsWith("NAMEACCEPTED")) {
                    System.out.println("Connected as " + line.substring(13) + ". Start chatting!");
                    break;
                }
            }

            // Thread to handle incoming messages
            new Thread(new Runnable() {
                public void run() {
                    try {
                        String line;
                        while ((line = in.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        System.out.println("Connection closed.");
                    }
                }
            }).start();

            // Main thread for sending messages
            String userInput;
            while ((userInput = consoleReader.readLine()) != null) {
                out.println(userInput);
            }
        } catch (IOException e) {
            System.out.println("Unable to connect to server: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.run();
    }
}
