package com.troy.ats.exception;

/** Thrown when creating/updating would violate a uniqueness rule. Maps to 409. */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String field, Object value) {
        super(field + " already exists: " + value);
    }
}
