package com.bandrehearsal.bandrehearsalmanager.exceptions;

/** Thrown when a refresh token is unknown, expired or revoked (HTTP 401). */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
