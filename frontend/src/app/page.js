"use client";

import Link from "next/link";
import { useAuth } from "@/lib/auth";
import { countLabel, formatDate, formatDuration, todayIso } from "@/lib/format";
import { useResource } from "@/lib/useResource";
import { EmptyState, Notice, PageHeader } from "@/components/ui";

export default function HomePage() {
    const { user, isAdmin } = useAuth();
    const rehearsals = useResource("rehearsal");

    const today = todayIso();
    const upcoming = rehearsals.items
        .filter((rehearsal) => rehearsal.date >= today)
        .sort((a, b) => a.date.localeCompare(b.date));

    return (
        <>
            <PageHeader
                channel="rehearsals"
                title="Coming up"
                summary={rehearsals.loading ? null : `${countLabel(upcoming.length, "rehearsal")} scheduled from today`}
            />
            <p className="lead">
                Welcome back, {user.username}.{" "}
                {isAdmin
                    ? <>Schedule new sessions on the <Link href="/rehearsals">Rehearsals</Link> page.</>
                    : "Here is what your bands have booked."}
            </p>

            <Notice message={rehearsals.error} onDismiss={() => rehearsals.setError(null)} />

            <div className="table-wrap">
                <table className="data">
                    <thead>
                        <tr>
                            <th scope="col">Date</th>
                            <th scope="col">Band</th>
                            <th scope="col">Room</th>
                            <th scope="col" className="num">Length</th>
                        </tr>
                    </thead>
                    <tbody>
                        {upcoming.map((rehearsal) => (
                            <tr key={rehearsal.id}>
                                <td className="nowrap">{formatDate(rehearsal.date)}</td>
                                <td>{rehearsal.band?.name}</td>
                                <td>
                                    {rehearsal.rehearsalRoom?.name}
                                    <span className="sub">{rehearsal.rehearsalRoom?.address}</span>
                                </td>
                                <td className="num nowrap">{formatDuration(rehearsal.durationMinutes)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {!rehearsals.loading && upcoming.length === 0 && (
                <EmptyState adminHint="Add one on the Rehearsals page.">Nothing is scheduled from today on.</EmptyState>
            )}
        </>
    );
}
