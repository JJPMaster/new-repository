package org.example;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 1234; // Port number
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("New client connected");

                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    Scanner scanner = new Scanner(socket.getInputStream());

                    Random random = new Random();
                    int randomNumber = random.nextInt(100) + 1; // Random number between 1 and 100
                    int guess;
                    boolean correct = false;

                    writer.println("Guess a number between 1 and 100");

                    while (!correct) {
                        guess = scanner.nextInt();
                        if (guess < randomNumber) {
                            writer.println("Higher");
                        } else if (guess > randomNumber) {
                            writer.println("Lower");
                        } else {
                            writer.println("Correct!");
                            correct = true;
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Server exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }
}
