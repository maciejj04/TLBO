package com.maciejj.tlbo;

public class Main {

    public static void main(String[] args) {
        TLBO tlbo = new TLBO(100, 5.0);
        try {
            tlbo.runAlgorythm();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("git");
    }
}
