package com.braintrader.measures;

import java.time.LocalDate;

public class GeneralMeasure implements IMeasure {

    private final String symbol;
    private final String measureName;
    private final LocalDate measureDate;
    private final Double measureValue;

    public GeneralMeasure(String symbol, String measureName, LocalDate measureDate, Double measureValue) {

        this.symbol = symbol;
        this.measureName = measureName;
        this.measureDate = measureDate;
        this.measureValue = measureValue;

    }

    @Override
    public String getMeasureName() {
        return measureName;
    }

    @Override
    public LocalDate getMeasureDate() {
        return measureDate;
    }

    @Override
    public Double getMeasureValue() {
        return measureValue;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "IndicatorMeasure [symbol=" + symbol + ", measureName=" + measureName + ", measureDate=" + measureDate + ", measureValue=" + measureValue + "]";
    }


}
