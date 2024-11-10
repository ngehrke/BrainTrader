package com.braintrader.tests.dev;

import com.braintrader.BrainConnectionHandler;
import com.braintrader.BrainLogger;
import com.braintrader.exceptions.BrainConnectionHandlerException;
import com.braintrader.exceptions.GeneralBrainException;
import com.ib.controller.ApiController;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

class TestMain {

    @Test
    void testMain() throws InterruptedException, BrainConnectionHandlerException, GeneralBrainException {

        System.out.println("Interactive Broker API");

        Consumer<String> systemOut = TestMain::print;

        BrainLogger brainLoggerIn = new BrainLogger(systemOut);
        BrainLogger brainLoggerOut = new BrainLogger(systemOut);

        BrainConnectionHandler brainConnectionHandler = new BrainConnectionHandler(systemOut,systemOut, systemOut);
        ApiController apiController = new ApiController(brainConnectionHandler,brainLoggerIn,brainLoggerOut);

        apiController.connect("127.0.0.1", 7497, 0, "+PACEAPI");

        while(!brainConnectionHandler.isConnected()) {
            Thread.sleep(1000);
        }

        System.out.println("Connected");

        System.out.println("Accountlist: "+brainConnectionHandler.getAccountList());

        apiController.accountSummary(0,"DUE088961",null,null,null);



        apiController.disconnect();
        System.out.println("Disconnected");

        System.out.println("finished");

    }

    private static void print(String str) {
        System.out.println(str.replaceAll("[^\\x20-\\x7E]", ""));
    }


}
