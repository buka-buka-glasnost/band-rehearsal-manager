package com.bandrehearsal.bandrehearsalmanager.exceptions;

/** Thrown when a resource addressed by id does not exist (HTTP 404). */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
