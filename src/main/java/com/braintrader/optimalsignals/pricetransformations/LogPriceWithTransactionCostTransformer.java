package com.braintrader.optimalsignals.pricetransformations;

import lombok.Getter;

@Getter
public class LogPriceWithTransactionCostTransformer extends StandardPriceTransformer {

    private final double fixCost;
    private final double variableCostFactor;

    public LogPriceWithTransactionCostTransformer(double fixCost, double variableCostFactor) {

        this.fixCost = fixCost;
        this.variableCostFactor = variableCostFactor;

    }

    @Override
    public double getBuyPrice(double price) {
        return Math.log(price + price*this.variableCostFactor + this.fixCost);
    }

    @Override
    public double getSellPrice(double price) {

        double linearPrice = price - price*this.variableCostFactor - this.fixCost;

        if (linearPrice <= 0) {
            return 0;
        }

        return Math.log(linearPrice);

    }

}
