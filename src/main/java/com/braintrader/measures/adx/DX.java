package com.braintrader.measures.adx;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.adx.DXIndicator;

import java.util.List;

public class DX extends IndicatorDayIntervallCalculator {

    public DX(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // DX Indikator
        DXIndicator dx = new DXIndicator(series, dayIntervall);

        return this.calculateMeasure(dx, "DX" + dayIntervall);

    }

}
