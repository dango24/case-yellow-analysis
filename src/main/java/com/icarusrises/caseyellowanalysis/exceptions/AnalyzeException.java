package com.icarusrises.caseyellowanalysis.exceptions;

public class AnalyzeException extends RuntimeException {

    public AnalyzeException(String message) {
        super(message);
    }

    public AnalyzeException(String message, Throwable cause) {
        super(message, cause);
    }
}
