package com.braintrader.measures.adx;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.adx.MinusDIIndicator;

import java.util.List;

public class MinusDI extends IndicatorDayIntervallCalculator {

    public MinusDI(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // MinusDI Indikator
        MinusDIIndicator minusDI = new MinusDIIndicator(series, dayIntervall);

        return this.calculateMeasure(minusDI, "MinusDI" + dayIntervall);

    }
}