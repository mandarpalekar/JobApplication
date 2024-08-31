package com.springsynergy.jobapp.exception;

public class JobAlreadyExistsException extends RuntimeException {
    public JobAlreadyExistsException(String message) {
        super(message);
    }
}
