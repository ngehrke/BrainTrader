package com.braintrader.datamanagement;

import lombok.Getter;

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

    @Override
    public String toString() {
        return "Price [symbol=" + symbol + ", date=" + date + ", currency=" + currency + ", open=" + open + ", close="
                + close + ", high=" + high + ", low=" + low + ", adjClose=" + adjClose + ", volume=" + volume + "]";
    }


}
