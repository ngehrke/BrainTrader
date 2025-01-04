package com.braintrader.visualization;

import com.braintrader.datamanagement.Price;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PriceChartVisualizer {

    private final Map<String, List<Price>> priceList;

    public PriceChartVisualizer(Map<String, List<Price>> mapOfPriceList) {
        this.priceList = mapOfPriceList;
    }

    public void visualize(boolean showOpen, boolean showClose, boolean showHigh, boolean showLow) {
        // Erstelle das Diagramm
        TimeSeriesCollection dataset = createDataset(showOpen, showClose, showHigh, showLow);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Price Chart", // Charttitel
                "Date", // X-Achsenbeschriftung
                "Price", // Y-Achsenbeschriftung
                dataset, // Daten
                true, // Legende anzeigen
                true, // Tooltips aktivieren
                false // URLs deaktivieren
        );

        // Diagramm anpassen
        customizeChart(chart);

        // Erstelle und zeige ein Fenster mit dem Diagramm
        JFrame frame = new JFrame("Price Chart");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private TimeSeriesCollection createDataset(boolean showOpen, boolean showClose, boolean showHigh, boolean showLow) {

        TimeSeriesCollection dataset = new TimeSeriesCollection();

        for (Map.Entry<String, List<Price>> entry : priceList.entrySet()) {

            String symbol = entry.getKey();
            List<Price> thePriceList = entry.getValue();

            TimeSeries closeSeries = new TimeSeries("Close " + symbol);
            TimeSeries openSeries = new TimeSeries("Open " + symbol);
            TimeSeries highSeries = new TimeSeries("High " + symbol);
            TimeSeries lowSeries = new TimeSeries("Low " + symbol);

            for (Price price : thePriceList) {

                LocalDate date = price.getDate();

                if (date != null) {

                    Day day = new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear());

                    if (price.getClose() != null) closeSeries.add(day, price.getClose());
                    if (price.getOpen() != null) openSeries.add(day, price.getOpen());
                    if (price.getHigh() != null) highSeries.add(day, price.getHigh());
                    if (price.getLow() != null) lowSeries.add(day, price.getLow());
                }
            }

            if (showClose) dataset.addSeries(closeSeries);
            if (showOpen) dataset.addSeries(openSeries);
            if (showHigh) dataset.addSeries(highSeries);
            if (showLow) dataset.addSeries(lowSeries);
        }

        return dataset;

    }

    private void customizeChart(JFreeChart chart) {

        XYPlot plot = (XYPlot) chart.getPlot();

        // Passe den Renderer an (Linien ohne Standardpunkte)
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        plot.setRenderer(renderer);

        // Iteriere über die Serien und füge Markierungen für Punkte hinzu
        for (Map.Entry<String, List<Price>> entry : priceList.entrySet()) {

            List<Price> thePriceList = entry.getValue();

            for (Price price : thePriceList) {

                if (price.getBuySignal() > 0) { // Nur wenn buySignal > 0

                    LocalDate date = price.getDate();

                    if (date != null && price.getClose() != null) {

                        // Erstelle eine Punktmarkierung
                        Day day = new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                        double xValue = day.getMiddleMillisecond();
                        double yValue = price.getClose();

                        // Punktform erstellen (z. B. kleiner roter Kreis)
                        double ySize = price.getBuySignal();

                        XYShapeAnnotation annotation = new XYShapeAnnotation(
                                new Ellipse2D.Double(xValue, yValue - ySize/2, 1, ySize),
                                new BasicStroke(1.0f), Color.BLACK, Color.BLACK
                        );

                        // Füge die Annotation dem Plot hinzu
                        plot.addAnnotation(annotation);
                    }
                }
            }
        }

        // Passe das Layout des Diagramms an
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(Color.BLACK);

    }



}


