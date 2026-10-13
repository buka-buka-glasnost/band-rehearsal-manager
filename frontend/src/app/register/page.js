"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { errorMessage } from "@/lib/api";
import { useAuth } from "@/lib/auth";
import { Notice } from "@/components/ui";

export default function RegisterPage() {
    const { register } = useAuth();
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
            await register(username.trim(), password);
            router.replace("/");
        } catch (e) {
            setError(errorMessage(e));
            setSubmitting(false);
        }
    }

    return (
        <form className="auth-card" onSubmit={handleSubmit}>
            <p className="brand">Band Rehearsal Manager</p>
            <h1>Create an account</h1>
            <p className="read-only">New accounts can view everything. Only an admin can make changes.</p>

            <Notice message={error} />

            <label>
                Username
                <input
                    autoComplete="username"
                    required
                    minLength={3}
                    maxLength={50}
                    pattern="[A-Za-z0-9_.\-]+"
                    title="Letters, digits, '.', '_' and '-'"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
            </label>
            <label>
                Password
                <input
                    type="password"
                    autoComplete="new-password"
                    required
                    minLength={6}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <span className="field-help">At least 6 characters</span>
            </label>

            <button type="submit" className="button primary" disabled={submitting}>
                {submitting ? "Creating account…" : "Create account"}
            </button>

            <p className="auth-switch">
                Already have an account? <Link href="/login">Log in</Link>
            </p>
        </form>
    );
}
