package com.rsa;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RSA {

    private KeyPair keyPair;

    public RSA(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    /**
     * this method accepts ascii strings only
     *
     * @param plainText an ascii string to encrypt
     * @return the cipherText
     */
    public String encrypt(String plainText) {
        // convert the plain text to a number
        BigInteger m = new BigInteger(plainText.getBytes(StandardCharsets.US_ASCII));

        BigInteger c = encrypt(m);

        // convert the encrypted number to base64 to make it copyable
        return Base64.getEncoder().encodeToString(c.toByteArray());
    }

    /**
     *
     * @param cipherText
     * @return the plainText
     */
    public String decrypt(String cipherText){
        BigInteger c = new BigInteger(Base64.getDecoder().decode(cipherText));

        BigInteger m = decrypt(c);

        return new String(m.toByteArray(), StandardCharsets.US_ASCII);
    }

    /**
     *
     * @param m is the number to apply the rsa encryption primitive to.
     * @return the encrypted number
     */
    public BigInteger encrypt(BigInteger m){
        PublicKey publicKey = keyPair.getPublicKey();

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
     * @return the decrypted number
     */
    public BigInteger decrypt(BigInteger c){
        PrivateKey privateKey = keyPair.getPrivateKey();

        // make sure 0 <= c < n holds
        if(c.signum() == -1 || c.compareTo(privateKey.getModulus()) >= 0)
            throw new IllegalArgumentException("Argument c must be in the interval [0,n)");

        // (c^d) % n = m
        return c.modPow(
                privateKey.getPrivateExponent(),
                privateKey.getModulus()
        );
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}
