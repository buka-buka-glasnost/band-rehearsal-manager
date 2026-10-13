package com.bandrehearsal.bandrehearsalmanager.exceptions;

/** Thrown when a request body is well-formed but refers to data that does not exist (HTTP 400). */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
