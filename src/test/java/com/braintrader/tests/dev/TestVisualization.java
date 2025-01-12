package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Price;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.optimalsignals.pricetransformations.LogPriceTransformer;
import com.braintrader.optimalsignals.OptimalResult;
import com.braintrader.optimalsignals.OptimalStockProfitCalculator;
import com.braintrader.optimalsignals.pricetransformations.LogPriceWithTransactionCostTransformer;
import com.braintrader.visualization.PriceChartVisualizer;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestVisualization {

    @Test
    void testVisualization() throws YfinanceException, InterruptedException {

        Runtime runtime = Runtime.getRuntime();

        long freeMem  = runtime.freeMemory();
        long totalMem = runtime.totalMemory();
        long maxMem   = runtime.maxMemory();

        long usedMem       = totalMem - freeMem;
        long availableMem  = maxMem - usedMem;

        System.out.println("Freier Speicher:       " + bytesToMB(freeMem) + " MB");
        System.out.println("Insgesamt reserviert:  " + bytesToMB(totalMem) + " MB");
        System.out.println("Maximal verfügbar:     " + bytesToMB(maxMem) + " MB");
        System.out.println("Bereits genutzt:       " + bytesToMB(usedMem) + " MB");
        System.out.println("Verfügbarer Heap:      " + bytesToMB(availableMem) + " MB");

        Yfinance yFinance = new Yfinance(System.out::println);

        Set<String> symbol = Set.of("AAPL");
        LocalDate startDate = LocalDate.of(2010, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        Map<String, List<Price>> prices = yFinance.getStockPricesFromDatabaseAsList(symbol, startDate, endDate);
        System.out.println("size of prices: "+prices.get("AAPL").size());
        int trades = prices.get("AAPL").size() / 10;
        System.out.println("trades: "+trades);
        LogPriceWithTransactionCostTransformer logPriceWithTransactionCostTransformer = new LogPriceWithTransactionCostTransformer(0,0.001);

        OptimalStockProfitCalculator optimalStockProfitCalculator = new OptimalStockProfitCalculator(prices.get("AAPL"),logPriceWithTransactionCostTransformer);
        OptimalResult result = optimalStockProfitCalculator.calculateSignals(trades);

        System.out.println("profit: "+ result.getMaxProfit()+", avg profit: "+result.getAverageProfit()+", trades done: "+result.getTransactions().size());

        PriceChartVisualizer priceChartVisualizer = new PriceChartVisualizer(prices);

        priceChartVisualizer.visualize(false, true, false, false);

        Thread.sleep(100000);

        yFinance.close();

        assertTrue(true);

    }

    private static long bytesToMB(long bytes) {
        return bytes / (1024 * 1024);
    }


}
