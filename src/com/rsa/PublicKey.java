package com.rsa;

import java.math.BigInteger;

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