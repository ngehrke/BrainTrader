package com.braintrader.optimalsignals;

import java.util.ArrayList;
import java.util.List;

public class StockProfit {

    public static Result maxProfitWithTransactions(double[] prices, int K) {

        int T = prices.length;
        if (T == 0 || K == 0) {
            return new Result(0, new ArrayList<>());
        }

        // DP table for profits
        double[][] dp = new double[K + 1][T];
        // Track transactions
        List<Transaction>[][] transactions = new ArrayList[K + 1][T];

        for (int k = 0; k <= K; k++) {
            for (int i = 0; i < T; i++) {
                transactions[k][i] = new ArrayList<>();
            }
        }

        for (int k = 1; k <= K; k++) {

            double maxDiff = -prices[0];
            int buyDay = 0;

            for (int i = 1; i < T; i++) {
                // Option 1: No transaction on day i
                dp[k][i] = dp[k][i - 1];
                transactions[k][i] = new ArrayList<>(transactions[k][i - 1]);

                // Option 2: Sell on day i
                double potentialProfit = prices[i] + maxDiff;
                if (potentialProfit > dp[k][i]) {
                    dp[k][i] = potentialProfit;
                    transactions[k][i] = new ArrayList<>(transactions[k - 1][buyDay]);
                    transactions[k][i].add(new Transaction(buyDay, i, prices[buyDay], prices[i]));
                }

                // Update maxDiff for the next day
                if (dp[k - 1][i] - prices[i] > maxDiff) {
                    maxDiff = dp[k - 1][i] - prices[i];
                    buyDay = i;
                }
            }
        }

        return new Result(dp[K][T - 1], transactions[K][T - 1]);

    }


}
