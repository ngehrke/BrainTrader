package com.braintrader.datamanagement;

import com.braintrader.exceptions.DataModelException;
import com.braintrader.exceptions.YfinanceException;
import com.braintrader.measures.IMeasure;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataModelManager {

    private final Yfinance yFinance;

    private final LocalDate startDate;
    private final LocalDate endDate;

    private final Map<RowKey,Double> lhs = new HashMap<>();
    private final List<Map<RowKey,Double>> rhsMeasureColumns = new ArrayList<>();
    @Getter
    private final List<String> rhsMeasureColumnNames = new ArrayList<>();

    private final List<Map<RowKey,String>> rhsCategoryColumns = new ArrayList<>();
    @Getter
    private final List<String> rhsCategoryColumnNames = new ArrayList<>();

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

            if (measures.isEmpty()) {
                throw new YfinanceException("No measures found for LHS for symbol "+symbol+" and measure "+measureName);
            }

            for(IMeasure measure : measures) {

                RowKey key = new RowKey(measure.getMeasureDate(), symbol);
                Double value=measure.getMeasureValue();

                this.lhs.put(key, value);

            }

        }

    }

    public void addRHSMeasureColumn(String symbol, String measureName) throws YfinanceException {

        if (lhs.isEmpty()) {
            throw new YfinanceException("Please define the left hand side measure column first.");
        }

        Map<RowKey,Double> rhsColumn = new HashMap<>();

        // get all rowKeys of the lhs
        Set<RowKey> lhsRowKeys = lhs.keySet();

        // iterate all keys of the lhs
        for(RowKey key : lhsRowKeys) {

            LocalDate date = key.getDate();

            // get the measure for the symbol
            IMeasure measure = yFinance.getMeasure(symbol, measureName, date);

            if (measure!=null) {
                rhsColumn.put(key, measure.getMeasureValue());
            }


        }

        rhsMeasureColumns.add(rhsColumn);
        rhsMeasureColumnNames.add(measureName+"_"+symbol);

        if (rhsColumn.isEmpty()) {
            throw new YfinanceException("No measures found for RHS for symbol "+symbol+" and measure "+measureName);
        }

    }

    public void addRHSCategoryColumn(String category) throws YfinanceException {

        if (lhs.isEmpty()) {
            throw new YfinanceException("Please define the left hand side measure column first.");
        }

        Map<RowKey,String> rhsColumn = new HashMap<>();

        // get all rowKeys of the lhs
        Set<RowKey> lhsRowKeys = lhs.keySet();

        // iterate all keys of the lhs
        for(RowKey key : lhsRowKeys) {

            String symbolLHS = key.getSymbol();

            String catValue = yFinance.getCompanyInfo(symbolLHS, category);

            if (catValue!=null) {
                rhsColumn.put(key, catValue);
            }

        }

        rhsCategoryColumns.add(rhsColumn);
        rhsCategoryColumnNames.add(category);

        if (rhsColumn.isEmpty()) {
            throw new YfinanceException("No measures found for RHS for category "+category);
        }

    }

    public void addSymbolAsRHSCategoryColumn() throws YfinanceException {

        if (lhs.isEmpty()) {
            throw new YfinanceException("Please define the left hand side measure column first.");
        }

        Map<RowKey,String> rhsColumn = new HashMap<>();

        // get all rowKeys of the lhs
        Set<RowKey> lhsRowKeys = lhs.keySet();

        // iterate all keys of the lhs
        for(RowKey key : lhsRowKeys) {

            String symbolLHS = key.getSymbol();
            rhsColumn.put(key, symbolLHS);

        }

        rhsCategoryColumns.add(rhsColumn);
        rhsCategoryColumnNames.add("symbol");

        if (rhsColumn.isEmpty()) {
            throw new YfinanceException("No measures found for RHS for symbol");
        }

    }

    // create createLHSVector but order rowKeys fist
    public Double[] createLHSVector() throws YfinanceException {

            if (lhs.isEmpty()) {
                throw new YfinanceException("Please define the left hand side measure column first.");
            }

            // get all rowKeys of the lhs
            List<RowKey> lhsRowKeys = new ArrayList<>(lhs.keySet());
            lhsRowKeys.sort(RowKey::compareTo);

            // iterate all keys of the lhs
            Double[] lhsVector = new Double[lhsRowKeys.size()];

            int counter=0;
            for(RowKey key : lhsRowKeys) {

                lhsVector[counter] = lhs.get(key);
                counter++;

            }

            return lhsVector;

    }

    public Double[][] createRHSMeasureMatrix() throws YfinanceException  {

        if (lhs.isEmpty()) {
            throw new YfinanceException("Please define the left hand side measure column first.");
        }

        // get all rowKeys of the lhs
        List<RowKey> lhsRowKeys = new ArrayList<>(lhs.keySet());
        lhsRowKeys.sort(RowKey::compareTo);

        // iterate all keys of the lhs
        Double[][] rhsMatrix = new Double[lhsRowKeys.size()][rhsMeasureColumns.size()];

        int counter=0;
        for(RowKey key : lhsRowKeys) {

            for(int i=0;i<rhsMeasureColumns.size();i++) {

                Double value = rhsMeasureColumns.get(i).get(key);

                if (value!=null) {
                    rhsMatrix[counter][i] = rhsMeasureColumns.get(i).get(key);
                } else {
                    rhsMatrix[counter][i] = null;
                }

            }

            counter++;

        }

        return rhsMatrix;

    }

    public String[][] createRHSCategoryMatrix() throws YfinanceException  {

        if (lhs.isEmpty()) {
            throw new YfinanceException("Please define the left hand side measure column first.");
        }

        // get all rowKeys of the lhs
        List<RowKey> lhsRowKeys = new ArrayList<>(lhs.keySet());
        lhsRowKeys.sort(RowKey::compareTo);

        // iterate all keys of the lhs
        String[][] rhsMatrix = new String[lhsRowKeys.size()][rhsCategoryColumns.size()];

        int counter=0;
        for(RowKey key : lhsRowKeys) {

            for(int i=0;i<rhsCategoryColumns.size();i++) {

                String value = rhsCategoryColumns.get(i).get(key);

                if (value!=null) {
                    rhsMatrix[counter][i] = rhsCategoryColumns.get(i).get(key);
                } else {
                    rhsMatrix[counter][i] = null;
                }

            }

            counter++;

        }

        return rhsMatrix;

    }

    public static float[] convertToFloatArray(Double[] array) {

        if (array == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }

        float[] floatArray = new float[array.length];

        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                floatArray[i] = array[i].floatValue();
            } else {
                floatArray[i] = Float.NaN; // Assign NaN for null values
            }
        }

        return floatArray;
    }

    public static float[][] convertToFloatArray(Double[][] array) {

        if (array == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }

        float[][] floatArray = new float[array.length][];

        for (int i = 0; i < array.length; i++) {

            if (array[i] != null) {

                floatArray[i] = new float[array[i].length];
                for (int j = 0; j < array[i].length; j++) {
                    if (array[i][j] != null) {
                        floatArray[i][j] = array[i][j].floatValue();
                    } else {
                        floatArray[i][j] = Float.NaN; // Assign NaN for null values
                    }
                }
            } else {
                floatArray[i] = null; // Assign null for null sub-arrays
            }

        }

        return floatArray;

    }

    public static int countNulls(Double[][] matrix) {

        int count = 0;

        for (Double[] row : matrix) {
            for (Double value : row) {
                if (value == null) {
                    count++;
                }
            }
        }

        return count;

    }

    public static int countNulls(Double[] vector) {

        int count = 0;

        for (Double value : vector) {
            if (value == null) {
                count++;
            }
        }

        return count;

    }

    public static int countNulls(String[][] matrix) {

        int count = 0;

        for (String[] row : matrix) {
            for (String value : row) {
                if (value == null) {
                    count++;
                }
            }
        }

        return count;

    }


}
