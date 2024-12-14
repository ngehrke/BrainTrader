package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Price;
import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class TestYfinance {

    @Test
    void testYfinance() throws YfinanceException, InterruptedException {

        Map<String,String> companies = getDax30Companies();

        Set<String> symbols = companies.keySet();

        Yfinance yFinance = new Yfinance(System.out::println);

        LocalDate startDate = LocalDate.of(1950, 1, 1);

        for(String symbol : symbols) {

            System.out.println("Getting prices for " + symbol + " (" + companies.get(symbol) + ")");

            Map<LocalDate, Price> prices = yFinance.getStockPrices(symbol, startDate, LocalDate.now());
            yFinance.savePrices(prices);

            Thread.sleep(10000);

        }

        yFinance.close();

    }

    public static Map<String, String> getDax30Companies() {

        Map<String, String> companies = new HashMap<>();

        companies.put("DHL.DE", "Deutsche Post AG");
        companies.put("AIR.DE", "Airbus SE");
        companies.put("FRE.DE", "Fresenius SE & Co. KGaA");
        companies.put("1COV.DE", "Covestro AG");
        companies.put("DB1.DE", "Deutsche Boerse AG");
        companies.put("BMW.DE", "Bayerische Motoren Werke Aktiengesellschaft");
        companies.put("SY1.DE", "Symrise AG");
        companies.put("P911.DE", "Dr. Ing. h.c. F. Porsche AG");
        companies.put("ENR.DE", "Siemens Energy AG");
        companies.put("HEI.DE", "Heidelberg Materials AG");
        companies.put("DTG.DE", "Daimler Truck Holding AG");
        companies.put("DTE.DE", "Deutsche Telekom AG");
        companies.put("IFX.DE", "Infineon Technologies AG");
        companies.put("SHL.DE", "Siemens Healthineers AG");
        companies.put("DBK.DE", "Deutsche Bank AG");
        companies.put("EOAN.DE", "E.ON SE");
        companies.put("MTX.DE", "MTU Aero Engines AG");
        companies.put("ALV.DE", "Allianz SE");
        companies.put("CON.DE", "Continental Aktiengesellschaft");
        companies.put("BAS.DE", "BASF SE");
        companies.put("BEI.DE", "Beiersdorf Aktiengesellschaft");
        companies.put("ZAL.DE", "Zalando SE");
        companies.put("SIE.DE", "Siemens Aktiengesellschaft");
        companies.put("VOW3.DE", "Volkswagen AG");
        companies.put("MRK.DE", "Merck KGaA");
        companies.put("HNR1.DE", "Hannover Rück SE");
        companies.put("ADS.DE", "adidas AG");
        companies.put("BAYN.DE", "Bayer Aktiengesellschaft");
        companies.put("RWE.DE", "RWE AG");
        companies.put("VNA.DE", "Vonovia SE");

        return companies;
    }

    public static Map<String,String> getDowJonesCompanies() {

        Map<String, String> dowJonesCompanies = new HashMap<>();

        dowJonesCompanies.put("MMM", "3M");
        dowJonesCompanies.put("AXP", "American Express");
        dowJonesCompanies.put("AMGN", "Amgen");
        dowJonesCompanies.put("AMZN", "Amazon");
        dowJonesCompanies.put("AAPL", "Apple");
        dowJonesCompanies.put("BA", "Boeing");
        dowJonesCompanies.put("CAT", "Caterpillar");
        dowJonesCompanies.put("CVX", "Chevron");
        dowJonesCompanies.put("CSCO", "Cisco Systems");
        dowJonesCompanies.put("KO", "Coca-Cola");
        dowJonesCompanies.put("DIS", "Walt Disney");
        dowJonesCompanies.put("GS", "Goldman Sachs");
        dowJonesCompanies.put("HD", "Home Depot");
        dowJonesCompanies.put("HON", "Honeywell");
        dowJonesCompanies.put("IBM", "IBM");
        dowJonesCompanies.put("JNJ", "Johnson & Johnson");
        dowJonesCompanies.put("JPM", "JPMorgan Chase");
        dowJonesCompanies.put("MCD", "McDonald's");
        dowJonesCompanies.put("MRK", "Merck & Co.");
        dowJonesCompanies.put("MSFT", "Microsoft");
        dowJonesCompanies.put("NKE", "Nike");
        dowJonesCompanies.put("NVDA", "Nvidia");
        dowJonesCompanies.put("PG", "Procter & Gamble");
        dowJonesCompanies.put("CRM", "Salesforce");
        dowJonesCompanies.put("SHW", "Sherwin-Williams");
        dowJonesCompanies.put("TRV", "Travelers Companies");
        dowJonesCompanies.put("UNH", "UnitedHealth Group");
        dowJonesCompanies.put("VZ", "Verizon");
        dowJonesCompanies.put("V", "Visa");
        dowJonesCompanies.put("WMT", "Walmart");

        return dowJonesCompanies;

    }

    public static Map<String, String> getNasdaq100Companies() {

        Map<String, String> companies = new HashMap<>();
        companies.put("AAPL", "Apple Inc.");
        companies.put("MSFT", "Microsoft Corporation");
        companies.put("NVDA", "NVIDIA Corporation");
        companies.put("AMZN", "Amazon.com, Inc.");
        companies.put("GOOG", "Alphabet Inc.");
        companies.put("GOOGL", "Alphabet Inc.");
        companies.put("META", "Meta Platforms, Inc.");
        companies.put("TSLA", "Tesla, Inc.");
        companies.put("AVGO", "Broadcom Inc.");
        companies.put("COST", "Costco Wholesale Corporation");
        companies.put("NFLX", "Netflix, Inc.");
        companies.put("ASML", "ASML Holding N.V.");
        companies.put("TMUS", "T-Mobile US, Inc.");
        companies.put("CSCO", "Cisco Systems, Inc.");
        companies.put("PEP", "PepsiCo, Inc.");
        companies.put("LIN", "Linde plc");
        companies.put("AMD", "Advanced Micro Devices, Inc.");
        companies.put("ADBE", "Adobe Inc.");
        companies.put("AZN", "AstraZeneca PLC");
        companies.put("ISRG", "Intuitive Surgical, Inc.");
        companies.put("INTU", "Intuit Inc.");
        companies.put("QCOM", "QUALCOMM Incorporated");
        companies.put("TXN", "Texas Instruments Incorporated");
        companies.put("BKNG", "Booking Holdings Inc.");
        companies.put("ARM", "Arm Holdings plc");
        companies.put("CMCSA", "Comcast Corporation");
        companies.put("HON", "Honeywell International Inc.");
        companies.put("AMGN", "Amgen Inc.");
        companies.put("PDD", "PDD Holdings Inc.");
        companies.put("AMAT", "Applied Materials, Inc.");
        companies.put("PANW", "Palo Alto Networks, Inc.");
        companies.put("ADP", "Automatic Data Processing, Inc.");
        companies.put("VRTX", "Vertex Pharmaceuticals Incorporated");
        companies.put("GILD", "Gilead Sciences, Inc.");
        companies.put("MU", "Micron Technology, Inc.");
        companies.put("SBUX", "Starbucks Corporation");
        companies.put("ADI", "Analog Devices, Inc.");
        companies.put("MRVL", "Marvell Technology, Inc.");
        companies.put("LRCX", "Lam Research Corporation");
        companies.put("MELI", "MercadoLibre, Inc.");
        companies.put("PYPL", "PayPal Holdings, Inc.");
        companies.put("CRWD", "CrowdStrike Holdings, Inc.");
        companies.put("INTC", "Intel Corporation");
        companies.put("KLAC", "KLA Corporation");
        companies.put("CTAS", "Cintas Corporation");
        companies.put("CDNS", "Cadence Design Systems, Inc.");
        companies.put("MDLZ", "Mondelez International, Inc.");
        companies.put("ABNB", "Airbnb, Inc.");
        companies.put("MAR", "Marriott International, Inc.");
        companies.put("REGN", "Regeneron Pharmaceuticals, Inc.");
        companies.put("SNPS", "Synopsys, Inc.");
        companies.put("FTNT", "Fortinet, Inc.");
        companies.put("CEG", "Constellation Energy Corporation");
        companies.put("ORLY", "O'Reilly Automotive, Inc.");
        companies.put("WDAY", "Workday, Inc.");
        companies.put("DASH", "DoorDash, Inc.");
        companies.put("TEAM", "Atlassian Corporation");
        companies.put("TTD", "The Trade Desk, Inc.");
        companies.put("ADSK", "Autodesk, Inc.");
        companies.put("CSX", "CSX Corporation");
        companies.put("CHTR", "Charter Communications, Inc.");
        companies.put("PCAR", "PACCAR Inc");
        companies.put("CPRT", "Copart, Inc.");
        companies.put("ROP", "Roper Technologies, Inc.");
        companies.put("NXPI", "NXP Semiconductors N.V.");
        companies.put("DDOG", "Datadog, Inc.");
        companies.put("MNST", "Monster Beverage Corporation");
        companies.put("PAYX", "Paychex, Inc.");
        companies.put("ROST", "Ross Stores, Inc.");
        companies.put("AEP", "American Electric Power Company, Inc.");
        companies.put("LULU", "Lululemon Athletica Inc.");
        companies.put("FANG", "Diamondback Energy, Inc.");
        companies.put("KDP", "Keurig Dr Pepper Inc.");
        companies.put("FAST", "Fastenal Company");
        companies.put("ODFL", "Old Dominion Freight Line, Inc.");
        companies.put("BKR", "Baker Hughes Company");
        companies.put("EA", "Electronic Arts Inc.");
        companies.put("VRSK", "Verisk Analytics, Inc.");
        companies.put("CTSH", "Cognizant Technology Solutions Corporation");
        companies.put("XEL", "Xcel Energy Inc.");
        companies.put("KHC", "The Kraft Heinz Company");
        companies.put("GEHC", "GE HealthCare Technologies Inc.");
        companies.put("EXC", "Exelon Corporation");
        companies.put("CCEP", "Coca-Cola Europacific Partners PLC");
        companies.put("IDXX", "IDEXX Laboratories, Inc.");
        companies.put("TTWO", "Take-Two Interactive Software, Inc.");
        companies.put("MCHP", "Microchip Technology Incorporated");
        companies.put("CSGP", "CoStar Group, Inc.");
        companies.put("ZS", "Zscaler, Inc.");
        companies.put("DXCM", "DexCom, Inc.");
        companies.put("ANSS", "ANSYS, Inc.");
        companies.put("WBD", "Warner Bros. Discovery, Inc.");
        companies.put("ON", "ON Semiconductor Corporation");
        companies.put("GFS", "GlobalFoundries Inc.");
        companies.put("CDW", "CDW Corporation");
        companies.put("ILMN", "Illumina, Inc.");
        companies.put("BIIB", "Biogen Inc.");
        companies.put("MDB", "MongoDB, Inc.");
        companies.put("MRNA", "Moderna, Inc.");
        companies.put("DLTR", "Dollar Tree, Inc.");
        companies.put("WBA", "Walgreens Boots Alliance, Inc.");

        return companies;

    }

}
