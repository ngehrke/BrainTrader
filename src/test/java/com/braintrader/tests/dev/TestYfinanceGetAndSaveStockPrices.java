package com.braintrader.tests.dev;

import com.braintrader.datamanagement.Yfinance;
import com.braintrader.exceptions.YfinanceException;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestYfinanceGetAndSaveStockPrices {

    @Test
    void testYfinance() throws YfinanceException, InterruptedException {

        Map<String,String> companies = getCryptocurrencies();

        Set<String> symbols = companies.keySet();

        Yfinance yFinance = new Yfinance(System.out::println);

        LocalDate startDate = LocalDate.of(1950, 1, 1);

        for(String symbol : symbols) {

            System.out.println("Getting prices for " + symbol + " (" + companies.get(symbol) + ")");

            yFinance.getAndSaveStockPricesInDatabase(symbol, startDate,LocalDate.now());

            Thread.sleep(5000);

        }

        yFinance.close();

        assertTrue(true);

    }

    public static Map<String, String> getCryptocurrencies() {

        Map<String, String> cryptocurrencies = new HashMap<>();

        cryptocurrencies.put("BTC-USD", "Bitcoin USD");
        cryptocurrencies.put("ETH-USD", "Ethereum USD");
        cryptocurrencies.put("USDT-USD", "Tether USDt USD");
        cryptocurrencies.put("XRP-USD", "XRP USD");
        cryptocurrencies.put("SOL-USD", "Solana USD");
        cryptocurrencies.put("BNB-USD", "BNB USD");
        cryptocurrencies.put("DOGE-USD", "Dogecoin USD");
        cryptocurrencies.put("USDC-USD", "USD Coin USD");
        cryptocurrencies.put("STETH-USD", "Lido Staked ETH USD");
        cryptocurrencies.put("ADA-USD", "Cardano USD");
        cryptocurrencies.put("WTRX-USD", "Wrapped TRON USD");
        cryptocurrencies.put("TRX-USD", "TRON USD");
        cryptocurrencies.put("AVAX-USD", "Avalanche USD");
        cryptocurrencies.put("LINK-USD", "Chainlink USD");
        cryptocurrencies.put("WSTETH-USD", "Lido wstETH USD");
        cryptocurrencies.put("SHIB-USD", "Shiba Inu USD");
        cryptocurrencies.put("TON11419-USD", "Toncoin USD");
        cryptocurrencies.put("WBTC-USD", "Wrapped Bitcoin USD");
        cryptocurrencies.put("DOT-USD", "Polkadot USD");
        cryptocurrencies.put("WETH-USD", "WETH USD");
        cryptocurrencies.put("XLM-USD", "Stellar USD");
        cryptocurrencies.put("SUI20947-USD", "Sui USD");
        cryptocurrencies.put("HBAR-USD", "Hedera USD");
        cryptocurrencies.put("BCH-USD", "Bitcoin Cash USD");
        cryptocurrencies.put("UNI7083-USD", "Uniswap USD");
        cryptocurrencies.put("PEPE24478-USD", "Pepe USD");
        cryptocurrencies.put("LTC-USD", "Litecoin USD");
        cryptocurrencies.put("LEO-USD", "UNUS SED LEO USD");
        cryptocurrencies.put("NEAR-USD", "NEAR Protocol USD");
        cryptocurrencies.put("APT21794-USD", "Aptos USD");
        cryptocurrencies.put("WEETH-USD", "Wrapped eETH USD");
        cryptocurrencies.put("WBETH-USD", "Wrapped Beacon ETH USD");
        cryptocurrencies.put("BTCB-USD", "Bitcoin BEP2 USD");
        cryptocurrencies.put("ICP-USD", "Internet Computer USD");
        cryptocurrencies.put("USDE29470-USD", "Ethena USDe USD");
        cryptocurrencies.put("HYPE32196-USD", "Hyperliquid USD");
        cryptocurrencies.put("AAVE-USD", "Aave USD");
        cryptocurrencies.put("DAI-USD", "Dai USD");
        cryptocurrencies.put("USDS33039-USD", "USDS USD");
        cryptocurrencies.put("POL28321-USD", "POL (ex-MATIC) USD");
        cryptocurrencies.put("ETC-USD", "Ethereum Classic USD");
        cryptocurrencies.put("CRO-USD", "Cronos USD");
        cryptocurrencies.put("RENDER-USD", "Render USD");
        cryptocurrencies.put("VET-USD", "VeChain USD");
        cryptocurrencies.put("SUSDE-USD", "Ethena Staked USDe USD");
        cryptocurrencies.put("FET-USD", "Artificial Superintelligence Alliance USD");
        cryptocurrencies.put("BGB-USD", "Bitget Token USD");
        cryptocurrencies.put("TAO22974-USD", "Bittensor USD");
        cryptocurrencies.put("MNT27075-USD", "Mantle USD");
        cryptocurrencies.put("ARB11841-USD", "Arbitrum USD");
        cryptocurrencies.put("FIL-USD", "Filecoin USD");
        cryptocurrencies.put("XMR-USD", "Monero USD");
        cryptocurrencies.put("KAS-USD", "Kaspa USD");
        cryptocurrencies.put("OM-USD", "MANTRA USD");
        cryptocurrencies.put("FTM-USD", "Fantom USD");
        cryptocurrencies.put("ATOM-USD", "Cosmos USD");
        cryptocurrencies.put("ALGO-USD", "Algorand USD");
        cryptocurrencies.put("STX4847-USD", "Stacks USD");
        cryptocurrencies.put("ENA-USD", "Ethena USD");
        cryptocurrencies.put("OKB-USD", "OKB USD");
        cryptocurrencies.put("JITOSOL-USD", "Jito Staked SOL USD");
        cryptocurrencies.put("TIA22861-USD", "Celestia USD");
        cryptocurrencies.put("OP-USD", "Optimism USD");
        cryptocurrencies.put("IMX10603-USD", "Immutable USD");
        cryptocurrencies.put("WIF-USD", "dogwifhat USD");
        cryptocurrencies.put("BONK-USD", "Bonk USD");
        cryptocurrencies.put("INJ-USD", "Injective USD");
        cryptocurrencies.put("THETA-USD", "Theta Network USD");
        cryptocurrencies.put("GRT6719-USD", "The Graph USD");
        cryptocurrencies.put("ONDO-USD", "Ondo USD");
        cryptocurrencies.put("VIRTUAL-USD", "Virtuals Protocol USD");
        cryptocurrencies.put("SEI-USD", "Sei USD");
        cryptocurrencies.put("WLD-USD", "Worldcoin USD");
        cryptocurrencies.put("FLOKI-USD", "FLOKI USD");
        cryptocurrencies.put("JASMY-USD", "JasmyCoin USD");
        cryptocurrencies.put("RUNE-USD", "THORChain USD");
        cryptocurrencies.put("FDUSD-USD", "First Digital USD USD");
        cryptocurrencies.put("LDO-USD", "Lido DAO USD");
        cryptocurrencies.put("CBBTC32994-USD", "Coinbase Wrapped BTC USD");
        cryptocurrencies.put("RETH-USD", "Rocket Pool ETH USD");
        cryptocurrencies.put("RSETH-USD", "Kelp DAO Restaked ETH USD");
        cryptocurrencies.put("GALA-USD", "Gala USD");
        cryptocurrencies.put("METH29035-USD", "Mantle Staked Ether USD");
        cryptocurrencies.put("SAND-USD", "The Sandbox USD");
        cryptocurrencies.put("KAIA-USD", "Kaia USD");
        cryptocurrencies.put("MKR-USD", "Maker USD");
        cryptocurrencies.put("BEAM28298-USD", "Beam USD");
        cryptocurrencies.put("FLR-USD", "Flare USD");
        cryptocurrencies.put("BRETT29743-USD", "Brett (Based) USD");
        cryptocurrencies.put("EOS-USD", "EOS USD");
        cryptocurrencies.put("HNT-USD", "Helium USD");
        cryptocurrencies.put("QNT-USD", "Quant USD");
        cryptocurrencies.put("KCS-USD", "KuCoin Token USD");
        cryptocurrencies.put("PYTH-USD", "Pyth Network USD");
        cryptocurrencies.put("RAY-USD", "Raydium USD");
        cryptocurrencies.put("ENS-USD", "Ethereum Name Service USD");
        cryptocurrencies.put("EZETH-USD", "Renzo Restaked ETH USD");
        cryptocurrencies.put("BBTC31369-USD", "BounceBit BTC USD");
        cryptocurrencies.put("JUP29210-USD", "Jupiter USD");
        cryptocurrencies.put("FLOW-USD", "Flow USD");

        return cryptocurrencies;

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
