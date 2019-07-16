package com.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

class KeyPair {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    private KeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * generate a key pair with a reasonable defaults
     */
    public static KeyPair generate() {
        return generate(BigInteger.valueOf(65537), BigInteger.ONE.shiftLeft(256), 2048);
    }

    /**
     *
     * @param e public exponent
     * @param primesDifference difference between p and q to keep in mind while generating the primes
     * @param keySize size of the key ie. the modulus (n)
     *
     * @return new Keypair instance with the generated key pair
     */
    public static KeyPair generate(BigInteger e, BigInteger primesDifference, int keySize){

        if(!e.testBit(0))
            throw new IllegalArgumentException("Public exponent e must be odd");

        if(keySize < 1024)
            throw new IllegalArgumentException("keySize must be at least 1024");

        SecureRandom rnd = new SecureRandom();

        BigInteger p = new BigInteger(keySize / 2, 100, rnd);
        BigInteger q = new BigInteger(keySize / 2, 100, rnd);

        // phi = (p-1) * (q-1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // |p - q| <= primesDifference && gcd(e, phi) == 1
        while(p.subtract(q).abs().compareTo(primesDifference) <= 0 || !phi.gcd(e).equals(BigInteger.ONE)) {

            q = new BigInteger(keySize / 2, 100, rnd);
            phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        }

        // (p * q) is the modulus
        BigInteger n = p.multiply(q);

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

