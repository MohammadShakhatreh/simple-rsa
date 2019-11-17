package com.rsa;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Main {

    public static void main(String[] args) {
        try(BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.txt")))) {
            br.write("Hello");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
