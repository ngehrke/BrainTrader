package com.braintrader.measures.rsi;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;

import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.List;


public class RSI extends IndicatorDayIntervallCalculator {

    public RSI(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance,symbol,dayIntervall);

    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // Close-Preis Indikator
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        // RSI Indikator mit einer Periode von n
        RSIIndicator rsi = new RSIIndicator(closePrice, dayIntervall);

        return this.calculateMeasure(rsi,"RSI"+dayIntervall);

    }

}
