package com.braintrader.exceptions;

public class BrainConnectionHandlerException extends Exception {

        public BrainConnectionHandlerException(String message) {
            super(message);
        }

        public BrainConnectionHandlerException(String message, Throwable cause) {
            super(message, cause);
        }

}
