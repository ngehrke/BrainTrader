package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.TradingStrategyException;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.tradingstrategy.BuyAndHoldConstantPeriodStrategy;
import com.braintrader.tradingstrategy.Trade;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestBuyAndHoldConstantPeriodStrategy {

    @Test
    void testBuyAndHoldConstantPeriodStrategy() throws YfinanceException, TradingStrategyException {

        Yfinance yFinance = new Yfinance(System.out::println);

        BuyAndHoldConstantPeriodStrategy strategy = new BuyAndHoldConstantPeriodStrategy(yFinance, 14, 5, 2);

        Set<String> symbols = yFinance.getAllSymbolsInDatabase();

        int measureCounter=0;
        for(String symbol : symbols) {

            Set<LocalDate> allDates = yFinance.getDatesOfAllPriceEntries(symbol);

            for(LocalDate date : allDates) {

                Trade trade = strategy.apply(symbol, 1, date);

                yFinance.saveMeasureInDatabase(trade);
                measureCounter++;


            }

        }

        System.out.println("Number of measures: " + measureCounter);

        yFinance.close();

        assertTrue(true);


    }

}
