package com.daniel.ltc20.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class FileUtils {
    public static boolean writeToFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content+"\n");
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            return false;
        }
    }
}
