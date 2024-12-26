package com.braintrader.measures.rsi;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;


public class RSI9 extends AbstractRSI  {

    public RSI9(Yfinance yfinance, String symbol) throws IndicatorException {
        super(yfinance,symbol,9);
    }

}
