package com.braintrader.tradingstrategy;

import com.braintrader.measures.IMeasure;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Trade implements IMeasure {

    private final String symbol;

    private final double entryPrice;
    private final double exitPrice;
    private final LocalDate entryDate;
    private final LocalDate exitDate;

    private final double quantity;

    protected String tradeName=null;

    public Trade(String symbol,double quantity, double entryPrice, double exitPrice, LocalDate entryDate, LocalDate exitDate) {

        this.symbol = symbol;
        this.entryPrice = entryPrice;
        this.exitPrice = exitPrice;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.quantity = quantity;

    }

    public double getProfit() {
        return (exitPrice - entryPrice) * quantity;
    }

    public double getReturn() {
        return (exitPrice - entryPrice) / entryPrice;
    }

    public double getDurationInDays() {
        return entryDate.until(exitDate).getDays();
    }

    public double getAnnualizedReturn() {
        return Math.pow(1 + getReturn(), 365 / getDurationInDays()) - 1;
    }

    @Override
    public String toString() {
        return "Trade [symbol=" + symbol + ", entryPrice=" + entryPrice + ", exitPrice=" + exitPrice + ", entryDate=" + entryDate + ", exitDate=" + exitDate + "]";
    }


    @Override
    public String getMeasureName() {
        return this.tradeName;
    }

    @Override
    public LocalDate getMeasureDate() {
        return this.entryDate;
    }

    @Override
    public Double getMeasureValue() {
        return this.getReturn();
    }

}
