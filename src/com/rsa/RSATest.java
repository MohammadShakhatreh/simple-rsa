package com.rsa;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RSATest {
    public static void main(String[] args) {

        long t0 = System.currentTimeMillis();
        KeyPair kp = KeyPair.generate();
        long t1 = System.currentTimeMillis();

        System.out.println("Time to generate 2048-bit key pair: " + (t1 - t0) + "ms");

        String plainText = "Hello, babe!";
        System.out.println("Plain text: " + plainText);

        t0 = System.currentTimeMillis();
        byte[] c = RSA.encrypt(kp.getPublicKey(), plainText.getBytes(StandardCharsets.UTF_8));
        t1 = System.currentTimeMillis();

        System.out.println("Cipher text in hex: " + new BigInteger(c).toString(16));
        System.out.println("Time to encrypt is: " + (t1 - t0) + "ms");

        t0 = System.currentTimeMillis();
        byte[] m = RSA.decrypt(kp.getPrivateKey(), c);
        t1 = System.currentTimeMillis();

        System.out.println("Cipher text in hex: " + new String(m, StandardCharsets.UTF_8));
        System.out.println("Time to decrypt is: " + (t1 - t0) + "ms");
    }
}
