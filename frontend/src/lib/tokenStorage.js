// Keeps the session (tokens + who is logged in) in localStorage so it survives a page reload.
// Every access is wrapped in try/catch because storage can be unavailable (private mode, SSR).

const STORAGE_KEY = "brm.session";

export function getSession() {
    try {
        const raw = window.localStorage.getItem(STORAGE_KEY);
        return raw ? JSON.parse(raw) : null;
    } catch {
        return null;
    }
}

// Accepts the AuthResponse from the backend: { accessToken, refreshToken, username, role, ... }
export function saveSession({ accessToken, refreshToken, username, role }) {
    try {
        window.localStorage.setItem(
            STORAGE_KEY,
            JSON.stringify({ accessToken, refreshToken, username, role })
        );
    } catch {
        // Storage unavailable: the session will only last until the page is reloaded
    }
}

export function clearSession() {
    try {
        window.localStorage.removeItem(STORAGE_KEY);
    } catch {
        // Nothing to clear
    }
}
