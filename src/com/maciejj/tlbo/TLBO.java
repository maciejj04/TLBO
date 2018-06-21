package com.maciejj.tlbo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.maciejj.tlbo.DataSetCorrelator.correlate;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class TLBO {

    private List<Polynomial> polynomials;
    private double threshold;
    private Polynomial currentBestPolynomial = null;


    TLBO(int populaion, double threshold) {
        polynomials = Stream.generate(()->new Polynomial(6)).limit(populaion).collect(toList());
        this.threshold = threshold;
    }

    void runAlgorythm() throws Exception {

        while (correlate(Resources.getY(), (currentBestPolynomial = findBestPolynomial()).getY()) > threshold){
            assert currentBestPolynomial != null;
            teacherPhase();
            learnerPhase();
        }

    }
    private void teacherPhase() {}
    private void learnerPhase() {}

    private int getPopulationCount(){
        return polynomials.size();
    }

    private Polynomial findBestPolynomial() throws Exception {
        DataSetCorrelator correlator = new DataSetCorrelator(Resources.getY());

        return polynomials.stream()
                .map(p -> new Object[] {correlator.correlate(p.getY()), p})
                .min(Comparator.comparingDouble(o -> (Double) o[0]))
                .map(o -> (Polynomial) o[1])
                .orElseThrow(Exception::new);

    }
}
