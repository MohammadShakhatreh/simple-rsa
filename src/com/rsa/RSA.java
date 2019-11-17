package com.rsa;

import java.math.BigInteger;

public class RSA {

    /**
     * This constructor is private so that no one can make an
     * instance of this class
     */
    private RSA() {}

    /**
     *
     * @param plainText
     * @return the cipherText
     */
    public static byte[] encrypt(PublicKey publicKey, byte[] plainText) {

        BigInteger m = new BigInteger(1, plainText);
        BigInteger c = encrypt(publicKey, m);

        return c.toByteArray();
    }

    /**
     *
     * @param cipherText
     * @return plainText
     */
    public static byte[] decrypt(PrivateKey privateKey, byte[] cipherText){
        // Convert the cipherText to a positive number
        BigInteger c = new BigInteger(cipherText);

        BigInteger m = decrypt(privateKey, c);

        return m.toByteArray();
    }

    /**
     *
     * @param m is the number to apply the rsa encryption primitive to.
     * @return the encrypted number c
     */
    public static BigInteger encrypt(PublicKey publicKey, BigInteger m){
        // make sure 0 <= m < n holds
        if(m.signum() == -1 || m.compareTo(publicKey.getModulus()) >= 0)
            throw new IllegalArgumentException("Argument m must be in the interval [0,n)");

        // (m^e) % n = c
        return m.modPow(
                publicKey.getPublicExponent(),
                publicKey.getModulus()
        );
    }

    /**
     *
     * @param c the encrypted number to apply the rsa decryption primitive to.
     * @return the decrypted number m
     */
    public static BigInteger decrypt(PrivateKey privateKey, BigInteger c){
        // make sure 0 <= c < n holds
        if(c.signum() == -1 || c.compareTo(privateKey.getModulus()) >= 0)
            throw new IllegalArgumentException("Argument c must be in the interval [0,n)");

        // (c^d) % n = m
        return c.modPow(
                privateKey.getPrivateExponent(),
                privateKey.getModulus()
        );
    }
}
