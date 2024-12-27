package com.braintrader.tests.dev;

import com.braintrader.datamanagement.DataModelManager;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.DataModelException;
import com.braintrader.exceptions.YfinanceException;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestDataMatrix {

    @Test
    void test() throws YfinanceException, DataModelException {

        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 23);

        Yfinance yFinance = new Yfinance(System.out::println);

        DataModelManager dataModelManager = new DataModelManager(yFinance, startDate, endDate);

        dataModelManager.defineLHS(Set.of("AAPL"), "BuyAndHoldConstantPeriodStrategy_14_5.0_2.0");
        dataModelManager.defineLHS(Set.of("AMZN"), "BuyAndHoldConstantPeriodStrategy_14_5.0_2.0");

        dataModelManager.addRHSMeasureColumn("AAPL","RSI14");
        dataModelManager.addRHSMeasureColumn("AAPL","RSI7");
        dataModelManager.addRHSMeasureColumn("AMZN","RSI14");
        dataModelManager.addRHSMeasureColumn("AMZN","RSI7");

        dataModelManager.addRHSCategoryColumn("sectorKey");
        dataModelManager.addRHSCategoryColumn("industryKey");
        dataModelManager.addRHSCategoryColumn("country");
        dataModelManager.addRHSCategoryColumn("exchange");

        dataModelManager.addSymbolAsRHSCategoryColumn();

        Double[] y = dataModelManager.createLHSVector();
        Double[][] x = dataModelManager.createRHSMeasureMatrix();
        String[][] xCat = dataModelManager.createRHSCategoryMatrix();

        System.out.println("size of y: "+y.length+" | missings: "+DataModelManager.countNulls(y));
        System.out.println("size of x: "+x.length+" | "+x[0].length+" | missings: "+DataModelManager.countNulls(x));
        System.out.println("size of xCat: "+xCat.length+" | "+xCat[0].length+" | missings: "+DataModelManager.countNulls(xCat));

        System.out.println("y:\n"+ Arrays.deepToString(y));
        System.out.println("x:\n"+ Arrays.deepToString(x));
        System.out.println("xCat:\n"+ Arrays.deepToString(xCat));

        yFinance.close();

        assertTrue(true);

    }


}