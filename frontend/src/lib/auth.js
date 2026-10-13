"use client";

import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { authApi, onSessionExpired } from "./api";
import { clearSession, getSession, saveSession } from "./tokenStorage";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    // { username, role } of the logged-in user, or null
    const [user, setUser] = useState(null);
    // false until the stored session has been read, so pages do not redirect too early
    const [ready, setReady] = useState(false);
    // true when the user was logged out because the refresh token stopped working
    const [sessionExpired, setSessionExpired] = useState(false);

    useEffect(() => {
        const session = getSession();
        if (session?.accessToken) {
            setUser({ username: session.username, role: session.role });
        }
        setReady(true);

        return onSessionExpired(() => {
            setUser(null);
            setSessionExpired(true);
        });
    }, []);

    const startSession = useCallback((authResponse) => {
        saveSession(authResponse);
        setUser({ username: authResponse.username, role: authResponse.role });
        setSessionExpired(false);
    }, []);

    const login = useCallback(async (username, password) => {
        const response = await authApi.post("/auth/login", { username, password });
        startSession(response.data);
    }, [startSession]);

    const register = useCallback(async (username, password) => {
        const response = await authApi.post("/auth/register", { username, password });
        startSession(response.data);
    }, [startSession]);

    const logout = useCallback(async () => {
        const refreshToken = getSession()?.refreshToken;
        clearSession();
        setUser(null);
        if (refreshToken) {
            // Revoke the refresh token on the server; logging out locally works even if this fails
            await authApi.post("/auth/logout", { refreshToken }).catch(() => {});
        }
    }, []);

    const value = useMemo(() => ({
        user,
        ready,
        sessionExpired,
        isAdmin: user?.role === "ADMIN",
        login,
        register,
        logout,
    }), [user, ready, sessionExpired, login, register, logout]);

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used inside <AuthProvider>");
    }
    return context;
}
