package com.rsa;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        long t0 = System.currentTimeMillis();
        KeyPair keyPair = KeyPair.generate();
        long t1 = System.currentTimeMillis();

        System.out.println("Time to generate the keyPair pair: " + (t1 - t0) + "ms");

        RSA rsa = new RSA(keyPair);

        BigInteger m = new BigInteger("555556666555568888");

        System.out.println("Plain text: " + m);

        BigInteger c = rsa.encrypt(m);
        System.out.println("Cipher text: " + c);
        System.out.println("Decrypted text: " + rsa.decrypt(c));

        String s = "Hello, world!!";

        t0 = System.currentTimeMillis();
        String cipherText = rsa.encrypt(s);

        System.out.println("Cipher text is " + cipherText);
        System.out.println("Plain text is " + rsa.decrypt(cipherText));
        t1 = System.currentTimeMillis();

        System.out.println("Time to encrypt and decrypt is: " + (t1 - t0) + "ms");
    }
}
