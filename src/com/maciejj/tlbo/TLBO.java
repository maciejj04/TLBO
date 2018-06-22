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
    private double currentBestPolynomialFitValue = Double.MAX_VALUE;

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
        IntStream.range(0, polynomials.size()).forEach(i -> {
//            Polynomial polynomial = polynomials.get(i);
            Polynomial newSolution = new Polynomial(getDegree());

            IntStream.range(0, getDegree()).forEach(j -> {
                double[] coefficientsMeans = calculateCoefficientsMean();
                double Tf = randomIntInRange(1, 2);
                double differenceMean = randomGenerator.nextDouble() * (currentBestPolynomial.getCoefficient(j) - Tf * coefficientsMeans[j]);

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
                currentBestPolynomialFitValue = correlator.correlate(currentBestPolynomial.getY());
            }

        });

    }

    private double[] calculateCoefficientsMean() {
        double[] means = new double[getDegree()];
        polynomials.forEach((p) -> {
            Double[] coefficients = p.getCoefficients();
            for (int i = 0; i < means.length; i++) {
                means[i] += coefficients[i];
            }
        });

        return stream(means).map(d -> d / polynomials.size()).toArray();
    }

    Consumer<Consumer<Integer>> forEachCoefficient = (consumer) -> {
        IntStream.range(0, getDegree()).forEach(consumer::accept);
    };

    private void learnerPhase() {
        DataSetCorrelator correlator = new DataSetCorrelator(Resources.getY());
        Random randomGenerator = new Random();
        for (Polynomial p : polynomials) {
            Polynomial randomPolynomial;
            while ((randomPolynomial = polynomials.get(randomIntInRange(0, polynomials.size() - 1))) == p);

            double randomPolymonialFitValue = correlator.correlate(randomPolynomial.getY());
            double currentPolymonialFitvalue = correlator.correlate(p.getY());
            Polynomial newSolution = new Polynomial(getDegree());
            if (randomPolymonialFitValue < currentPolymonialFitvalue) {
                forEachCoefficient.accept((i) ->
                        newSolution.updateCoefficient(i, p.getCoefficient(i) + randomGenerator.nextDouble() * (p.getCoefficient(i) - randomPolynomial.getCoefficient(i))));
            } else {
                forEachCoefficient.accept((i) ->
                        newSolution.updateCoefficient(i, p.getCoefficient(i) + randomGenerator.nextDouble() * (randomPolynomial.getCoefficient(i) - (p.getCoefficient(i)))));
            }
            newSolution.recalculateYValues();
            // TODO: last if

        }

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

    private Integer randomIntInRange(int from, int to) {
        return current().nextInt(from, to + 1);
    }
}
