package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestYfinanceUpdateStockInfo {

    @Test
    void testGetAndSaveInfo() throws YfinanceException {

        Yfinance yFinance = new Yfinance(System.out::println);

        yFinance.updateAllStockInfosInDatabase();

        yFinance.close();

        assertTrue(true);

    }

}
