package com.example.codeitearthquake.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ApiServiceException extends RuntimeException {
    public ApiServiceException(String message) {
        super(message);
    }

    public ApiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
