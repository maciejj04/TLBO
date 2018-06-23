package com.maciejj.tlbo;

import java.util.List;

import static java.lang.Math.abs;

public class DataSetCorrelator {

    private List<Double> orgDataSet;

    DataSetCorrelator(List<Double> orgDataSet) {
        this.orgDataSet = orgDataSet;
    }

    Double correlate(List<Double> newDataSet){
        return correlate(orgDataSet, newDataSet);
    }

    static double correlate(List<Double> v1, List<Double> v2) {
        assert v1.size() == v2.size();
        double result = 0.0;
        for(int i = 0 ; i < v1.size(); i++) {
            result += abs(v1.get(i) - v2.get(i));
        }
        return result;
    }
}