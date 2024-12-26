package com.braintrader.exceptions;

public class DataModelException extends Exception {

    public DataModelException(String message) {
        super(message);
    }

    public DataModelException(String message, Throwable cause) {
        super(message, cause);
    }

}
