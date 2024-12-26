package com.braintrader.exceptions;

public class IndicatorException extends Exception {

    public IndicatorException(String message) {
        super(message);
    }

    public IndicatorException(String message, Throwable cause) {
        super(message, cause);
    }

}
