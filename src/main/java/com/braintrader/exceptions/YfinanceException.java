package com.braintrader.exceptions;

public class YfinanceException extends Exception {

    public YfinanceException(String message) {
        super(message);
    }

    public YfinanceException(String message, Throwable cause) {
        super(message, cause);
    }

}
