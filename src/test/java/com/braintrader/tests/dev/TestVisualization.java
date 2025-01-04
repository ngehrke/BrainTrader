package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Price;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.visualization.PriceChartVisualizer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestVisualization {

    @Test
    void testVisualization() throws YfinanceException {

        Yfinance yFinance = new Yfinance(System.out::println);

        Set<String> symbol = Set.of("AAPL");
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 23);

        Map<String, List<Price>> prices = yFinance.getStockPricesFromDatabaseAsList(symbol, startDate, endDate);
        PriceChartVisualizer priceChartVisualizer = new PriceChartVisualizer(prices);

        priceChartVisualizer.visualize(false, true, false, false);

        yFinance.close();

        assertTrue(true);

    }

}
