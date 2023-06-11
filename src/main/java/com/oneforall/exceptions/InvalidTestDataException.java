package com.oneforall.exceptions;

public class InvalidTestDataException
        extends RuntimeException {
    public InvalidTestDataException(String message) {
        super(message);
    }

    public InvalidTestDataException(String message, Exception ex) {
        super(message, ex);
    }
}
