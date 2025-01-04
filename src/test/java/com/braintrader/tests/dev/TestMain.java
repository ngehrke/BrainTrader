package com.braintrader.tests.dev;

import com.braintrader.exceptions.BrainHistoricalDataHandlerException;
import com.braintrader.handler.BrainConnectionHandler;
import com.braintrader.handler.BrainContractDetailsHandler;
import com.braintrader.BrainLogger;
import com.braintrader.exceptions.BrainConnectionHandlerException;
import com.braintrader.exceptions.GeneralBrainException;
import com.braintrader.handler.ContractTimeSeries;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types;
import com.ib.contracts.StkContract;
import com.ib.controller.ApiController;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

class TestMain {

    @Test
    void testMain() throws InterruptedException, BrainConnectionHandlerException, GeneralBrainException, BrainHistoricalDataHandlerException {

        System.out.println("Interactive Broker API");

        Consumer<String> systemOut = TestMain::print;

        BrainLogger brainLoggerIn = new BrainLogger(systemOut);
        BrainLogger brainLoggerOut = new BrainLogger(systemOut);

        BrainConnectionHandler brainConnectionHandler = new BrainConnectionHandler(systemOut,systemOut, systemOut);
        ApiController apiController = new ApiController(brainConnectionHandler,brainLoggerIn,brainLoggerOut);

        apiController.connect("", 7497, 0, "+PACEAPI");

        while(!brainConnectionHandler.isConnected()) {
            Thread.sleep(1000);
        }

        System.out.println("Connected");
        System.out.println("Accountlist: "+brainConnectionHandler.getAccountList());

        Thread.sleep(1000);

        apiController.accountSummary(0,"DUE088961",null,null,null);

        Contract contract = new StkContract("AAPL");

        // Place an order

        Order order = new Order();
        order.clientId(0);
        order.account("DUE088961");
        order.totalQuantity(Decimal.get(50));
        order.action(Types.Action.BUY);
        order.orderType(OrderType.MKT);

        apiController.placeOrModifyOrder(contract, order, null);


        // Anfrage für Vertragsdetails
        BrainContractDetailsHandler brainContractDetailsHandler = new BrainContractDetailsHandler();
        apiController.reqContractDetails(contract, brainContractDetailsHandler);

        // get historical data
        ContractTimeSeries contractTimeSeries = new ContractTimeSeries(apiController, contract);
        contractTimeSeries.requestHistoricalData("20241201 00:00:00", 365, Types.DurationUnit.DAY, Types.BarSize._1_day, Types.WhatToShow.TRADES);

        Thread.sleep(1000);

        System.out.println("Data points retrieved: "+contractTimeSeries.getDataPointList().size());

        for(int i=0; i<contractTimeSeries.getDataPointList().size(); i++) {
            System.out.println(contractTimeSeries.getDataPointList().get(i).toString());
        }

        apiController.disconnect();
        System.out.println("Disconnected");

        System.out.println("finished");

    }

    private static void print(String str) {
        System.out.println(str.replaceAll("[^\\x20-\\x7E]", ""));
    }


}
