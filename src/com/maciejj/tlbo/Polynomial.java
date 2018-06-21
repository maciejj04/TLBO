package com.maciejj.tlbo;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.generate;

class Polynomial {

    private final List<Double> x;
    private List<Double> y;
    private Double[] coefficients;

    Polynomial(int degree) {
        this.x = Resources.getXData();
        this.y = new LinkedList<>(Stream.generate(() -> 0d).limit(x.size()).collect(Collectors.toList()));
        coefficients = drawCoefficients(degree);
        recalculateY();
    }

    private void recalculateY() {
        for(int i = 0; i < x.size(); i++){
            y.set(i, calculateY(x.get(i)));
        }
    }

    private Double calculateY(Double x){
        double result = coefficients[0];
        for(int i = 1 ; i < coefficients.length; i++) {
            result = result*x + coefficients[i];
        }
        return result;

    }

    private Double[] drawCoefficients(int degree) {
        Random random = new Random();
        return generate(supplyWithRandomCoefficient(random)).limit(degree).toArray(Double[]::new);
    }

    void RecalculateValues() {

    }

    public List<Double> getY() {
        return y;
    }

    private Supplier<Double> supplyWithRandomCoefficient(Random random){
        return ()->random.nextDouble()/10000000000000000000.0;
    }

    public void updateCoefficients(Double[] coefficients) {
        assert coefficients.length == this.coefficients.length;
        this.coefficients = coefficients;
    }


}
