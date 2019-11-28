package rsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class PrivateKey {
    private BigInteger privateExponent;
    private BigInteger modulus;

    public PrivateKey(BigInteger privateExponent, BigInteger modulus) {
        this.privateExponent = privateExponent;
        this.modulus = modulus;
    }

    /**
     * Exports the key into a file and it will be in the current directory
     * if there is a file with the same name it'll be overridden
     * @param filename
     */
    public void save(String filename) throws IOException {
        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(filename))) {
            String d = Base64.getEncoder().encodeToString(this.privateExponent.toByteArray());
            String n = Base64.getEncoder().encodeToString(this.modulus.toByteArray());

            bw.write(d);
            bw.newLine();
            bw.write(n);
        }
    }

    /**
     * Imports the key from a file
     * @param filename
     */
    public static PrivateKey load(String filename) throws IOException {
        BigInteger d, n;
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {

            d = new BigInteger(Base64.getDecoder().decode(br.readLine()));
            n = new BigInteger(Base64.getDecoder().decode(br.readLine()));
        }

        return new PrivateKey(d, n);
    }

    public BigInteger getPrivateExponent() {
        return privateExponent;
    }

    public BigInteger getModulus() {
        return modulus;
    }

    @Override
    public String toString() {
        return this.privateExponent + " " + this.modulus;
    }
}
