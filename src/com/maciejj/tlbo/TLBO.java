package com.maciejj.tlbo;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.maciejj.tlbo.DataSetCorrelator.correlate;
import static com.maciejj.tlbo.Resources.getDegree;
import static java.util.Arrays.stream;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.toList;

class TLBO {

    private List<Polynomial> polynomials;
    private double threshold;
    private Polynomial currentBestPolynomial = null;
    private Double currentBestPolynomialFitValue = Double.MAX_VALUE;

    TLBO(int populaion, double threshold) {
        polynomials = Stream.generate(() -> new Polynomial(6)).limit(populaion).collect(toList());
        this.threshold = threshold;
    }

    Polynomial runAlgorythm() throws Exception {
        while (correlate(Resources.getY(), (currentBestPolynomial = findBestPolynomial()).getY()) > threshold) {
            assert currentBestPolynomial != null;
            teacherPhase();
            learnerPhase();
        }

        currentBestPolynomial.recalculateYValues();
        System.out.println("Best fitted polnomial fit value = " + currentBestPolynomialFitValue);
        return currentBestPolynomial;
    }

    private void teacherPhase() {
        Random randomGenerator = new Random();
        IntStream.range(0, getPopulationCount()).forEach(i -> {

            Polynomial newSolution = new Polynomial(getDegree());

            IntStream.range(0, getDegree()).forEach(j -> {
                double Tf = randomIntInRange(1, 2);
                double differenceMean = randomGenerator.nextDouble() * (currentBestPolynomial.getCoefficient(j) - Tf * calculateCoefficientsMean()[j]);

                newSolution.updateCoefficient(j, polynomials.get(i).getCoefficient(j) + differenceMean);
            });

            newSolution.recalculateYValues();
            // TODO: can be optimized:
            //                  - keep fit value inside Polymonial (but this is against Single responsibility principle) or
            //                  - keep Polynomials objects in Tuple/Map with it's fit value inside of TLBO class.

            DataSetCorrelator correlator = new DataSetCorrelator(Resources.getY());
            double newSolutionFit = correlator.correlate(newSolution.getY());

            if (newSolutionFit < currentBestPolynomialFitValue) {
                polynomials.set(i, newSolution);
                currentBestPolynomial = newSolution;
                currentBestPolynomialFitValue = newSolutionFit;
            }
        });
    }

    private void learnerPhase() {
        DataSetCorrelator correlator = new DataSetCorrelator(Resources.getY());
        Random randomGenerator = new Random();
        for (int i = 0; i < getPopulationCount(); i++) {
            Polynomial p = polynomials.get(i);
            Polynomial randomPoly;
            while ((randomPoly = polynomials.get(randomIntInRange(0, getPopulationCount() - 1))) == p) ;
            final Polynomial randomPolynomial = randomPoly;

            double randomPolymonialFitValue = correlator.correlate(randomPolynomial.getY());
            double currentPolymonialFitvalue = correlator.correlate(p.getY());
            Polynomial newSolution = new Polynomial(getDegree());
            if (randomPolymonialFitValue < currentPolymonialFitvalue) {
                forEachCoefficient.accept((j) ->
                        newSolution.updateCoefficient(j, p.getCoefficient(j) + randomGenerator.nextDouble() * (randomPolynomial.getCoefficient(j) - (p.getCoefficient(j)))));
            } else {
                forEachCoefficient.accept((j) ->
                        newSolution.updateCoefficient(j, p.getCoefficient(j) + randomGenerator.nextDouble() * (p.getCoefficient(j) - randomPolynomial.getCoefficient(j))));
            }
            newSolution.recalculateYValues();
            Double newSolutionFit = correlator.correlate(newSolution.getY());

            if (newSolutionFit < currentPolymonialFitvalue) {
                polynomials.set(i, newSolution);
            }
        }
    }

    private double[] calculateCoefficientsMean() {
        double[] means = new double[getDegree()];
        polynomials.forEach((p) -> {
            Double[] coefficients = p.getCoefficients();
            for (int i = 0; i < means.length; i++) {
                means[i] += coefficients[i];
            }
        });

        return stream(means).map(d -> d / getPopulationCount()).toArray();
    }

    private Consumer<Consumer<Integer>> forEachCoefficient = (consumer) -> IntStream.range(0, getDegree()).forEach(consumer::accept);

    private int getPopulationCount() {
        return polynomials.size();
    }

    private Polynomial findBestPolynomial() throws Exception {
        DataSetCorrelator correlator = new DataSetCorrelator(Resources.getY());

        return polynomials.stream()
                .map(p -> new Object[]{correlator.correlate(p.getY()), p})
                .min(Comparator.comparingDouble(o -> (Double) o[0]))
                .map(o -> {
                    currentBestPolynomialFitValue = (Double) o[0];
                    return (Polynomial) o[1];
                })
                .orElseThrow(Exception::new);
    }

    private Integer randomIntInRange(int from, int to) {
        return current().nextInt(from, to + 1);
    }
}
