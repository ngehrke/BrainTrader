package com.braintrader.measures.fisher;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.FisherIndicator;
import org.ta4j.core.indicators.helpers.MedianPriceIndicator;

import java.util.List;

public class Fisher extends IndicatorDayIntervallCalculator {

    public Fisher(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        MedianPriceIndicator medianPrice = new MedianPriceIndicator(series);

        // Indikator
        FisherIndicator fisher = new FisherIndicator(medianPrice, dayIntervall);

        return this.calculateMeasure(fisher, "Fisher" + dayIntervall);

    }

}
