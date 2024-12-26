package com.braintrader.measures.rsi;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;


public class RSI14 extends AbstractRSI {

    public RSI14(Yfinance yfinance, String symbol) throws IndicatorException {
        super(yfinance,symbol,14);
    }

}
