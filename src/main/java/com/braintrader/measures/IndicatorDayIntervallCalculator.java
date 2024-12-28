package com.braintrader.measures;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import lombok.Getter;

@Getter
public abstract class IndicatorDayIntervallCalculator extends IndicatorCalculator {

    protected final int dayIntervall;

    protected IndicatorDayIntervallCalculator(Yfinance yfinance, String symbol, int dayIntervall) throws IndicatorException {
        super(yfinance,symbol);

        if (dayIntervall<=2) {
            throw new IndicatorException("Day intervall must be greater than 2");
        }

        this.dayIntervall = dayIntervall;

    }


}
