"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { errorMessage } from "@/lib/api";
import { useAuth } from "@/lib/auth";
import { Notice } from "@/components/ui";

export default function LoginPage() {
    const { login, sessionExpired } = useAuth();
    const router = useRouter();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const [submitting, setSubmitting] = useState(false);

    async function handleSubmit(event) {
        event.preventDefault();
        setError(null);
        setSubmitting(true);
        try {
            await login(username.trim(), password);
            router.replace("/");
        } catch (e) {
            setError(errorMessage(e));
            setSubmitting(false);
        }
    }

    return (
        <form className="auth-card" onSubmit={handleSubmit}>
            <p className="brand">Band Rehearsal Manager</p>
            <h1>Log in</h1>

            {sessionExpired && !error && (
                <p className="read-only">Your session has ended. Log in again to continue.</p>
            )}
            <Notice message={error} />

            <label>
                Username
                <input
                    autoComplete="username"
                    required
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
            </label>
            <label>
                Password
                <input
                    type="password"
                    autoComplete="current-password"
                    required
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </label>

            <button type="submit" className="button primary" disabled={submitting}>
                {submitting ? "Logging in…" : "Log in"}
            </button>

            <p className="auth-switch">
                No account yet? <Link href="/register">Create one</Link>
            </p>
            <p className="hint">
                A fresh install has two demo accounts: <strong>admin</strong> / admin123 can make changes,
                {" "}<strong>user</strong> / user123 can only view.
            </p>
        </form>
    );
}
