package client;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import rsa.KeyPair;
import rsa.PrivateKey;
import rsa.PublicKey;
import rsa.RSA;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class Client {

    // Keys for the client
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    private static PublicKey serverPublicKey;

    private static void loadKeys(String username) {
        try {
            publicKey = PublicKey.load(username);
            privateKey = PrivateKey.load(username);

            serverPublicKey = PublicKey.load("server");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Flow of the authentication process is as follows:
     * 1 - Client sends the username
     * 2 - Server sends a nonce
     * 3 - Client sends the nonce signed
     */
    private static String authenticate(String username, BufferedReader serverReader, BufferedWriter serverWriter) throws IOException {

        // Send username to server
        serverWriter.write(username + "\r\n");
        serverWriter.flush();

        // Read the nonce from server
        String nonce = serverReader.readLine();
        nonce = RSA.decrypt(privateKey, nonce);

        // Send the nonce to server signed
        serverWriter.write(nonce + "\r\n");
        serverWriter.flush();

        // Read response from server
        return serverReader.readLine();
    }

    /**
     * Usage: ./client host port username [filename]
     */
    public static void main(String[] args) {
        if(args.length < 3) {
            System.err.println("Usage: ./client host port username");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        loadKeys(username);
        try (Socket s = new Socket(host, port)) {

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter serverWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));

            // User authentication
            String res = authenticate(username, serverReader, serverWriter);
            if (!res.equals("200 OK")) {
                System.err.println(res);
                return;
            }

            // Main loop waits for user input and sends it
            // to the server encrypted
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while((line = br.readLine()) != null) {
                String encryptedMessage = RSA.encrypt(serverPublicKey, line);

                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(line.getBytes(StandardCharsets.UTF_8));

                String signedHash = RSA.sign(privateKey, Base64.getEncoder().encodeToString(hash));

                serverWriter.write(encryptedMessage + " " + signedHash + "\r\n");
                serverWriter.flush();
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
    }
}
