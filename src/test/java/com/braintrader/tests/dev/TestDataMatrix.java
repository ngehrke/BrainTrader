package com.braintrader.tests.dev;

import ai.catboost.CatBoostError;
import ai.catboost.CatBoostModel;
import ai.catboost.CatBoostPredictions;
import com.braintrader.datamanagement.CatboostTrainer;
import com.braintrader.datamanagement.DataModelManager;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.CatboostException;
import com.braintrader.exceptions.DataModelException;
import com.braintrader.exceptions.YfinanceException;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestDataMatrix {

    @Test
    void test() throws YfinanceException, DataModelException, CatboostException, CatBoostError {

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

        CatboostTrainer trainer = new CatboostTrainer(y, x, xCat, dataModelManager.getRhsMeasureColumnNames(), dataModelManager.getRhsCategoryColumnNames());

        String modelPath = "model.cbm";
        trainer.trainModel(modelPath, 0.8);

        // Example: Making predictions
        CatBoostModel model = CatBoostModel.loadModel(modelPath);

        float[][] xFloat = DataModelManager.convertToFloatArray(x);
        CatBoostPredictions predictions = model.predict(xFloat,xCat);

        System.out.println("Predictions: " + predictions.get(0, 0));

        model.close();

        yFinance.close();

        assertTrue(true);

    }


}
