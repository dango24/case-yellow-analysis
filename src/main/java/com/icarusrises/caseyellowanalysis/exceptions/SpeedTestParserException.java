package com.icarusrises.caseyellowanalysis.exceptions;

public class SpeedTestParserException extends RuntimeException {

    public SpeedTestParserException(String message) {
        super(message);
    }

    public SpeedTestParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
