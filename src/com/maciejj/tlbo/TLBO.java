package com.maciejj.tlbo;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.maciejj.tlbo.DataSetCorrelator.correlate;
import static com.maciejj.tlbo.Resources.getDegree;
import static java.lang.Math.*;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class TLBO {

    private List<Polynomial> polynomials;
    private double threshold;
    private Polynomial currentBestPolynomial = null;

    TLBO(int populaion, double threshold) {
        polynomials = Stream.generate(() -> new Polynomial(6)).limit(populaion).collect(toList());
        this.threshold = threshold;
    }

    void runAlgorythm() throws Exception {
        while (correlate(Resources.getY(), (currentBestPolynomial = findBestPolynomial()).getY()) > threshold) {
            assert currentBestPolynomial != null;
            teacherPhase();
            learnerPhase();
        }
    }

    private void teacherPhase() {
        Random randomGenerator = new Random();
        for (int i = 0; i < polynomials.size(); i++) {
            Polynomial newSolution = new Polynomial(getDegree());

            IntStream.range(0, getDegree()).forEach(j -> {
                double Tf = randomIntInRange(1,2);

                double differenceMean = randomGenerator.nextDouble() * (currentBestPolynomial.getCoefficient(j) - Tf*GetMeanOfDesignVariable(j));

                temporaryNewSolution.SetParam(j, functions[k].GetParam(j) + differenceMean);

            });

        }

    }

    private void learnerPhase() {
    }

    private int getPopulationCount() {
        return polynomials.size();
    }

    private Polynomial findBestPolynomial() throws Exception {
        DataSetCorrelator correlator = new DataSetCorrelator(Resources.getY());

        return polynomials.stream()
                .map(p -> new Object[]{correlator.correlate(p.getY()), p})
                .min(Comparator.comparingDouble(o -> (Double) o[0]))
                .map(o -> (Polynomial) o[1])
                .orElseThrow(Exception::new);

    }

    private Integer randomIntInRange(int from, int to){
        return current().nextInt(from,to+1);
    }
}
