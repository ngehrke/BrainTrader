package com.braintrader.measures.aroon;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.aroon.AroonOscillatorIndicator;

import java.util.List;

public class AroonOscillator extends IndicatorDayIntervallCalculator {

    public AroonOscillator(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // Indikator
        AroonOscillatorIndicator aroonOscillator = new AroonOscillatorIndicator(series, dayIntervall);

        return this.calculateMeasure(aroonOscillator, "AroonOscillator" + dayIntervall);

    }

}
