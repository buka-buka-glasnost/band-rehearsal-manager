"use client";

import { useCallback, useEffect, useState } from "react";
import api, { errorMessage } from "./api";

/**
 * Loads one backend resource (e.g. "band") and offers the CRUD calls for it:
 *   GET    /{resource}/get-list
 *   POST   /{resource}/create
 *   PUT    /{resource}/update
 *   DELETE /{resource}/delete/{id}
 */
export function useResource(resource) {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const reload = useCallback(async () => {
        try {
            const response = await api.get(`/${resource}/get-list`);
            setItems(response.data);
        } catch (e) {
            setError(errorMessage(e));
        } finally {
            setLoading(false);
        }
    }, [resource]);

    useEffect(() => {
        reload();
    }, [reload]);

    // Creates the entity when it has no id, otherwise updates it. Returns true on success.
    const save = useCallback(async (entity) => {
        setError(null);
        try {
            if (entity.id == null) {
                await api.post(`/${resource}/create`, entity);
            } else {
                await api.put(`/${resource}/update`, entity);
            }
            await reload();
            return true;
        } catch (e) {
            setError(errorMessage(e));
            return false;
        }
    }, [resource, reload]);

    const remove = useCallback(async (id) => {
        setError(null);
        try {
            await api.delete(`/${resource}/delete/${id}`);
            await reload();
            return true;
        } catch (e) {
            setError(errorMessage(e));
            return false;
        }
    }, [resource, reload]);

    return { items, loading, error, setError, save, remove, reload };
}
