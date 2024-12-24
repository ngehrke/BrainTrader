package com.braintrader.tradingstrategy;

import com.braintrader.datamanagement.Price;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.TradingStrategyException;
import com.braintrader.exceptions.YfinanceException;
import lombok.Getter;

import java.time.LocalDate;

// this strategy buys a stock at the start date and sells it at the end date if the takeprofit is not reached, when reached it sells at the takeprofit price
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

    @Override
    public Trade apply(String symbol,double quantity, LocalDate startDate) throws TradingStrategyException {

        try {

            Price buyPrice = yFinance.getPriceFromDatabase(symbol, startDate);

            // calcualte the enddate and prices
            LocalDate endDate = startDate.plusDays(periodInDays);
            LocalDate sellDate = yFinance.getNearestTradingDayFromDatabase(symbol, endDate);
            Price sellPriceEndOfPeriod = yFinance.getPriceFromDatabase(symbol, sellDate);

            if (sellDate==null || sellPriceEndOfPeriod==null) {
                return null;
            }

            // calculate prices
            Double highestPriceValue = yFinance.getHighestPriceInPeriod(symbol, startDate, endDate);

            if (highestPriceValue==null) {
                return null;
            }

            double buyPriceValue = (buyPrice.getClose() + buyPrice.getHigh() + buyPrice.getClose() ) / 3.0;
            double sellPriceEndOfPeriodValue = (sellPriceEndOfPeriod.getClose() + sellPriceEndOfPeriod.getHigh() + sellPriceEndOfPeriod.getClose() ) / 3.0;

            // create the trade
            double takeProfitPrice = buyPriceValue*(1.0+takeProfitInPercent/100.0);
            double stopLossPrice   = buyPriceValue*(1.0-stopLossInPercent/100.0);

            Trade soldTakeProfitTrade = new Trade(symbol,quantity, buyPriceValue, takeProfitPrice, startDate, sellDate);
            Trade stopLossTrade = new Trade(symbol,quantity, buyPriceValue, stopLossPrice, startDate, sellDate);
            Trade soldEndOfPeriodTrade = new Trade(symbol,quantity, buyPriceValue, sellPriceEndOfPeriodValue, startDate, sellDate);

            LocalDate datePriceWasFirstTimeAboveTakeProfit = yFinance.getDatePriceWasFirstTimeAboveThreshold(symbol, startDate, sellDate, takeProfitPrice);
            LocalDate datePriceWasFirstTimeBelowStopLoss   = yFinance.getDatePriceWasFirstTimeBelowThreshold(symbol, startDate, sellDate, stopLossPrice);

            // TODO: decide which trade to return

            return null;

        } catch (YfinanceException e) {
            throw new TradingStrategyException("Error while getting price from database: "+e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        return "BuyAndHoldConstantPeriodStrategy [periodInDays=" + periodInDays + ", takeProfitInPercent=" + takeProfitInPercent + ", stopLossInPercent=" + stopLossInPercent + "]";
    }

    public String getMeasureName() {
        return "BuyAndHoldConstantPeriodStrategy_"+periodInDays+"_"+takeProfitInPercent+"_"+stopLossInPercent;
    }

}
