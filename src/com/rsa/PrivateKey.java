package com.rsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PrivateKey {
    private BigInteger modulus;
    private BigInteger privateExponent;

    public PrivateKey(BigInteger modulus, BigInteger privateExponent) {
        this.modulus = modulus;
        this.privateExponent = privateExponent;
    }

    public BigInteger getPrivateExponent() {
        return privateExponent;
    }

    public BigInteger getModulus() {
        return modulus;
    }

    /**
     * Exports the key into a file and it will be in the current directory
     * if there is a file with the same name it'll be overridden
     * @param filename
     */
    public void save(String filename) throws IOException {
        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(filename))) {
            bw.write(this.modulus.toString(16));
            bw.newLine();
            bw.write(this.privateExponent.toString(16));
        }
    }

    /**
     * Imports the key from a file
     * @param filename
     */
    public static PrivateKey load(String filename) throws IOException {
        BigInteger modulus, privateExponent;
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {
            modulus = new BigInteger(br.readLine(), 16);
            privateExponent = new BigInteger(br.readLine(), 16);
        }

        return new PrivateKey(modulus, privateExponent);
    }


    @Override
    public String toString() {

        int digits = 20;

        String mod = modulus.toString();
        mod = mod.substring(0, Math.min(mod.length(), digits)) + (mod.length() > digits ? "..." : "");

        String pe = privateExponent.toString();
        pe = pe.substring(0, Math.min(pe.length(), digits)) + (pe.length() > digits ? "..." : "");

        return  "\t\tModulus = " + mod + '\n' +
                "\t\tPrivate exponent = " + pe + '\n';
    }
}
