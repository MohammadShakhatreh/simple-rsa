package client;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import rsa.KeyPair;
import rsa.PublicKey;
import rsa.RSA;

import java.util.Base64;
import java.util.Scanner;

public class Client {

    /**
     * Usage: ./client host port
     * @param args
     */
    public static void main(String[] args) {
        if(args.length < 3) {
            System.err.println("Usage: ./client host port username");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        String username = args[2];

        KeyPair keyPair = KeyPair.generate();

        try (Socket s = new Socket(host, port)) {

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter serverWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));

            // Key exchange
            serverWriter.write(keyPair.publicKey + "\r\n");
            serverWriter.flush();

            String pk = serverReader.readLine();

            String[] tmp = pk.split(" ");
            PublicKey serverPub = new PublicKey(
                    new BigInteger(tmp[0]),
                    new BigInteger(tmp[1])
            );


            //User Authentication
            String r = serverReader.readLine();
            String token = username + " " + r;

            byte[] c = RSA.encrypt(serverPub, token.getBytes(StandardCharsets.UTF_8));

            serverWriter.write(Base64.getEncoder().encodeToString(c) + "\r\n");
            serverWriter.flush();

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                line = br.readLine();
                System.out.println(line);
                byte[] c = RSA.encrypt(serverPub, line.getBytes(StandardCharsets.UTF_8));

                serverWriter.write(Base64.getEncoder().encodeToString(c) + '\n');
                serverWriter.flush();
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
