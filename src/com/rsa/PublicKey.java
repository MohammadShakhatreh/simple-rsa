package com.rsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PublicKey {
    private BigInteger modulus;
    private BigInteger publicExponent;

    public PublicKey(BigInteger modulus, BigInteger publicExponent) {
        this.modulus = modulus;
        this.publicExponent = publicExponent;
    }

    public BigInteger getPublicExponent() {
        return publicExponent;
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
            bw.write(this.publicExponent.toString(16));
        }
    }

    /**
     * Imports the key from a file
     * @param filename
     */
    public void load(String filename) throws IOException {
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {
            this.modulus = new BigInteger(br.readLine(), 16);
        }
    }

    @Override
    public String toString() {

        int digits = 20;

        String mod = modulus.toString();
        mod = mod.substring(0, Math.min(mod.length(), digits)) + (mod.length() > digits ? "..." : "");

        String pe = publicExponent.toString();
        pe = pe.substring(0, Math.min(pe.length(), digits)) + (pe.length() > digits ? "..." : "");

        return  "\t\tModulus = " + mod + '\n' +
                "\t\tPublic exponent = " + pe + '\n';
    }
}