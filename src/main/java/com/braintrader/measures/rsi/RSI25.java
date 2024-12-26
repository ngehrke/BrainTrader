package com.braintrader.measures.rsi;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;


public class RSI25 extends AbstractRSI {

    public RSI25(Yfinance yfinance, String symbol) throws IndicatorException {
        super(yfinance,symbol,25);
    }

}
