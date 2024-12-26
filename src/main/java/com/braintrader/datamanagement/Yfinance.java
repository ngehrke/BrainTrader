package com.braintrader.datamanagement;

import com.braintrader.exceptions.YfinanceException;
import com.braintrader.measures.GeneralMeasure;
import com.braintrader.measures.IMeasure;
import jep.MainInterpreter;
import jep.Interpreter;
import jep.SharedInterpreter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Yfinance {

    private static String PYTHON_JEP_PATH="C:\\Program Files\\Python313\\Lib\\site-packages\\jep\\jep.dll";
    private static String localDbPath = "\\data\\stockdata";

    private final Connection con;
    private final Consumer<String> logger;

    public Yfinance(Consumer<String> logger) throws YfinanceException {

        if (logger==null) {
            throw new YfinanceException("Logger must not be null");
        }

        this.logger = logger;
        MainInterpreter.setJepLibraryPath(PYTHON_JEP_PATH);

        String dbPath = System.getProperty("user.dir") + localDbPath;

        // Connection to local h2 database
        try {

            this.con = DriverManager.getConnection("jdbc:h2:file:" + dbPath);
            this.createTables();

        } catch (SQLException e) {
            throw new YfinanceException("Error connecting to local database", e);
        }

        logger.accept("yfinance connected to local database at " + dbPath);

    }

    private void createTables() throws SQLException {

        try (Statement statement = this.con.createStatement()) {

            String sqlPrices= """
                    CREATE TABLE IF NOT EXISTS price (
                        symbol VARCHAR(255) NOT NULL, -- Das Ticker-Symbol, kein NULL-Wert erlaubt
                        pricedate DATE NOT NULL, -- Das Datum des Preises im YYYY-MM-DD-Format, kein NULL-Wert erlaubt
                        currency VARCHAR(10), -- Die Währung des Preises (z. B. USD, EUR)
                        open DOUBLE, -- Eröffnungskurs
                        close DOUBLE, -- Schlusskurs
                        high DOUBLE, -- Höchstkurs
                        low DOUBLE, -- Tiefstkurs
                        adjClose DOUBLE, -- Angepasster Schlusskurs
                        volume BIGINT, -- Handelsvolumen
                        PRIMARY KEY (symbol, pricedate) -- Primärschlüssel aus Symbol und Datum
                    );
                    """;

            statement.execute(sqlPrices);

            String sqlCompanyInfo = """
                    CREATE TABLE IF NOT EXISTS company_info (
                        symbol VARCHAR(255) NOT NULL, -- Das Ticker-Symbol, kein NULL-Wert erlaubt
                        property VARCHAR(512) NOT NULL, -- Die Eigenschaft des Unternehmens, kein NULL-Wert erlaubt
                        val VARCHAR, -- Der Wert der Eigenschaft
                        ts_entry TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Zeitstempel des Eintrags
                        PRIMARY KEY (symbol, property) -- Primärschlüssel aus Symbol und Eigenschaft
                    );
                    """;

            statement.execute(sqlCompanyInfo);

            String sqlCompanyMeasure = """
                    CREATE TABLE IF NOT EXISTS company_measure (
                        symbol VARCHAR(255) NOT NULL, -- Das Ticker-Symbol, kein NULL-Wert erlaubt
                        measuredate DATE NOT NULL, -- Das Datum des Maßes im YYYY-MM-DD-Format, kein NULL-Wert erlaubt
                        measure VARCHAR(512) NOT NULL, -- Die Eigenschaft des Unternehmens, kein NULL-Wert erlaubt
                        val DOUBLE PRECISION NOT NULL, -- Der Wert der Eigenschaft
                        ts_entry TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Zeitstempel des Eintrags
                        PRIMARY KEY (symbol,measuredate, measure) -- Primärschlüssel aus Symbol und Eigenschaft
                    );
                    """;

            statement.execute(sqlCompanyMeasure);

        }

    }

    public String getCompanyInfo(String symbol, String property) throws YfinanceException {

        if (symbol==null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (property==null) {
            throw new YfinanceException("Property must not be null");
        }

        String result = null;

        String sql = """
            SELECT val FROM company_info WHERE symbol = ? AND property = ?
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, property);

            try (ResultSet rs = preparedStatement.executeQuery()) {

                if (rs.next()) {
                    result = rs.getString(1);
                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting company info from database: "+e.getMessage(), e);
        }

        return result;

    }

    public List<IMeasure> getMeasures(String symbol, String measureName, LocalDate startDate, LocalDate endDate) throws YfinanceException  {

            if (symbol==null) {
                throw new YfinanceException("Symbol must not be null");
            }

            if (measureName==null) {
                throw new YfinanceException("Measure name must not be null");
            }

            if (startDate==null) {
                throw new YfinanceException("Start date must not be null");
            }

            if (endDate==null) {
                throw new YfinanceException("End date must not be null");
            }

            if (startDate.isAfter(endDate)) {
                throw new YfinanceException("Start date must not be after end date");
            }

            List<IMeasure> result = new ArrayList<>();

            String sql = """
                SELECT symbol, measuredate, measure, val FROM company_measure WHERE symbol = ? AND measure = ? AND measuredate >= ? AND measuredate <= ? ORDER BY measuredate
                """;

            try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

                preparedStatement.setString(1, symbol);
                preparedStatement.setString(2, measureName);
                preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));
                preparedStatement.setDate(4, java.sql.Date.valueOf(endDate));

                try (ResultSet rs = preparedStatement.executeQuery()) {

                    while (rs.next()) {

                        IMeasure measure = new GeneralMeasure(rs.getString(1), rs.getString(3), rs.getDate(2).toLocalDate(), rs.getDouble(4));
                        result.add(measure);

                    }

                }

            } catch (SQLException e) {
                throw new YfinanceException("Error getting measures from database: "+e.getMessage(), e);
            }

            return result;

    }

    public void saveMeasuresInDatabase(Collection<IMeasure> measures) throws YfinanceException  {

        if (measures==null) {
            throw new YfinanceException("Measures must not be null");
        }

        for (IMeasure measure : measures) {

            try {

                this.saveMeasureInDatabase(measure);

            } catch (YfinanceException e) {
                throw new YfinanceException("Error saving measures to database: "+e.getMessage(), e);
            }

        }

    }

    public boolean saveMeasureInDatabase(IMeasure measure) throws YfinanceException  {

        if (measure == null) {
            logger.accept("Measure must not be null, not saving measure");
            return false;
        }

        if (measure.getMeasureValue()==null) {
            logger.accept("Measure value is null, not saving measure for "+measure);
            return false;
        }

        String sql = """
            MERGE INTO company_measure (symbol, measuredate, measure, val)
            KEY (symbol, measuredate, measure)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, measure.getSymbol());
            preparedStatement.setDate(2, java.sql.Date.valueOf(measure.getMeasureDate()));
            preparedStatement.setString(3, measure.getMeasureName());
            preparedStatement.setDouble(4, measure.getMeasureValue());

            preparedStatement.execute();

            logger.accept("Saved measure for "+measure);

            return true;

        } catch (SQLException e) {
            throw new YfinanceException("Error saving measure to database: "+e.getMessage(), e);
        }

    }

    public LocalDate getDatePriceWasFirstTimeAboveThreshold(String symbol, LocalDate fromDate, LocalDate toDate, double thresholdPrice) throws YfinanceException {

        if (symbol==null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (fromDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (toDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (fromDate.isAfter(toDate)) {
            throw new YfinanceException("From date must not be after to date");
        }

        LocalDate result = null;

        String sql = """
            SELECT MIN(pricedate) FROM price WHERE symbol = ? AND pricedate >= ? AND pricedate <= ? AND high > ?
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setDate(2, java.sql.Date.valueOf(fromDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(toDate));
            preparedStatement.setDouble(4, thresholdPrice);

            try (ResultSet rs = preparedStatement.executeQuery()) {

                if (rs.next() && rs.getDate(1)!=null) {
                        result = rs.getDate(1).toLocalDate();
                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting date where price was first time above threshold: "+e.getMessage(), e);
        }

        return result;

    }

    public LocalDate getDatePriceWasFirstTimeBelowThreshold(String symbol, LocalDate fromDate, LocalDate toDate, double thresholdPrice) throws YfinanceException {

        if (symbol==null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (fromDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (toDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (fromDate.isAfter(toDate)) {
            throw new YfinanceException("From date must not be after to date");
        }

        LocalDate result = null;

        String sql = """
            SELECT MIN(pricedate) FROM price WHERE symbol = ? AND pricedate >= ? AND pricedate <= ? AND low < ?
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setDate(2, java.sql.Date.valueOf(fromDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(toDate));
            preparedStatement.setDouble(4, thresholdPrice);

            try (var rs = preparedStatement.executeQuery()) {

                if (rs.next() && rs.getDate(1)!=null) {

                    result = rs.getDate(1).toLocalDate();

                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting date where price was first time below threshold: "+e.getMessage(), e);
        }

        return result;

    }

    public boolean wasPriceBelowThreshold(String symbol, LocalDate fromDate, LocalDate toDate, double thresholdPrice) throws YfinanceException {

        boolean result = false;

        try {

            Double lowestPrice = getLowestPriceInPeriod(symbol, fromDate, toDate);

            if (lowestPrice != null && lowestPrice < thresholdPrice) {
                result = true;
            }

        } catch (YfinanceException e) {
            throw new YfinanceException("Error checking if price was below threshold: "+e.getMessage(), e);
        }

        return result;

    }

    public LocalDate getDateOfLowestPriceInPeriod(String symbol, LocalDate fromDate, LocalDate toDate) throws YfinanceException {

        if (symbol==null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (fromDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (toDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (fromDate.isAfter(toDate)) {
            throw new YfinanceException("From date must not be after to date");
        }

        LocalDate result = null;

        String sql = """
            SELECT pricedate FROM price WHERE symbol = ? AND pricedate >= ? AND pricedate <= ? ORDER BY low ASC LIMIT 1
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setDate(2, java.sql.Date.valueOf(fromDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(toDate));

            try (var rs = preparedStatement.executeQuery()) {

                if (rs.next()) {

                    result = rs.getDate(1).toLocalDate();

                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting date of lowest price in period: "+e.getMessage(), e);
        }

        return result;

    }

    public Double getLowestPriceInPeriod(String symbol, LocalDate fromDate, LocalDate toDate) throws YfinanceException {

        if (symbol==null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (fromDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (toDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (fromDate.isAfter(toDate)) {
            throw new YfinanceException("From date must not be after to date");
        }

        Double result = null;

        String sql = """
            SELECT MIN(low) FROM price WHERE symbol = ? AND pricedate >= ? AND pricedate <= ?
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setDate(2, java.sql.Date.valueOf(fromDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(toDate));

            try (var rs = preparedStatement.executeQuery()) {

                if (rs.next()) {

                    result = rs.getDouble(1);

                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting lowest price in period: "+e.getMessage(), e);
        }

        return result;

    }

    public LocalDate getDateOfHighestPriceInPeriod(String symbol, LocalDate fromDate, LocalDate toDate) throws YfinanceException {

        if (symbol==null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (fromDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (toDate==null) {
            throw new YfinanceException("Date must not be null");
        }

        if (fromDate.isAfter(toDate)) {
            throw new YfinanceException("From date must not be after to date");
        }

        LocalDate result = null;

        String sql = """
            SELECT pricedate FROM price WHERE symbol = ? AND pricedate >= ? AND pricedate <= ? ORDER BY high DESC LIMIT 1
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setDate(2, java.sql.Date.valueOf(fromDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(toDate));

            try (var rs = preparedStatement.executeQuery()) {

                if (rs.next()) {

                    result = rs.getDate(1).toLocalDate();

                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting date of highest price in period: "+e.getMessage(), e);
        }

        return result;

    }

    public Double getHighestPriceInPeriod(String symbol, LocalDate fromDate, LocalDate toDate) throws YfinanceException {

            if (symbol==null) {
                throw new YfinanceException("Symbol must not be null");
            }

            if (fromDate==null) {
                throw new YfinanceException("Date must not be null");
            }

            if (toDate==null) {
                throw new YfinanceException("Date must not be null");
            }

            if (fromDate.isAfter(toDate)) {
                throw new YfinanceException("From date must not be after to date");
            }

            Double result = null;

            String sql = """
                SELECT MAX(high) FROM price WHERE symbol = ? AND pricedate >= ? AND pricedate <= ?
                """;

            try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

                preparedStatement.setString(1, symbol);
                preparedStatement.setDate(2, java.sql.Date.valueOf(fromDate));
                preparedStatement.setDate(3, java.sql.Date.valueOf(toDate));

                try (var rs = preparedStatement.executeQuery()) {

                    if (rs.next()) {

                        result = rs.getDouble(1);

                    }

                }

            } catch (SQLException e) {
                throw new YfinanceException("Error getting highest price in period: "+e.getMessage(), e);
            }

            return result;
    }

    public LocalDate getNearestTradingDayFromDatabase(String symbol, LocalDate date) throws YfinanceException {

        if (symbol==null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (date==null) {
            throw new YfinanceException("Date must not be null");
        }

        LocalDate result = null;

        String sql = """
            SELECT MIN(pricedate) FROM price WHERE symbol = ? AND pricedate >= ?
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));

            try (var rs = preparedStatement.executeQuery()) {

                if (rs.next() && rs.getDate(1)!=null) {

                    result = rs.getDate(1).toLocalDate();

                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting next trading day: "+e.getMessage(), e);
        }

        return result;

    }

    public Price getPriceFromDatabase(String symbol, LocalDate date, int tradingDaysAfter) throws YfinanceException {

        if (symbol == null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (date == null) {
            throw new YfinanceException("Date must not be null");
        }

        if (tradingDaysAfter < 0) {
            throw new YfinanceException("Trading days after must not be negative");
        }

        Price result = null;

        String sql = """
            SELECT symbol, pricedate, currency, open, close, high, low, adjClose, volume
            FROM price
            WHERE symbol = ? AND pricedate >= ? 
            ORDER BY pricedate
            LIMIT 1 OFFSET ?
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            preparedStatement.setInt(3, tradingDaysAfter);

            try (var rs = preparedStatement.executeQuery()) {

                if (rs.next()) {

                    result = new Price();

                    result.symbol = rs.getString(1);
                    result.date = rs.getDate(2).toLocalDate();
                    result.currency = rs.getString(3);
                    result.open = rs.getDouble(4);
                    result.close = rs.getDouble(5);
                    result.high = rs.getDouble(6);
                    result.low = rs.getDouble(7);
                    result.adjClose = rs.getDouble(8);
                    result.volume = rs.getLong(9);

                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting price from database: "+e.getMessage(), e);
        }

        return result;



    }

    public Price getPriceFromDatabase(String symbol, LocalDate date) throws YfinanceException {

        if (symbol == null) {
            throw new YfinanceException("Symbol must not be null");
        }

        if (date == null) {
            throw new YfinanceException("Date must not be null");
        }

        Price result = null;

        String sql = """
            SELECT symbol, pricedate, currency, open, close, high, low, adjClose, volume
            FROM price
            WHERE symbol = ? AND pricedate = ?
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));

            try (var rs = preparedStatement.executeQuery()) {

                if (rs.next()) {

                    result = new Price();

                    result.symbol = rs.getString(1);
                    result.date = rs.getDate(2).toLocalDate();
                    result.currency = rs.getString(3);
                    result.open = rs.getDouble(4);
                    result.close = rs.getDouble(5);
                    result.high = rs.getDouble(6);
                    result.low = rs.getDouble(7);
                    result.adjClose = rs.getDouble(8);
                    result.volume = rs.getLong(9);

                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting price from database: "+e.getMessage(), e);
        }

        return result;

    }

    public void savePrices(Map<LocalDate, Price> prices) throws YfinanceException {


        if (prices == null) {
            throw new YfinanceException("Prices must not be null");
        }

        Set<String> symbols = new HashSet<>();

        String sql = """
            MERGE INTO price (symbol, pricedate, currency, open, close, high, low, adjClose, volume)
            KEY (symbol, pricedate)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            int counter=0;
            for (Price price : prices.values()) {

                symbols.add(price.symbol);

                preparedStatement.setString(1, price.symbol);
                preparedStatement.setDate(2, java.sql.Date.valueOf(price.date));

                if (price.currency!=null) {
                    preparedStatement.setString(3, price.currency);
                } else {
                    preparedStatement.setNull(3, java.sql.Types.VARCHAR);
                }

                if (price.open!=null) {
                preparedStatement.setObject(4, price.open, java.sql.Types.DOUBLE);
                } else {
                    preparedStatement.setNull(4, java.sql.Types.DOUBLE);
                }

                if (price.close!=null) {
                    preparedStatement.setObject(5, price.close, java.sql.Types.DOUBLE);
                } else {
                    preparedStatement.setNull(5, java.sql.Types.DOUBLE);
                }

                if (price.high!=null) {
                    preparedStatement.setObject(6, price.high, java.sql.Types.DOUBLE);
                } else {
                    preparedStatement.setNull(6, java.sql.Types.DOUBLE);
                }

                if (price.low!=null) {
                    preparedStatement.setObject(7, price.low, java.sql.Types.DOUBLE);
                } else {
                    preparedStatement.setNull(7, java.sql.Types.DOUBLE);
                }

                if (price.adjClose!=null) {
                    preparedStatement.setObject(8, price.adjClose, java.sql.Types.DOUBLE);
                } else {
                    preparedStatement.setNull(8, java.sql.Types.DOUBLE);
                }

                if (price.volume!=null) {
                    preparedStatement.setObject(9, price.volume, java.sql.Types.BIGINT);
                } else {
                    preparedStatement.setNull(9, java.sql.Types.BIGINT);
                }

                // Füge den Eintrag zum Batch hinzu
                preparedStatement.addBatch();
                counter++;

                if (counter % 1000 == 0) {
                    preparedStatement.executeBatch();
                }

            }

            // Führe den Batch aus
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new YfinanceException("Error saving prices to database: " + e.getMessage(), e);
        }

        logger.accept("Saved " + prices.size() + " prices for "+symbols+" to database");

    }

    public void close() {

        if (this.con==null) {
            return;
        }

        try (Statement statement = this.con.createStatement()) {

            statement.execute("SHUTDOWN COMPACT");

            if (!con.isClosed()) {
                con.close();
            }


        } catch (SQLException e) {
            logger.accept("Error closing database connection: " + e.getMessage());
        }

        logger.accept("yfinance closed");

    }

    public void updateAllStockInfosInDatabase() throws YfinanceException {

        Set<String> symbols = new LinkedHashSet<>();

        try (Statement statement = this.con.createStatement()) {

            try (var rs = statement.executeQuery("SELECT DISTINCT symbol FROM price ORDER BY symbol ")) {

                while (rs.next()) {
                    symbols.add(rs.getString(1));
                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting symbols from database: "+e.getMessage(), e);
        }

        for (String symbol : symbols) {

            try {

                log("Updating info for "+symbol);

                Map<String, String> stockInfo = this.getStockInfo(symbol);
                this.saveStockInfo(symbol,stockInfo);

                Thread.sleep(200);

            } catch (YfinanceException e) {

                logger.accept("Error updating info for "+symbol+": "+e.getMessage());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

    }

    public void updateAllStockPricesInDatabase() throws YfinanceException {

        Set<String> symbols = new LinkedHashSet<>();

        try (Statement statement = this.con.createStatement()) {

            try (var rs = statement.executeQuery("SELECT DISTINCT symbol FROM price ORDER BY symbol")) {

                while (rs.next()) {
                    symbols.add(rs.getString(1));
                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting symbols from database: "+e.getMessage(), e);
        }

        for (String symbol : symbols) {

            try {

                log("Updating prices for "+symbol);

                Map<LocalDate, Price> prices = this.updatePrices(symbol);
                this.savePrices(prices);

                Thread.sleep(1000);

            } catch (YfinanceException e) {

                logger.accept("Error updating prices for "+symbol+": "+e.getMessage());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

    }

    public LocalDate getLastPriceEntry(String symbol) throws YfinanceException {

            if (symbol==null) {
                throw new YfinanceException("Symbol must not be null");
            }

            LocalDate result = null;

            String sql = """
                SELECT MAX(pricedate) FROM price WHERE symbol = ?
                """;

            try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

                preparedStatement.setString(1, symbol);

                try (var rs = preparedStatement.executeQuery()) {

                    if (rs.next()) {

                        result = rs.getDate(1).toLocalDate();

                    }

                }

            } catch (SQLException e) {
                throw new YfinanceException("Error getting last price entry for "+symbol+": "+e.getMessage(), e);
            }

            return result;

    }

    public List<Price> getAllPricesInDatabase(String symbol) throws YfinanceException {

            List<Price> result = new ArrayList<>();

            String sql = """
                SELECT symbol, pricedate, currency, open, close, high, low, adjClose, volume
                FROM price WHERE symbol = ? ORDER BY pricedate
                """;

            try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

                preparedStatement.setString(1, symbol);

                try (var rs = preparedStatement.executeQuery()) {

                    while (rs.next()) {

                        Price price = new Price();

                        price.symbol = rs.getString(1);
                        price.date = rs.getDate(2).toLocalDate();
                        price.currency = rs.getString(3);
                        price.open = rs.getDouble(4);
                        price.close = rs.getDouble(5);
                        price.high = rs.getDouble(6);
                        price.low = rs.getDouble(7);
                        price.adjClose = rs.getDouble(8);
                        price.volume = rs.getLong(9);

                        result.add(price);

                    }

                }

            } catch (SQLException e) {
                throw new YfinanceException("Error getting all prices for "+symbol+": "+e.getMessage(), e);
            }

            return result;

    }

    public Set<LocalDate> getDatesOfAllPriceEntries(String symbol) throws YfinanceException {

        Set<LocalDate> result = new LinkedHashSet<>();

        String sql = """
            SELECT DISTINCT pricedate FROM price WHERE symbol = ? ORDER BY pricedate
            """;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);

            try (var rs = preparedStatement.executeQuery()) {

                while (rs.next()) {
                    result.add(rs.getDate(1).toLocalDate());
                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting dates of all price entries for "+symbol+": "+e.getMessage(), e);
        }

        return result;

    }

    public LocalDate getFirstPriceEntry(String symbol) throws YfinanceException {

            if (symbol==null) {
                throw new YfinanceException("Symbol must not be null");
            }

            LocalDate result = null;

            String sql = """
                SELECT MIN(pricedate) FROM price WHERE symbol = ?
                """;

            try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

                preparedStatement.setString(1, symbol);

                try (var rs = preparedStatement.executeQuery()) {

                    if (rs.next()) {

                        result = rs.getDate(1).toLocalDate();

                    }

                }

            } catch (SQLException e) {
                throw new YfinanceException("Error getting first price entry for "+symbol+": "+e.getMessage(), e);
            }

            return result;
    }

    public Set<String> getAllSymbolsInDatabase() throws YfinanceException {

        Set<String> result = new LinkedHashSet<>();

        try (Statement statement = this.con.createStatement()) {

            try (var rs = statement.executeQuery("SELECT DISTINCT symbol FROM price ORDER BY symbol")) {

                while (rs.next()) {
                    result.add(rs.getString(1));
                }

            }

        } catch (SQLException e) {
            throw new YfinanceException("Error getting symbols from database: "+e.getMessage(), e);
        }

        return result;

    }

    // gets the latest prices for a stock
    protected Map<LocalDate,Price> updatePrices(String tickerSymbol) throws YfinanceException {

        Map<LocalDate, Price> result = new HashMap<>();

        // get the latest price entry in table price
        String sql = """
            SELECT MAX(pricedate) FROM price WHERE symbol = ?
            """;

        LocalDate lastDate = null;

        try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

            preparedStatement.setString(1, tickerSymbol);

            try (var rs = preparedStatement.executeQuery()) {

                if (rs.next()) {

                    lastDate = rs.getDate(1).toLocalDate();

                } else {
                    return result;
                }

            }

        } catch (SQLException e) {
            logger.accept("Error getting latest price date for "+tickerSymbol+": "+e.getMessage());
        }

        result= this.getStockPrices(tickerSymbol, lastDate, LocalDate.now());

        return result;


    }

    public void getAndSaveStockPricesInDatabase(String tickerSymbol, LocalDate startDate, LocalDate endDate) throws YfinanceException {

        Map<LocalDate, Price> prices = this.getStockPrices(tickerSymbol, startDate, endDate);
        this.savePrices(prices);

    }

    public void getAndSaveStockInfoInDatabase(String tickerSymbol) throws YfinanceException {

        Map<String, String> stockInfo = this.getStockInfo(tickerSymbol);
        this.saveStockInfo(tickerSymbol,stockInfo);

    }

    public void saveStockInfo(String tickerSymbol, Map<String,String> stockInfoMap) throws YfinanceException  {

            if (tickerSymbol == null) {
                throw new YfinanceException("Ticker symbol must not be null");
            }

            if (stockInfoMap == null) {
                throw new YfinanceException("Stock info map must not be null");
            }

            String sql = """
                MERGE INTO company_info (symbol, property, val, ts_entry )
                KEY (symbol, property)
                VALUES (?, ?, ?, CURRENT_TIMESTAMP)
                """;

            try (PreparedStatement preparedStatement = this.con.prepareStatement(sql)) {

                for (Map.Entry<String, String> entry : stockInfoMap.entrySet()) {

                    preparedStatement.setString(1, tickerSymbol);
                    preparedStatement.setString(2, entry.getKey());
                    preparedStatement.setString(3, entry.getValue());

                    preparedStatement.addBatch();

                }

                preparedStatement.executeBatch();

            } catch (SQLException e) {
                logger.accept("Error saving stock info to database: "+e.getMessage());
            }
    }

    public Map<String,String> getStockInfo(String tickerSymbol) throws YfinanceException {

        Map<String,String> result = new HashMap<>();

        try (Interpreter interp = new SharedInterpreter()) {

            interp.exec("import yfinance as yf");
            interp.set("ticker", tickerSymbol);
            interp.exec("stock = yf.Ticker(ticker)");
            interp.exec("info = stock.info");
            interp.exec("result = {key: str(info[key]) for key in info.keys()}");

            @SuppressWarnings("unchecked")
            HashMap<String, String> info = (HashMap<String, String>) interp.getValue("result");

            result.putAll(info);

        } catch (Exception e) {
            throw new YfinanceException("Error getting stock info: "+e.getMessage(), e);
        }

        return result;

    }

    public Map<LocalDate,Price> getStockPrices(String tickerSymbol, LocalDate startDate, LocalDate endDate) throws YfinanceException  {

        Map<LocalDate,Price> result = new HashMap<>();

        // Maps zur Speicherung der Ergebnisse
        Map<String, Double> openPrices = new HashMap<>();
        Map<String, Double> closePrices = new HashMap<>();
        Map<String, Double> highPrices = new HashMap<>();
        Map<String, Double> lowPrices = new HashMap<>();
        Map<String, Double> adjClosePrices = new HashMap<>();
        Map<String, Long> volumeData = new HashMap<>();

        String currency = ""; // Variable zur Speicherung der Währung

        try (Interpreter interp = new SharedInterpreter()) {
            // Importieren der erforderlichen Python-Module
            interp.exec("import yfinance as yf");
            interp.exec("import pandas as pd");

            // Setzen der Ticker-Symbol- und Datumsvariablen in Python
            interp.set("ticker", tickerSymbol);
            interp.set("start_date", startDate.toString());
            interp.set("end_date", endDate.toString());

            // Abrufen der Ticker-Informationen (inklusive Währung)
            interp.exec("stock = yf.Ticker(ticker)");
            interp.exec("currency = stock.info.get('currency', 'Unknown')");

            // Währung abrufen
            currency = (String) interp.getValue("currency");
            log("Currency of prices for "+tickerSymbol+": " + currency);

            // Abrufen der historischen Aktienkursdaten und Erstellung der Dictionaries
            interp.exec("data = stock.history(start=start_date, end=end_date, interval='1d')");

            // Separate Dictionaries für jede Preiskategorie erstellen
            interp.exec(
                    "open_dict = {date.strftime('%Y-%m-%d'): row['Open'] for date, row in data.iterrows()} if 'Open' in data.columns else {}\n" +
                       "close_dict = {date.strftime('%Y-%m-%d'): row['Close'] for date, row in data.iterrows()} if 'Close' in data.columns else {}\n" +
                       "high_dict = {date.strftime('%Y-%m-%d'): row['High'] for date, row in data.iterrows()} if 'High' in data.columns else {}\n" +
                       "low_dict = {date.strftime('%Y-%m-%d'): row['Low'] for date, row in data.iterrows()} if 'Low' in data.columns else {}\n" +
                       "adj_close_dict = {date.strftime('%Y-%m-%d'): row['Adj Close'] for date, row in data.iterrows()} if 'Adj Close' in data.columns else {}\n" +
                       "volume_dict = {date.strftime('%Y-%m-%d'): int(row['Volume']) for date, row in data.iterrows()} if 'Volume' in data.columns else {}"
            );

            // Abrufen der Dictionaries in Java
            @SuppressWarnings("unchecked")
            HashMap<String, Double> openDict = (HashMap<String, Double>) interp.getValue("open_dict");
            @SuppressWarnings("unchecked")
            HashMap<String, Double> closeDict = (HashMap<String, Double>) interp.getValue("close_dict");
            @SuppressWarnings("unchecked")
            HashMap<String, Double> highDict = (HashMap<String, Double>) interp.getValue("high_dict");
            @SuppressWarnings("unchecked")
            HashMap<String, Double> lowDict = (HashMap<String, Double>) interp.getValue("low_dict");
            @SuppressWarnings("unchecked")
            HashMap<String, Double> adjCloseDict = (HashMap<String, Double>) interp.getValue("adj_close_dict");
            @SuppressWarnings("unchecked")
            HashMap<String, Long> volumeDict = (HashMap<String, Long>) interp.getValue("volume_dict");

            // Übertragen der Daten in die Java-Maps
            openPrices.putAll(openDict);
            closePrices.putAll(closeDict);
            highPrices.putAll(highDict);
            lowPrices.putAll(lowDict);
            adjClosePrices.putAll(adjCloseDict);
            volumeData.putAll(volumeDict);

            Set<String> allKeys = new HashSet<>();
            allKeys.addAll(openPrices.keySet());
            allKeys.addAll(closePrices.keySet());
            allKeys.addAll(highPrices.keySet());
            allKeys.addAll(lowPrices.keySet());
            allKeys.addAll(adjClosePrices.keySet());
            allKeys.addAll(volumeData.keySet());

            for(String dateKey : allKeys) {

                LocalDate date = LocalDate.parse(dateKey);

                Price price = new Price();
                price.symbol = tickerSymbol;
                price.date = date;
                price.currency = currency;
                price.open = openPrices.get(dateKey);
                price.close = closePrices.get(dateKey);
                price.high = highPrices.get(dateKey);
                price.low = lowPrices.get(dateKey);
                price.adjClose = adjClosePrices.get(dateKey);
                price.volume = volumeData.get(dateKey);

                result.put(date, price);

            }



        } catch (Exception e) {
            throw new YfinanceException("Error getting stock prices: "+e.getMessage(), e);
        }

        return result;

    }



    private void log(String message) {

        logger.accept(message);

    }


}
