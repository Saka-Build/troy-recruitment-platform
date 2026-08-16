package com.troy.ats.exception;

import org.springframework.http.HttpStatus;

/**
 * Business-rule violation where the caller chooses the status code -
 * e.g. account locked (423), stage transition not allowed (422).
 */
public class ServiceException extends RuntimeException {

    private final HttpStatus status;

    public ServiceException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ServiceException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
