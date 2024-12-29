package com.braintrader.measures.aroon;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.aroon.AroonUpIndicator;

import java.util.List;

public class AroonUp extends IndicatorDayIntervallCalculator {

    public AroonUp(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // Indikator
        AroonUpIndicator aroonUp = new AroonUpIndicator(series, dayIntervall);

        return this.calculateMeasure(aroonUp, "AroonUp" + dayIntervall);

    }

}
