import "@fontsource-variable/archivo/wdth.css";
import "@fontsource/permanent-marker/400.css";
import AppShell from "@/components/AppShell";
import { AuthProvider } from "@/lib/auth";
import "./globals.css";

export const metadata = {
    title: "Band Rehearsal Manager",
    description: "Schedule rehearsals for your label's bands, book rooms and track who showed up.",
};

export default function RootLayout({ children }) {
    return (
        <html lang="en">
            <body>
                <AuthProvider>
                    <AppShell>{children}</AppShell>
                </AuthProvider>
            </body>
        </html>
    );
}
