package com.braintrader.tests.dev.updatestockdata;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestYfinanceUpdateStockPrices {

    @Test
    void testUpdateStockPrices() throws YfinanceException {

        Yfinance yFinance = new Yfinance(System.out::println);

        yFinance.updateAllStockPricesInDatabase();

        yFinance.close();

        assertTrue(true);

    }

}
