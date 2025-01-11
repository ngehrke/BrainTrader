package com.braintrader.optimalsignals;

import lombok.Getter;

@Getter
public class OptimalTransaction {

    private final int buyDay;
    private final int sellDay;
    private final double buyPrice;
    private final double sellPrice;

    public OptimalTransaction(int buyDay, int sellDay, double buyPrice, double sellPrice) {

        this.buyDay = buyDay;
        this.sellDay = sellDay;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;

    }

    public double getProfit() {
        return sellPrice - buyPrice;
    }


    @Override
    public String toString() {
        return "(Buy: Tag " + buyDay + " (Preis: " + buyPrice + "), Sell: Tag " + sellDay + " (Preis: " + sellPrice + "))";
    }

}

