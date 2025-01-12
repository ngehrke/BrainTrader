package com.braintrader.optimalsignals.pricetransformations;

import lombok.Getter;

@Getter
public class StandardPriceWithTransactionCostTransformer extends StandardPriceTransformer {

    private final double fixCost;
    private final double variableCostFactor;

    public StandardPriceWithTransactionCostTransformer(double fixCost, double variableCostFactor) {
        this.fixCost = fixCost;
        this.variableCostFactor = variableCostFactor;
    }

    @Override
    public double getBuyPrice(double price) {
        return price + price*this.variableCostFactor + this.fixCost;
    }

    @Override
    public double getSellPrice(double price) {
        return price - price*this.variableCostFactor - this.fixCost;
    }

}
