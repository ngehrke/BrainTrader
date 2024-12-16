package com.braintrader.tradingstrategy;

import com.braintrader.datamanagement.Price;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.TradingStrategyException;
import com.braintrader.exceptions.YfinanceException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BuyAndHoldConstantPeriodStrategy implements ITradingStrategy {

    private final Yfinance yFinance;

    private final int periodInDays;
    private final double takeProfitInPercent;
    private final double stopLossInPercent;

    public BuyAndHoldConstantPeriodStrategy(Yfinance yFinance, int periodInDays, double takeProfitInPercent, double stopLossInPercent) throws TradingStrategyException {

        if (yFinance==null) {
            throw new TradingStrategyException("Yfinance object cannot be null");
        }

        this.yFinance = yFinance;

        this.periodInDays = periodInDays;
        this.takeProfitInPercent = takeProfitInPercent;
        this.stopLossInPercent = stopLossInPercent;

    }

    // TODO: Implement the apply method
    @Override
    public Trade apply(String symbol,double quantity,PriceType priceType, LocalDate startDate) throws TradingStrategyException {

        try {

            Price buyPrice = yFinance.getPriceFromDatabase(symbol, startDate);
            Price sellPriceEndOfPeriod = yFinance.getPriceFromDatabase(symbol, startDate, periodInDays);

            // check highest prize in period

        } catch (YfinanceException e) {
            throw new TradingStrategyException("Error while getting price from database: "+e.getMessage(), e);
        }

        return null;

    }

    @Override
    public String toString() {
        return "BuyAndHoldConstantPeriodStrategy [periodInDays=" + periodInDays + ", takeProfitInPercent=" + takeProfitInPercent + ", stopLossInPercent=" + stopLossInPercent + "]";
    }

}
