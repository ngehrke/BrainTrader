package com.braintrader.optimalsignals;

import lombok.Getter;

import java.util.List;

public class OptimalResult {

    @Getter
    private final double maxProfit;
    private final List<OptimalTransaction> optimalTransactions;

    public OptimalResult(double maxProfit, List<OptimalTransaction> optimalTransactions) {
        this.maxProfit = maxProfit;
        this.optimalTransactions = optimalTransactions;
    }

    public double getAverageProfit() {
        return maxProfit / optimalTransactions.size();
    }

    public List<OptimalTransaction> getTransactions() {
        return optimalTransactions;
    }

    @Override
    public String toString() {
        return "Max Profit: " + maxProfit + ", Transactions: " + optimalTransactions;
    }

}

