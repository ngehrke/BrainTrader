package com.braintrader.measures.rsi;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;

public class RSI70 extends AbstractRSI {

    public RSI70(Yfinance yfinance, String symbol) throws IndicatorException {
        super(yfinance,symbol,70);
    }

}
