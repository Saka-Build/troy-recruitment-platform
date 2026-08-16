package com.troy.ats.exception;

/** CV/photo upload or read failed for an infrastructure reason. Maps to 500. */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
