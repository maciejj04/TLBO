package com.maciejj.tlbo;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        TLBO tlbo = new TLBO(100, 5.0);
        Polynomial bestPolynomial;
        try {
            bestPolynomial = tlbo.runAlgorythm();
            System.out.println("Polynommial coefficients: " + Arrays.toString(bestPolynomial.getCoefficients()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
