package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.exceptions.BadRequestException;

/** Small helpers shared by the CRUD services. */
final class ServiceSupport {

    private ServiceSupport() {
    }

    /** The update endpoints take the id in the body, so it must be present. */
    static Long requireId(Long id) {
        if (id == null) {
            throw new BadRequestException("Id is required for an update");
        }
        return id;
    }

    /** Returns the id of a referenced entity sent in a request body, e.g. { "band": { "id": 1 } }. */
    static Long referenceId(Long id, String field) {
        if (id == null) {
            throw new BadRequestException("Field '" + field + "' must contain an id");
        }
        return id;
    }
}
