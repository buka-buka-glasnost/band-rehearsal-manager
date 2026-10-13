import axios from "axios";
import { clearSession, getSession, saveSession } from "./tokenStorage";

export const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

// Used by every page. Adds the access token and refreshes it automatically (see below).
const api = axios.create({ baseURL: API_URL });

// Used only for /auth/* calls, so the interceptors below never run for them
// (a failed refresh must not trigger another refresh).
export const authApi = axios.create({ baseURL: API_URL });

// --- Request: attach "Authorization: Bearer <access token>" ------------------
api.interceptors.request.use((config) => {
    const session = getSession();
    if (session?.accessToken) {
        config.headers.Authorization = `Bearer ${session.accessToken}`;
    }
    return config;
});

// --- Response: on 401, refresh the token once and retry the request ---------
//
// The access token lives 15 minutes. When it expires the backend answers 401,
// we exchange the refresh token for a new pair at /auth/refresh and repeat the
// original request, so the user never notices. If several requests fail at the
// same time, they all wait for the same refresh call instead of starting their own
// (the backend accepts each refresh token only once).

let refreshInFlight = null;

function refreshTokens() {
    if (!refreshInFlight) {
        const refreshToken = getSession()?.refreshToken;
        refreshInFlight = (
            refreshToken
                ? authApi.post("/auth/refresh", { refreshToken })
                : Promise.reject(new Error("No refresh token"))
        )
            .then((response) => {
                saveSession(response.data);
                return response.data;
            })
            .finally(() => {
                refreshInFlight = null;
            });
    }
    return refreshInFlight;
}

// The auth context subscribes here to hear when the session cannot be renewed
const sessionExpiredListeners = new Set();

export function onSessionExpired(listener) {
    sessionExpiredListeners.add(listener);
    return () => sessionExpiredListeners.delete(listener);
}

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const original = error.config;
        const isUnauthorized = error.response?.status === 401;

        if (!isUnauthorized || !original || original._retried) {
            return Promise.reject(error);
        }
        original._retried = true;

        try {
            await refreshTokens();
            // The request interceptor adds the new access token
            return api(original);
        } catch (refreshError) {
            // The refresh token was rejected (expired, revoked or missing): end the session.
            // If the server could not be reached at all, keep the session and just fail.
            const serverUnreachable = refreshError.isAxiosError && !refreshError.response;
            if (!serverUnreachable) {
                clearSession();
                sessionExpiredListeners.forEach((listener) => listener());
            }
            return Promise.reject(error);
        }
    }
);

export default api;

// Turns an axios error into a sentence for the user. The backend sends
// RFC 9457 problem details: { status, detail, errors: { field: message } }.
export function errorMessage(error) {
    const response = error?.response;
    if (!response) {
        return `Cannot reach the server at ${API_URL}. Check that the backend is running.`;
    }
    const data = response.data;
    if (data?.errors && Object.keys(data.errors).length > 0) {
        return Object.values(data.errors).join(". ") + ".";
    }
    if (data?.detail) {
        return data.detail;
    }
    return `The request failed with status ${response.status}.`;
}
