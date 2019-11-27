package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    /**
     * Usage: ./client host port
     * @param args
     */
    public static void main(String[] args) {
        if(args.length < 2) {
            System.err.println("Usage: ./client host port");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket s = new Socket(host, port)) {

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter serverWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                line = br.readLine();
                serverWriter.write(line + "\r\n");
                serverWriter.flush();
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
