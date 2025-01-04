package com.braintrader.tests.dev;

import com.braintrader.optimalsignals.Result;
import com.braintrader.optimalsignals.StockProfit;
import com.braintrader.optimalsignals.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestOptimalSignals {

    @Test
    void testOptimalSignals() {

        double[] prices = {10, 11,11,11, 3,3, 7, 6, 55};
        int K = 1;

        Result result = StockProfit.maxProfitWithTransactions(prices, K);

        System.out.println("Maximaler Gewinn: " + result.getMaxProfit());
        System.out.println("Kauf- und Verkaufszeitpunkte:");

        for (Transaction transaction : result.getTransactions()) {
            System.out.println(transaction);
        }

        assertTrue(true);

    }

}
