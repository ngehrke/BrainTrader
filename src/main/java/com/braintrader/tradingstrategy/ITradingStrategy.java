package com.braintrader.tradingstrategy;

import com.braintrader.exceptions.TradingStrategyException;

import java.time.LocalDate;

public interface ITradingStrategy {

    Trade apply(String symbol,double quantity,PriceType priceType, LocalDate startDate) throws TradingStrategyException;

}
