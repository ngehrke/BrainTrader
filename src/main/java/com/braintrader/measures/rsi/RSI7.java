package com.braintrader.measures.rsi;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;


public class RSI7 extends AbstractRSI {

    public RSI7(Yfinance yfinance, String symbol) throws IndicatorException {
        super(yfinance,symbol,7);
    }

}
