package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import com.braintrader.measures.adx.ADX;
import com.braintrader.measures.adx.MinusDI;
import com.braintrader.measures.adx.PlusDI;
import com.braintrader.measures.aroon.AroonDown;
import com.braintrader.measures.aroon.AroonOscillator;
import com.braintrader.measures.aroon.AroonUp;
import com.braintrader.measures.distancefromma.DistanceFromMovingAverage;
import com.braintrader.measures.fisher.Fisher;
import com.braintrader.measures.pvo.PVO;
import com.braintrader.measures.rsi.RSI;
import org.junit.jupiter.api.Test;
import org.ta4j.core.Indicator;


import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestIndicatorCalculation {

    List<Integer> dayIntervallList = List.of(7, 9, 14, 25, 70);

    @Test
    void testRSI() throws YfinanceException, IndicatorException {

        Yfinance yFinance = new Yfinance(System.out::println);

        Set<String> symbols = yFinance.getAllSymbolsInDatabase();

        for(String symbol : symbols) {

            System.out.println("Calculating indicators for symbol: " + symbol);

            for(Integer dayIntervall : dayIntervallList) {

                RSI rsiCalculator = new RSI(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, rsiCalculator);

                ADX adxCalculator = new ADX(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, adxCalculator);

                MinusDI minusDICalculator = new MinusDI(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, minusDICalculator);

                PlusDI plusDICalculator = new PlusDI(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, plusDICalculator);

                AroonDown aroonDownCalculator = new AroonDown(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, aroonDownCalculator);

                AroonUp aroonUpCalculator = new AroonUp(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, aroonUpCalculator);

                AroonOscillator aroonOscillatorCalculator = new AroonOscillator(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, aroonOscillatorCalculator);

                Fisher fisherCalculator = new Fisher(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, fisherCalculator);

                DistanceFromMovingAverage distanceFromMovingAverageCalculator = new DistanceFromMovingAverage(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, distanceFromMovingAverageCalculator);

                PVO pvoCalculator = new PVO(yFinance, symbol, dayIntervall);
                calculateAndSaveIndicator(yFinance, pvoCalculator);

            }

        }

        yFinance.close();

        assertTrue(true);

    }

    private static void calculateAndSaveIndicator(Yfinance yFinance , IndicatorDayIntervallCalculator indicator) throws IndicatorException, YfinanceException {

        List<IMeasure> measures = indicator.calculateMeasure();
        yFinance.saveMeasuresInDatabase(measures);

    }


}
