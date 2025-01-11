package com.braintrader.datamanagement;

import com.braintrader.measures.GeneralMeasure;
import com.braintrader.measures.IMeasure;
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

    @Setter
    protected double buySignal=0;
    @Setter
    protected double sellSignal=0;

    public double getMedianPrice() {
        return (high + low) / 2;
    }

    public double getAveragePrice() {
        return (open + close + high + low) / 4;
    }


    public IMeasure getOptimalBuySignalMeasure() {

        return new GeneralMeasure(symbol, "OptimalBuySignal", date, this.buySignal);

    }

    public void logPrices() {

        if (this.open!=null && this.open>=0) {
            this.open = Math.log(this.open);
        }

        if (this.close!=null && this.close>=0) {
            this.close = Math.log(this.close);
        }

        if (this.high!=null && this.high>=0) {
            this.high = Math.log(this.high);
        }

        if (this.low!=null && this.low>=0) {
            this.low = Math.log(this.low);
        }

    }

    public IMeasure getOptimalSellSignalMeasure() {

        return new GeneralMeasure(symbol, "OptimalSellSignal", date, this.sellSignal);

    }

    @Override
    public String toString() {
        return "Price [symbol=" + symbol + ", date=" + date + ", currency=" + currency + ", open=" + open + ", close="
                + close + ", high=" + high + ", low=" + low + ", adjClose=" + adjClose + ", volume=" + volume + "]";
    }


}
