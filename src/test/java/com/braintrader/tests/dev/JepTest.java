package com.braintrader.tests.dev;


import jep.Interpreter;
import jep.MainInterpreter;
import jep.SharedInterpreter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


class JepTest {

    @Test
    void testJep() {

        MainInterpreter.setJepLibraryPath("C:\\Program Files\\Python313\\Lib\\site-packages\\jep\\jep.dll");

        // Ticker-Symbol der gewünschten Aktie
        String tickerSymbol = "AAPL"; // Beispiel: Apple Inc.

        // Berechnung des Datums vor drei Jahren
        java.time.LocalDate endDate = java.time.LocalDate.now();
        java.time.LocalDate startDate = endDate.minusYears(3);

        // Map zur Speicherung der Ergebnisse
        Map<String, Double> stockPrices = new HashMap<>();

        try (Interpreter interp = new SharedInterpreter()) {
            // Importieren der erforderlichen Python-Module
            interp.exec("import yfinance as yf");
            interp.exec("import pandas as pd");

            // Setzen der Ticker-Symbol- und Datumsvariablen in Python
            interp.set("ticker", tickerSymbol);
            interp.set("start_date", startDate.toString());
            interp.set("end_date", endDate.toString());

            // Abrufen der historischen Aktienkursdaten
            interp.exec("stock = yf.Ticker(ticker)");
            interp.exec("data = stock.history(start=start_date, end=end_date, interval='1d')");

            // Überprüfen, ob Daten vorhanden sind
            interp.exec("data_exists = not data.empty");
            boolean dataExists = (Boolean) interp.getValue("data_exists");

            if (dataExists) {
                // Konvertieren der Daten in ein Dictionary mit Datum als String und Schlusskurs als Wert
                interp.exec("data_dict = {date.strftime('%Y-%m-%d'): row['Close'] for date, row in data.iterrows()}");

                // Abrufen des Dictionaries in Java
                @SuppressWarnings("unchecked")
                HashMap<String, Double> dataDict = (HashMap<String, Double>) interp.getValue("data_dict");

                // Übertragen der Daten in die Java-Map
                stockPrices.putAll(dataDict);

                // Ausgabe der gesammelten Daten
                for (Map.Entry<String, Double> entry : stockPrices.entrySet()) {
                    System.out.println("Datum: " + entry.getKey() + ", Schlusskurs: " + entry.getValue() + " USD");
                }
            } else {
                System.out.println("Keine Daten für den angegebenen Zeitraum verfügbar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(true);

    }

}
