package com.rsa;

import java.math.BigInteger;

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
