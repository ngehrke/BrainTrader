package com.braintrader.measures;

import com.braintrader.datamanagement.Price;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class YFinanceIndicatorCalculator {

    private final Yfinance yFinance;

    protected final String symbol;

    protected final Map<LocalDate,Integer> dateToIndexMap = new HashMap<>();
    protected BarSeries series;

    protected YFinanceIndicatorCalculator(Yfinance yFinance,String symbol) throws IndicatorException {

        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IndicatorException("Symbol cannot be null or empty");
        }

        if (yFinance == null) {
            throw new IndicatorException("Yfinance object cannot be null");
        }

        this.symbol = symbol;
        this.yFinance = yFinance;

        this.loadBars();

    }

    public abstract List<IMeasure> calculateMeasure() throws IndicatorException;

    private void loadBars() throws IndicatorException {

        try {

            // get the complete price history from the database
            List<Bar> bars = new ArrayList<>();
            List<Price> prices = yFinance.getAllPricesInDatabase(symbol);

            // load the bars from the database
            int counter=0;
            for(Price price:prices) {

                Bar bar = createBar(price.getDate(), price.getOpen(), price.getHigh(), price.getLow(), price.getClose(), price.getVolume());

                if (bar!=null) {

                    bars.add(bar);
                    dateToIndexMap.put(price.getDate(), counter);
                    counter++;

                }

            }

            this.series = new BaseBarSeries(bars);

        } catch (Exception e) {
            throw new IndicatorException("Error loading bars from database (symbol: "+symbol+"): "+e.getMessage(), e);
        }

    }

    private static Bar createBar(LocalDate date, double open, double high, double low, double close, long volume) {

        if (date==null || Double.isNaN(open) || Double.isNaN(high) || Double.isNaN(low) || Double.isNaN(close)) {
            return null;
        }

        ZonedDateTime dateTime = date.atStartOfDay(ZoneOffset.UTC);

        return new BaseBar(Duration.ofDays(1),dateTime, open, high,
                low, close, volume);

    }

    protected Integer getIndexOfDate(LocalDate date) {
        return dateToIndexMap.get(date);
    }

}
