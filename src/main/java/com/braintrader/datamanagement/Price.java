package com.braintrader.datamanagement;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class Price {

    protected String symbol;
    protected LocalDate date;
    protected String currency;

    protected Double open;
    protected Double close;
    protected Double high;
    protected Double low;
    protected Double adjClose;
    protected Long volume;

    protected double buySignal=0;
    protected double sellSignal=0;

    @Setter
    protected int dayNumber=0;

    public double getMedianPrice() {
        return (high + low) / 2;
    }

    public double getAveragePrice() {
        return (open + close + high + low) / 4;
    }

    @Override
    public String toString() {
        return "Price [symbol=" + symbol + ", date=" + date + ", currency=" + currency + ", open=" + open + ", close="
                + close + ", high=" + high + ", low=" + low + ", adjClose=" + adjClose + ", volume=" + volume + "]";
    }


}
