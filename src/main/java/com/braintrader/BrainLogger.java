package com.braintrader;

import com.braintrader.exceptions.GeneralBrainException;
import com.ib.controller.ApiConnection;

import java.util.function.Consumer;

public class BrainLogger implements ApiConnection.ILogger {

    private Consumer<String> loggerConsumer=null;

    public BrainLogger(Consumer<String> loggerConsumer) throws GeneralBrainException {

        if (loggerConsumer==null) {
            throw new GeneralBrainException("loggerConsumer is null");
        }

        this.loggerConsumer=loggerConsumer;

    }

    @Override
    public void log(String s) {
        loggerConsumer.accept("logger: "+s);
    }

}
