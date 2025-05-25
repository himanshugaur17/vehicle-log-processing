package com.example.vehiclelogprocessing.exception;

public class ExternalServiceException extends Exception {

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
