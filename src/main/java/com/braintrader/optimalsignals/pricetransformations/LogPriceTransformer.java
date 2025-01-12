package com.braintrader.optimalsignals.pricetransformations;

public class LogPriceTransformer extends StandardPriceTransformer {

    @Override
    public double getBuyPrice(double price) {
        return Math.log(price);
    }

    @Override
    public double getSellPrice(double price) {
        return Math.log(price);
    }

}
