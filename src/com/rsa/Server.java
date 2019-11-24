package com.rsa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    // Array of connected clients
    public static Vector<ClientHandler> clients = new Vector<>();

    /**
     * Usage: ./server port
     * @param args
     */
    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("Usage: ./server port");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try(ServerSocket soc = new ServerSocket(port)) {

            // Server loop
            while(true) {
                // Accept new connections
                Socket s = soc.accept();
                System.out.println("New connection: " + s);

                ClientHandler clientHandler = new ClientHandler(s);
                clients.add(clientHandler);

                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
