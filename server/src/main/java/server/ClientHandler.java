package server;

import rsa.PublicKey;
import rsa.RSA;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String username;
    private PublicKey publicKey;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private String authenticate(BufferedReader clientReader, BufferedWriter clientWriter) throws IOException {
        SecureRandom rand = new SecureRandom();

        // Read client username and load it's public key
        this.username = clientReader.readLine();
        this.publicKey = PublicKey.load(username);

        // Server sends a nonce encrypted
        String nonce = Long.toString(rand.nextLong());
        clientWriter.write(RSA.encrypt(publicKey, nonce) + "\r\n");
        clientWriter.flush();

        // Client sends the nonce
        String returnedNonce = clientReader.readLine();

        String response = "200 OK";

        // See if the nonce from client equals the server nonce
        if(!nonce.equals(returnedNonce)){
            response = "401 Unauthorized";
        }

        clientWriter.write(response + "\r\n");
        clientWriter.flush();

        return response;
    }

    @Override
    public void run() {

        try {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            String response = authenticate(clientReader, clientWriter);

            // if client is not authenticated exit
            if(!response.equals("200 OK"))
                return;

            String request;
            while((request = clientReader.readLine()) != null) {
                String[] message = request.split(" ");

                request = RSA.decrypt(Server.privateKey, message[0]);
                String requestHash = RSA.unSign(publicKey, message[1]);

                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] d = digest.digest(request.getBytes(StandardCharsets.UTF_8));

                System.out.println(username + ": " + request);
                if(!requestHash.equals(Base64.getEncoder().encodeToString(d))){
                    System.err.println("Message verification failed!");
                }
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } finally {
            // Remove myself from client vector
            Server.clients.remove(this);

            // Close the connection
            try {
                socket.close();
            } catch (IOException ignored){}
        }
    }
}
