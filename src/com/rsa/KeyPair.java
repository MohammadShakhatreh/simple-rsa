package com.rsa;

import java.math.BigInteger;
import java.nio.file.Path;
import java.security.SecureRandom;

class KeyPair {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    private KeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Generate a key pair with a reasonable defaults
     */
    public static KeyPair generate() {
        return generate(BigInteger.valueOf(65537), 2048);
    }

    /**
     *
     * Generate a key pair
     * @param e public exponent
     * @param keySize size of the key ie. the modulus n
     *
     * @return new Keypair instance with the generated key pair
     */
    public static KeyPair generate(BigInteger e, int keySize) {

        if(e.compareTo(BigInteger.ONE) <= 0)
            throw new IllegalArgumentException("Public exponent e must be greater than one");

        SecureRandom rnd = new SecureRandom();

        BigInteger p, q, n, phi;

        p = new BigInteger(keySize / 2, 100, rnd);

        do {
            q = new BigInteger(keySize / 2, 100, rnd);

            // phi = (p-1) * (q-1)
            phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

            // (p * q) is the modulus
            n = p.multiply(q);

            // |p - q| ^ 3 <= n || gcd(e, phi) != 1
        } while (p.subtract(q).abs().pow(3).compareTo(n) <= 0 || !phi.gcd(e).equals(BigInteger.ONE));

        // (e^(-1) % phi) is private exponent
        BigInteger d = e.modInverse(phi);

        PublicKey publicKey = new PublicKey(n, e);
        PrivateKey privateKey = new PrivateKey(n, d);

        return new KeyPair(publicKey, privateKey);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public String toString() {
        return "Key pair:\n" +
                "\tPublic key:\n" + publicKey + '\n' +
                "\tPrivate key:\n" + privateKey;
    }
}

