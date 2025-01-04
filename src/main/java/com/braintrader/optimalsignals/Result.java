package com.braintrader.optimalsignals;

import java.util.List;

public class Result {

    private final double maxProfit;
    private final List<Transaction> transactions;

    public Result(double maxProfit, List<Transaction> transactions) {
        this.maxProfit = maxProfit;
        this.transactions = transactions;
    }

    public double getMaxProfit() {
        return maxProfit;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Max Profit: " + maxProfit + ", Transactions: " + transactions;
    }

}

