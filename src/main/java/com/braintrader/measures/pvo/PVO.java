package com.braintrader.measures.pvo;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.PVOIndicator;


import java.util.List;

public class PVO extends IndicatorDayIntervallCalculator {

    public PVO(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        // Indikator
        PVOIndicator pvo = new PVOIndicator(series,dayIntervall,dayIntervall*2);

        return this.calculateMeasure(pvo, "PVO" + dayIntervall+"_"+(dayIntervall*2));

    }

}
