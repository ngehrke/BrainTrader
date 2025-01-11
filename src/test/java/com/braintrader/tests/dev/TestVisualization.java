package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Price;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.optimalsignals.OptimalResult;
import com.braintrader.optimalsignals.OptimalStockProfitCalculator;
import com.braintrader.visualization.PriceChartVisualizer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestVisualization {

    @Test
    void testVisualization() throws YfinanceException, InterruptedException {

        Yfinance yFinance = new Yfinance(System.out::println);

        Set<String> symbol = Set.of("AAPL");
        LocalDate startDate = LocalDate.of(2010, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        Map<String, List<Price>> prices = yFinance.getStockPricesFromDatabaseAsList(symbol, startDate, endDate);

        prices.get("AAPL").forEach(Price::logPrices);

        OptimalStockProfitCalculator optimalStockProfitCalculator = new OptimalStockProfitCalculator(prices.get("AAPL"));
        OptimalResult result = optimalStockProfitCalculator.calculateSignals(500);

        System.out.println("profit: "+ result.getMaxProfit()+", avg profit: "+result.getAverageProfit());

        PriceChartVisualizer priceChartVisualizer = new PriceChartVisualizer(prices);

        priceChartVisualizer.visualize(false, true, false, false);

        Thread.sleep(100000);

        yFinance.close();

        assertTrue(true);

    }

}
