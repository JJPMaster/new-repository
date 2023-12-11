package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 1234;

        try (Socket socket = new Socket(hostname, port)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            String serverResponse = reader.readLine();
            System.out.println(serverResponse);

            while (true) {
                System.out.print("Enter your guess: ");
                int guess = scanner.nextInt();
                writer.println(guess);

                serverResponse = reader.readLine();
                System.out.println(serverResponse);

                if (serverResponse.equalsIgnoreCase("Correct!")) {
                    break;
                }
            }

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
