package com.braintrader.measures.adx;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.adx.ADXIndicator;

import java.util.List;

public class ADX extends IndicatorDayIntervallCalculator {

    public ADX(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // ADX Indikator
        ADXIndicator adx = new ADXIndicator(series, dayIntervall);

        return this.calculateMeasure(adx, "ADX" + dayIntervall);

    }

}
