package server;

import rsa.KeyPair;
import rsa.PublicKey;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Our Application layer protocol is as follows:
 *
 * Headers:
 *
 * Content-Type: [file, reg, auth]
 * this is a required header
 * -- file: the data section will be a file and the server will resend it to all other active clients
 * -- reg: the data section is a regular message the server will print it
 * -- auth: the data section will be client username
 *
 * Content-Length: length
 * data section length in bytes
 *
 * Filename: filename
 * used with (file) header to indicate the file name
 *
 *
 */
public class Server {

    public static KeyPair keyPair;

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

        keyPair = KeyPair.generate();

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
