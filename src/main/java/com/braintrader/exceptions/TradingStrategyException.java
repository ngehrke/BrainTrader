package com.braintrader.exceptions;

public class TradingStrategyException extends Exception {

    public TradingStrategyException(String message) {

        super(message);

    }

    public TradingStrategyException(String message, Throwable cause) {

        super(message, cause);

    }

}
