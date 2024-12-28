package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestDeleteSymbolsInDatabase {

    @Test
    void testDeleteSymbolsInDatabase() throws YfinanceException {

        Yfinance yFinance = new Yfinance(System.out::println);

        Map<String, String> symbolMap = TestYfinanceGetAndSaveStockPrices.getCryptocurrencies();
        Set<String> symbols = symbolMap.keySet();

        for(String symbol:symbols) {
            yFinance.deleteSymbolInDatabase(symbol);
        }

        yFinance.close();

        assertTrue(true);

    }

}
