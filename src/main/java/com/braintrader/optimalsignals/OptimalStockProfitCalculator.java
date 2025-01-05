package com.braintrader.optimalsignals;

import com.braintrader.datamanagement.Price;

import java.util.ArrayList;
import java.util.List;

public class OptimalStockProfitCalculator {

    private final List<Price> priceList;

    public OptimalStockProfitCalculator(List<Price> priceList) {

        this.priceList = priceList;

    }

    public OptimalResult calculateSignals(int numberOfTransactions, double transactionCostPercentage,double fixTransactionCost) {

        double[] prices = priceList.stream().mapToDouble(Price::getClose).toArray();
        OptimalResult optimalResult = maxProfitWithTransactions(prices, numberOfTransactions,transactionCostPercentage,fixTransactionCost);

        for(OptimalTransaction optimalTransaction : optimalResult.getTransactions()) {

            Price priceBuy = priceList.get(optimalTransaction.getBuyDay());
            Price priceSell = priceList.get(optimalTransaction.getSellDay());

            priceBuy.setBuySignal(optimalTransaction.getProfitPercentage());
            priceSell.setSellSignal(optimalTransaction.getProfitPercentage());

        }

        return optimalResult;

    }

    public static OptimalResult maxProfitWithTransactions(double[] prices, int K, double transactionCostPercentage, double fixTransactionCost) {

        int T = prices.length;
        if (T == 0 || K == 0) {
            return new OptimalResult(0, new ArrayList<>());
        }

        // DP table for profits
        double[][] dp = new double[K + 1][T];
        // Track transactions
        List<OptimalTransaction>[][] transactions = new ArrayList[K + 1][T];

        for (int k = 0; k <= K; k++) {
            for (int i = 0; i < T; i++) {
                transactions[k][i] = new ArrayList<>();
            }
        }

        for (int k = 1; k <= K; k++) {

            double maxDiff = -prices[0] * (1 + transactionCostPercentage)-fixTransactionCost; // Adjusted for transaction cost
            int buyDay = 0;

            for (int i = 1; i < T; i++) {
                // Option 1: No transaction on day i
                dp[k][i] = dp[k][i - 1];
                transactions[k][i] = new ArrayList<>(transactions[k][i - 1]);

                // Option 2: Sell on day i
                double sellPrice = prices[i] * (1 - transactionCostPercentage)-fixTransactionCost; // Selling with transaction cost
                double potentialProfit = sellPrice + maxDiff;
                if (potentialProfit > dp[k][i]) {
                    dp[k][i] = potentialProfit;
                    transactions[k][i] = new ArrayList<>(transactions[k - 1][buyDay]);
                    transactions[k][i].add(new OptimalTransaction(buyDay, i, prices[buyDay], prices[i]));
                }

                // Update maxDiff for the next day
                double buyPrice = prices[i] * (1 + transactionCostPercentage)+fixTransactionCost; // Buying with transaction cost
                if (dp[k - 1][i] - buyPrice > maxDiff) {
                    maxDiff = dp[k - 1][i] - buyPrice;
                    buyDay = i;
                }
            }
        }

        return new OptimalResult(dp[K][T - 1], transactions[K][T - 1]);

    }



}
