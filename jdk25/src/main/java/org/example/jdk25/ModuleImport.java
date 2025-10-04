package org.example.jdk25;

import module java.base;

public class ModuleImport {

    // No need for individual imports of java.lang classes
    public static void main(String[] args) {
        String message = "Hello, Module System!";
        System.out.println(message);
        Date d = new Date();

    }
}
