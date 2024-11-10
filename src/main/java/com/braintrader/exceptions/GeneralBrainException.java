package com.braintrader.exceptions;

public class GeneralBrainException extends Exception {

    public GeneralBrainException(String message) {
        super(message);
    }

    public GeneralBrainException(String message, Throwable cause) {
        super(message, cause);
    }

}
