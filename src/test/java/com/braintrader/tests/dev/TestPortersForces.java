package com.braintrader.tests.dev;

import com.braintrader.porter.FiveForcesAnalysis;
import com.braintrader.porter.Force;
import com.braintrader.porter.SecurityInForce;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestPortersForces {

    @Test
    void testPortersForces() {

        FiveForcesAnalysis fiveForcesAnalysis = new FiveForcesAnalysis();

        fiveForcesAnalysis.setCompanyName("Apple");
        fiveForcesAnalysis.setTickerSymbol("AAPL");

        List<Force> forceList = new ArrayList<>();
        fiveForcesAnalysis.setForceList(forceList);

        Force force1 = new Force();
        forceList.add(force1);
        force1.setForceName("Rivalität unter den bestehenden Wettbewerbern (intensity of competitive rivalry)");
        List<SecurityInForce> securityInForceList1 = new ArrayList<>();
        force1.setSecurityInForceList(securityInForceList1);
        SecurityInForce securityInForce1 = new SecurityInForce();
        securityInForceList1.add(securityInForce1);
        securityInForce1.setSecurityName("Company1");
        securityInForce1.setIsin("ISIN1");
        securityInForce1.setTickerSymbol("TICKER1");
        securityInForce1.setReasonForBeingInForce("The reason for this company being... ");
        SecurityInForce securityInForce2 = new SecurityInForce();
        securityInForceList1.add(securityInForce2);
        securityInForce2.setSecurityName("Index2");
        securityInForce2.setIsin("ISIN2");
        securityInForce2.setTickerSymbol("TICKER2");
        securityInForce2.setReasonForBeingInForce("The reason for this index being... ");
        SecurityInForce securityInForce3 = new SecurityInForce();
        securityInForceList1.add(securityInForce3);
        securityInForce3.setSecurityName("etf3");
        securityInForce3.setIsin("ISIN3");
        securityInForce3.setTickerSymbol("TICKER3");
        securityInForce3.setReasonForBeingInForce("The reason for this etf being... ");

        Force force2 = new Force();
        forceList.add(force2);
        force2.setForceName("Bedrohung durch neue Anbieter (threat of entry)");
        List<SecurityInForce> securityInForceList2 = new ArrayList<>();
        force2.setSecurityInForceList(securityInForceList2);
        SecurityInForce securityInForce1a = new SecurityInForce();
        securityInForceList2.add(securityInForce1a);
        securityInForce1a.setSecurityName("Company1a");
        securityInForce1a.setIsin("ISIN1a");
        securityInForce1a.setTickerSymbol("TICKER1a");
        securityInForce1a.setReasonForBeingInForce("The reason for this company being... ");

        Force force3 = new Force();
        forceList.add(force3);
        force3.setForceName("Verhandlungsstärke der Lieferanten (bargaining power of suppliers)");
        List<SecurityInForce> securityInForceList3 = new ArrayList<>();
        force3.setSecurityInForceList(securityInForceList3);
        SecurityInForce securityInForce1b = new SecurityInForce();
        securityInForceList3.add(securityInForce1b);
        securityInForce1b.setSecurityName("Company1b");
        securityInForce1b.setIsin("ISIN1b");
        securityInForce1b.setTickerSymbol("TICKER1b");
        securityInForce1b.setReasonForBeingInForce("The reason for this company being... ");

        Force force4 = new Force();
        forceList.add(force4);
        force4.setForceName("Verhandlungsstärke der Abnehmer (bargaining power of buyers)");
        List<SecurityInForce> securityInForceList4 = new ArrayList<>();
        force4.setSecurityInForceList(securityInForceList4);
        SecurityInForce securityInForce1c = new SecurityInForce();
        securityInForceList4.add(securityInForce1c);
        securityInForce1c.setSecurityName("Company1c");
        securityInForce1c.setIsin("ISIN1c");
        securityInForce1c.setTickerSymbol("TICKER1c");
        securityInForce1c.setReasonForBeingInForce("The reason for this company being... ");

        Force force5 = new Force();
        forceList.add(force5);
        force5.setForceName("Bedrohung durch Ersatzprodukte (threat of substitutes)");
        List<SecurityInForce> securityInForceList5 = new ArrayList<>();
        force5.setSecurityInForceList(securityInForceList5);
        SecurityInForce securityInForce1d = new SecurityInForce();
        securityInForceList5.add(securityInForce1d);
        securityInForce1d.setSecurityName("Company1d");
        securityInForce1d.setIsin("ISIN1d");
        securityInForce1d.setTickerSymbol("TICKER1d");
        securityInForce1d.setReasonForBeingInForce("The reason for this company being... ");

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        String analysisAsJson = gson.toJson(fiveForcesAnalysis);

        System.out.println(analysisAsJson);

        /*
        Bitte führe eine spezifische Analyse für ein Unternehmen durch. Leitgedanke dabei ist das Porter Five Forces Model.
        Für jede Force oder Kraft sollen börsennotierte Wertpapiere zugeordnet werden, die besonders gut für das analysierte Unternehmen in dieser Kraft als spezifischer Einfluss reinpasst.
        Es können ein oder mehrere Wertpapiere sein, aber mindestens eines pro Kraft.
        Wertpapiere können dabei sein: Firmen und Konzerne, Indizies oder auch Fonds wie ETF.
        Unten ist ein Beispiel für das Outputformat in JSON, in dem die Firma Apple als Beispiel analysiert wird.

        Die von dir zu analysierende Firma ist: Zalando

        Gehe dabei wie folgt vor.
        * Denke intensiv über die Kräfte und passende Wertpapiere nach
        * Recherchiere auch nochmal im Internet, was gut zusammenpasst
        */

        assertTrue(true);

    }


}
