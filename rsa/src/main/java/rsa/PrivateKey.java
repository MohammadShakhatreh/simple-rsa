package rsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            bw.write(this.privateExponent.toString(16));
            bw.newLine();
            bw.write(this.modulus.toString(16));
        }
    }

    /**
     * Imports the key from a file
     * @param filename
     */
    public static PrivateKey load(String filename) throws IOException {
        BigInteger privateExponent, modulus;
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {
            privateExponent = new BigInteger(br.readLine(), 16);
            modulus = new BigInteger(br.readLine(), 16);
        }

        return new PrivateKey(privateExponent, modulus);
    }

    public BigInteger getPrivateExponent() {
        return privateExponent;
    }

    public BigInteger getModulus() {
        return modulus;
    }

    @Override
    public String toString() {

        int digits = 20;

        String mod = modulus.toString(16);
        mod = mod.substring(0, Math.min(mod.length(), digits)) + (mod.length() > digits ? "..." : "");

        String pe = privateExponent.toString(16);
        pe = pe.substring(0, Math.min(pe.length(), digits)) + (pe.length() > digits ? "..." : "");

        return  "\t\tModulus = " + mod + '\n' +
                "\t\tPrivate exponent = " + pe + '\n';
    }
}
