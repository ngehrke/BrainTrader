package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.rsi.RSI14;
import com.braintrader.measures.rsi.RSI25;
import com.braintrader.measures.rsi.RSI7;
import com.braintrader.measures.rsi.RSI70;
import com.braintrader.measures.rsi.RSI9;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestRSI14 {

    @Test
    void testRSI() throws YfinanceException, IndicatorException {

        Yfinance yFinance = new Yfinance(System.out::println);

        Set<String> symbols = yFinance.getAllSymbolsInDatabase();

        for(String symbol : symbols) {

            RSI14 rsi14Calculator = new RSI14(yFinance, symbol);
            List<IMeasure> measures14 = rsi14Calculator.calculateMeasure();

            RSI7 rsi7Calculator = new RSI7(yFinance, symbol);
            List<IMeasure> measures7 = rsi7Calculator.calculateMeasure();

            RSI9 rsi9Calculator = new RSI9(yFinance, symbol);
            List<IMeasure> measures9 = rsi9Calculator.calculateMeasure();

            RSI25 rsi25Calculator = new RSI25(yFinance, symbol);
            List<IMeasure> measures25 = rsi25Calculator.calculateMeasure();

            RSI70 rsi70Calculator = new RSI70(yFinance, symbol);
            List<IMeasure> measures70 = rsi70Calculator.calculateMeasure();

            yFinance.saveMeasuresInDatabase(measures14);
            yFinance.saveMeasuresInDatabase(measures7);
            yFinance.saveMeasuresInDatabase(measures9);
            yFinance.saveMeasuresInDatabase(measures25);
            yFinance.saveMeasuresInDatabase(measures70);

        }

        yFinance.close();

        assertTrue(true);

    }

}
