package com.braintrader.measures.adx;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.adx.PlusDIIndicator;

import java.util.List;

public class PlusDI extends IndicatorDayIntervallCalculator {

    public PlusDI(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // PlusDI Indikator
        PlusDIIndicator plusDI = new PlusDIIndicator(series, dayIntervall);

        return this.calculateMeasure(plusDI, "PlusDI" + dayIntervall);

    }
}
