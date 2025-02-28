package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Price;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.optimalsignals.OptimalResult;
import com.braintrader.optimalsignals.OptimalStockProfitCalculator;
import com.braintrader.optimalsignals.pricetransformations.LogPriceWithTransactionCostTransformer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestOptimalSignals {

    @Test
    void testOptimalSignals() throws YfinanceException {

        Yfinance yFinance = new Yfinance(System.out::println);

        Set<String> symbol = Set.of("AAPL");
        LocalDate startDate = LocalDate.of(2010, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        LogPriceWithTransactionCostTransformer logPriceWithTransactionCostTransformer = new LogPriceWithTransactionCostTransformer(0,0.001);

        Map<String, List<Price>> prices = yFinance.getStockPricesFromDatabaseAsList(symbol, startDate, endDate);
        OptimalStockProfitCalculator optimalStockProfitCalculator = new OptimalStockProfitCalculator(prices.get("AAPL"),logPriceWithTransactionCostTransformer);

        for(int i=1;i<100;i++) {
            OptimalResult optimalResult = optimalStockProfitCalculator.calculateSignals(i);
            System.out.println("Number of transactions: "+i+", profit: "+ optimalResult.getMaxProfit()+", avg profit: "+optimalResult.getAverageProfit());
        }

        yFinance.close();
        assertTrue(true);

    }

}
