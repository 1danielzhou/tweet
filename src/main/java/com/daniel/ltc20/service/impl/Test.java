package com.daniel.ltc20.service.impl;

import java.io.*;

public class Test {
    public static void main(String[] args) {
        String inputFilePath = "words.txt";
        String outputFilePath = "all_words.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] s = line.split(" ");
                for (String s1 : s) {
                    writer.write(s1);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
