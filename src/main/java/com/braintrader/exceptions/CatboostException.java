package com.braintrader.exceptions;

public class CatboostException extends Exception {

    public CatboostException(String message) {
        super(message);
    }

    public CatboostException(String message, Throwable cause) {
        super(message, cause);
    }

}
