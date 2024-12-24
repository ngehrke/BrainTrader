package com.braintrader.tradingstrategy;

import com.braintrader.exceptions.TradingStrategyException;

import java.time.LocalDate;

public interface ITradingStrategy {

    Trade apply(String symbol,double quantity, LocalDate startDate) throws TradingStrategyException;

}
