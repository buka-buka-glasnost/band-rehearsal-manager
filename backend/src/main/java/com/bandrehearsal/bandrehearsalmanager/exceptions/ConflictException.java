package com.bandrehearsal.bandrehearsalmanager.exceptions;

/** Thrown when an operation would break data consistency, e.g. a duplicate username or deleting a referenced record (HTTP 409). */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
