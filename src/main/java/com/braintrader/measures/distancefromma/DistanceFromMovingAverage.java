package com.braintrader.measures.distancefromma;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.IndicatorDayIntervallCalculator;
import org.ta4j.core.indicators.DistanceFromMAIndicator;
import org.ta4j.core.indicators.FisherIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.MedianPriceIndicator;

import java.util.List;

public class DistanceFromMovingAverage extends IndicatorDayIntervallCalculator {

    public DistanceFromMovingAverage(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance, symbol, dayIntervall);
    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator movingAverage = new SMAIndicator(closePrice, dayIntervall);

        // Indikator
        DistanceFromMAIndicator dFMA = new DistanceFromMAIndicator(series,movingAverage);

        return this.calculateMeasure(dFMA, "DistanceFromMovingAvg" + dayIntervall);

    }

}
