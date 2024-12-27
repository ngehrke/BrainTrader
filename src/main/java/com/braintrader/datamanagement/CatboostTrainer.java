package com.braintrader.datamanagement;

import com.braintrader.exceptions.CatboostException;
import jep.Interpreter;
import jep.JepException;
import jep.SharedInterpreter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CatboostTrainer {

    private final double[] y;
    private final double[][] x;
    private final String[][] xCat;
    private final List<String> rhsMeasureColumnNames;
    private final List<String> rhsCategoryColumnNames;

    public CatboostTrainer(Double[] y, Double[][] x, String[][] xCat,
                           List<String> rhsMeasureColumnNames, List<String> rhsCategoryColumnNames) {

        this.y = convertToPrimitive(y);
        this.x = convertToPrimitive(x);
        this.xCat = xCat;
        this.rhsMeasureColumnNames = rhsMeasureColumnNames;
        this.rhsCategoryColumnNames = rhsCategoryColumnNames;

        Python.setInterpreterPath();

    }

    public void trainModel(String modelPath, double trainSplit) throws CatboostException {

        try (Interpreter interp = new SharedInterpreter()) {

            // Split data into train and test sets
            int splitIdx = (int) (y.length * trainSplit);

            double[][] xTrain = IntStream.range(0, splitIdx).mapToObj(i -> x[i]).toArray(double[][]::new);
            double[][] xTest = IntStream.range(splitIdx, y.length).mapToObj(i -> x[i]).toArray(double[][]::new);
            String[][] xCatTrain = IntStream.range(0, splitIdx).mapToObj(i -> xCat[i]).toArray(String[][]::new);
            String[][] xCatTest = IntStream.range(splitIdx, y.length).mapToObj(i -> xCat[i]).toArray(String[][]::new);
            double[] yTrain = IntStream.range(0, splitIdx).mapToDouble(i -> y[i]).toArray();
            double[] yTest = IntStream.range(splitIdx, y.length).mapToDouble(i -> y[i]).toArray();

            // Set training data
            interp.set("x", xTrain);
            interp.set("xCat", xCatTrain);
            interp.set("y", yTrain);
            interp.set("model_path", modelPath);

            // Determine yMin and yMax for clipping
            double yMin = DoubleStream.of(y).filter(value -> !Double.isNaN(value)).min().orElse(Double.NEGATIVE_INFINITY);
            double yMax = DoubleStream.of(y).filter(value -> !Double.isNaN(value)).max().orElse(Double.POSITIVE_INFINITY);

            // Create Pool and train the model
            interp.eval("import numpy as np");
            interp.eval("from catboost import CatBoostRegressor, Pool");
            interp.eval("train_pool = Pool(data=x, cat_features=xCat, label=y)");
            interp.eval("model = CatBoostRegressor(iterations=100, depth=6, learning_rate=0.1, loss_function='RMSE')");
            interp.eval("model.fit(train_pool, verbose=False)");

            // Save the model
            interp.eval("model.save_model(model_path, format='cbm')");

            // Predict and apply clipping
            interp.eval("test_pool = Pool(data=xTest, cat_features=xCatTest, label=yTest)");
            interp.eval("predictions = model.predict(test_pool)");
            interp.set("y_min", yMin);
            interp.set("y_max", yMax);
            interp.eval("clipped_predictions = np.clip(predictions, y_min, y_max)");
            interp.eval("print('Clipped Predictions:', clipped_predictions)");

        } catch (JepException e) {
            throw new CatboostException("Error while training Catboost model: "+e.getMessage(), e);
        }


    }

    private double[] convertToPrimitive(Double[] array) {

        return Stream.of(array)
                .mapToDouble(value -> value != null ? value : Double.NaN)
                .toArray();

    }

    private double[][] convertToPrimitive(Double[][] matrix) {

        return Arrays.stream(matrix).map(doubles -> doubles != null ? convertToPrimitive(doubles) : new double[matrix[0].length])
                .toArray(double[][]::new);

    }


}
