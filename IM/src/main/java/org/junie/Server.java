package org.junie;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server is running...");
        ServerSocket listener = new ServerSocket(PORT);

        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String username;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Ask for a unique username
                while (true) {
                    out.println("SUBMITNAME");
                    username = in.readLine();
                    if (username == null) {
                        return;
                    }
                    synchronized (clients) {
                        if (!clients.containsKey(username)) {
                            clients.put(username, out);
                            // Notify the server console about the new connection
                            System.out.println(username + " has connected.");
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED " + username);

                // Broadcast messages
                String input;
                while ((input = in.readLine()) != null) {
                    for (PrintWriter writer : clients.values()) {
                        writer.println("MESSAGE " + username + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (username != null) {
                    System.out.println(username + " has disconnected.");
                    clients.remove(username);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
