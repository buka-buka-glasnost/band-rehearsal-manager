"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";
import { useAuth } from "@/lib/auth";
import { SECTIONS } from "@/lib/sections";

const PUBLIC_PATHS = ["/login", "/register"];

// Wraps every page: sends logged-out visitors to /login, and shows the
// masthead with the navigation and the current user once someone is logged in.
export default function AppShell({ children }) {
    const { user, ready, logout } = useAuth();
    const pathname = usePathname();
    const router = useRouter();
    const isPublic = PUBLIC_PATHS.includes(pathname);

    useEffect(() => {
        if (!ready) return;
        if (!user && !isPublic) router.replace("/login");
        if (user && isPublic) router.replace("/");
    }, [ready, user, isPublic, router]);

    if (!ready) return null;

    if (isPublic) {
        return user ? null : <main className="auth-page">{children}</main>;
    }
    if (!user) return null;

    async function handleLogout() {
        await logout();
        router.replace("/login");
    }

    return (
        <>
            <header className="masthead">
                <div className="masthead-row">
                    <Link href="/" className="brand">Band Rehearsal Manager</Link>
                    <div className="account">
                        <span className="account-name">{user.username}</span>
                        <span className="sticker" data-role={user.role}>
                            {user.role === "ADMIN" ? "Admin" : "Read-only"}
                        </span>
                        <button type="button" className="button small" onClick={handleLogout}>
                            Log out
                        </button>
                    </div>
                </div>

                <nav className="sections" aria-label="Sections">
                    {SECTIONS.map((section) => (
                        <Link
                            key={section.href}
                            href={section.href}
                            aria-current={pathname.startsWith(section.href) ? "page" : undefined}
                        >
                            {section.label}
                        </Link>
                    ))}
                </nav>
            </header>
            <main className="page">{children}</main>
        </>
    );
}
