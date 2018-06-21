package com.maciejj.tlbo;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Resources {

    private static final List<Double> x = unmodifiableList(new LinkedList<>(
            Arrays.asList(0.0, 264.808356, 509.685126, 695.405297, 895.918182, 1091.478836, 1211.497785, 1342.528998, 1418.860884, 1529.883628, 1609.157774, 1672.253403, 1801.11707, 1910.151466, 2014.215536, 2122.619985, 2229.639395, 2351.761252, 2420.075621, 2514.104385, 2580.05268, 2699.786064, 2842.944259, 2938.825163, 3040.748212, 3189.473079, 3224.573087, 3314.859206, 3429.522837, 3536.26652, 3632.65348, 3716.482798, 3850.463706, 3932.49201, 4001.085077, 4083.191328, 4196.606914, 4322.520755, 4407.749683, 4511.417009, 4715.589753, 4800.943963, 4971.719275, 5038.990663, 5101.333902, 5237.197969, 5326.119588, 5431.893972, 5508.555286, 5613.136854)
    ));

    private static final List<Double> y = unmodifiableList(new LinkedList<>(
            Arrays.asList(0.386, 0.394, 0.402, 0.41, 0.418, 0.426, 0.434, 0.463, 0.487, 0.43, 0.492, 0.534, 0.63, 0.567, 0.54, 0.59, 0.776, 1.048, 1.061, 1.073, 1.121, 1.319, 1.307, 1.311, 1.357, 1.436, 1.472, 1.45, 1.445, 1.396, 1.328, 1.358, 1.387, 1.318, 1.253, 1.136, 1.19, 1.139, 1.045, 0.628, 0.839, 0.786, 0.743, 0.729, 0.72, 0.528, 0.48, 0.706, 0.788, 0.784)
    ));

    private static int degree = 6;

    static List<Double> getXData() {
        return x;
    }

    static List<Double> getY() {
        return y;
    }

    static int getDegree() {
        return degree;
    }

}
