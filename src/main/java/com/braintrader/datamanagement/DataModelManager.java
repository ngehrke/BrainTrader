package com.braintrader.datamanagement;

import com.braintrader.exceptions.DataModelException;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.measures.IMeasure;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataModelManager {

    private final Yfinance yFinance;

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Set<String> usedDays = new HashSet<>();

    private Map<String,Double> lhs = new HashMap<>();
    private List<Map<String,Double>> rhsMeasureColumns = new ArrayList<>();
    private List<String> rhsMeasureColumnNames = new ArrayList<>();

    private List<Map<String,String>> rhsCategoryColumns = new ArrayList<>();
    private List<String> rhsCategoryColumnNames = new ArrayList<>();

    public DataModelManager(Yfinance yFinance, LocalDate startDate, LocalDate endDate) throws DataModelException {

        if (yFinance == null) {
            throw new DataModelException("Yfinance object cannot be null");
        }

        if (startDate == null) {
            throw new DataModelException("Start date cannot be null");
        }

        if (endDate == null) {
            throw new DataModelException("End date cannot be null");
        }

        this.yFinance = yFinance;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public void defineLHS(Set<String> symbols, String measureName) throws YfinanceException {

        // define the left hand side of the equation
        for(String symbol:symbols) {

            List<IMeasure> measures = yFinance.getMeasures(symbol, measureName, startDate, endDate);

            for(IMeasure measure : measures) {

                String key=measure.getMeasureDate()+"."+symbol;
                Double value=measure.getMeasureValue();

                this.lhs.put(key, value);
                this.usedDays.add(measure.getMeasureDate().toString());

            }

        }

    }

    public void addRHSMeasureColumn(Set<String> symbols, String measureName) throws YfinanceException {

        // define the right hand side of the equation
        Map<String,Double> column = new HashMap<>();

        for(String symbol:symbols) {

            List<IMeasure> measures = yFinance.getMeasures(symbol, measureName, startDate, endDate);

            for(IMeasure measure : measures) {

                String key=measure.getMeasureDate()+"."+symbol;
                Double value=measure.getMeasureValue();

                column.put(key, value);
                this.usedDays.add(measure.getMeasureDate().toString());

            }

        }

        rhsMeasureColumns.add(column);
        rhsMeasureColumnNames.add(measureName);

    }

    // TODO: addRHSCategoryColumn (getCompanyInfo)
    public void addRHSCategoryColumn(Set<String> symbols, String category) throws YfinanceException {

        if (usedDays.isEmpty()) {
            throw new YfinanceException("No days have been used yet. Please define the left hand side and the right hand side measure columns first.");
        }


    }


}
