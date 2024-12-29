package com.braintrader.measures.aroon;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.aroon.AroonDownIndicator;

import java.util.List;

public class AroonDown extends IndicatorDayIntervallCalculator {

    public AroonDown(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // Indikator
        AroonDownIndicator aroonDown = new AroonDownIndicator(series, dayIntervall);

        return this.calculateMeasure(aroonDown, "AroonDown" + dayIntervall);

    }

}
