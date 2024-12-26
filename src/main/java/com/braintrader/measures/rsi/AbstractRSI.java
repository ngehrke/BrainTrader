package com.braintrader.measures.rsi;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.IndicatorException;
import com.braintrader.measures.IMeasure;
import com.braintrader.measures.GeneralMeasure;
import com.braintrader.measures.YFinanceIndicatorCalculator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractRSI extends YFinanceIndicatorCalculator {

    protected final int dayIntervall;

    public AbstractRSI(Yfinance yfinance, String symbol,int dayIntervall) throws IndicatorException {
        super(yfinance,symbol);

        if (dayIntervall<=2) {
            throw new IndicatorException("RSI day intervall must be greater than 2");
        }

        this.dayIntervall = dayIntervall;

    }

    @Override
    public final List<IMeasure> calculateMeasure() throws IndicatorException {

        List<IMeasure> resultList = new ArrayList<>();

        // Close-Preis Indikator
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        // RSI Indikator mit einer Periode von n
        RSIIndicator rsi = new RSIIndicator(closePrice, dayIntervall);

        for(Map.Entry<LocalDate,Integer> entry:dateToIndexMap.entrySet()) {

            Integer index = entry.getValue();
            LocalDate date = entry.getKey();

            Num value = rsi.getValue(index);

            if (value!=null && !value.isNaN() && value.doubleValue()>0 ) {
                IMeasure measure = new GeneralMeasure(symbol, "RSI"+dayIntervall, date, value.doubleValue());
                resultList.add(measure);
            }

        }

        return resultList;

    }

}
