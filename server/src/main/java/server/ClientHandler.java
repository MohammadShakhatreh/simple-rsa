package server;

import rsa.PublicKey;
import rsa.RSA;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class ClientHandler implements Runnable {

    private Socket s;
    private String name;


    public ClientHandler(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));

            //Keys Exchange
            String pk = br.readLine();

            String[] tmp = pk.split(" ");
            PublicKey clientPub = new PublicKey(
                    new BigInteger(tmp[0]),
                    new BigInteger(tmp[1])
            );

            bw.write(Server.keyPair.publicKey + "\r\n");
            bw.flush();

            //User Auth
            SecureRandom rand = new SecureRandom();

            long r = rand.nextLong();
            bw.write(r + "\r\n");
            bw.flush();

            String token = br.readLine();
            byte[] c = Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8));
            byte[] m = RSA.decrypt(Server.keyPair.privateKey, c);

            tmp = new String(m, StandardCharsets.UTF_8).split(" ");

            if(Long.parseLong(tmp[1]) != r){
                bw.write("Auth failed!\r\n");
                bw.flush();
                System.exit(-1);
            } else {
                bw.write("Auth succeeded");
                bw.flush();
            }

            this.name = tmp[0];

            String line;
            while((line = br.readLine()) != null) {
                byte[] c = Base64.getDecoder().decode(line);

                long t0 = System.currentTimeMillis();
                byte[] m = RSA.decrypt(Server.keyPair.privateKey, c);
                long t1 = System.currentTimeMillis();

                System.out.println("Time to decrypt: " + (t1 - t0) + "ms");
                System.out.println(new String(m, StandardCharsets.UTF_8));
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                s.close();
            } catch (IOException e){}
        }
    }
}
