package rsa;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RSA {

    /**
     * Make the class as a utility class
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

        return toByteArray(c);
    }

    /**
     *
     * @param cipherText
     * @return plainText
     */
    public static byte[] decrypt(PrivateKey privateKey, byte[] cipherText){
        BigInteger c = new BigInteger(1, cipherText);

        BigInteger m = decrypt(privateKey, c);

        return toByteArray(m);
    }

    /**
     *
     * @param m is the number to apply the rsa encryption primitive to.
     * @return the encrypted number c
     */
    public static BigInteger encrypt(PublicKey publicKey, BigInteger m){
        // make sure 0 <= m < n holds
        if(m.signum() == -1 || m.compareTo(publicKey.getModulus()) >= 0)
            throw new IllegalArgumentException("Argument m must be in the interval [0, n)");

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
            throw new IllegalArgumentException("Argument c must be in the interval [0, n)");

        // (c^d) % n = m
        return c.modPow(
                privateKey.getPrivateExponent(),
                privateKey.getModulus()
        );
    }

    /**
     * Remove leading zero byte that BigInteger added
     * when converting the message bytes to a positive number
     */
    private static byte[] toByteArray(BigInteger bi) {
        byte[] b = bi.toByteArray();

        // The original message is positive by default
        if (b[0] != 0x00)
            return b;

        byte[] res = new byte[b.length - 1];
        System.arraycopy(b, 1, res, 0, res.length);

        return res;
    }

    public static void main(String[] args) {

        long t0 = System.currentTimeMillis();
        KeyPair kp = KeyPair.generate();
        long t1 = System.currentTimeMillis();

        System.out.println("Time to generate 2048-bit key pair: " + (t1 - t0) + "ms");

        String plainText = "Hello, World!";
        System.out.println("Plain text: " + plainText);

        t0 = System.currentTimeMillis();
        byte[] c = RSA.encrypt(kp.publicKey, plainText.getBytes(StandardCharsets.UTF_8));
        t1 = System.currentTimeMillis();

        System.out.println("Cipher text in hex: " + new BigInteger(c).toString(16));
        System.out.println("Time to encrypt is: " + (t1 - t0) + "ms");

        t0 = System.currentTimeMillis();
        byte[] m = RSA.decrypt(kp.privateKey, c);
        t1 = System.currentTimeMillis();

        System.out.println("Plain text: " + new String(m, StandardCharsets.UTF_8));
        System.out.println("Time to decrypt is: " + (t1 - t0) + "ms");
    }
}
