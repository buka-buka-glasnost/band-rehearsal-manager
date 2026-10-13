"use client";

import Link from "next/link";
import { useAuth } from "@/lib/auth";
import { formatDate, formatDateLong, formatDuration, todayIso } from "@/lib/format";
import { useResource } from "@/lib/useResource";
import { Notice, RisoHeading } from "@/components/ui";

export default function HomePage() {
    const { isAdmin } = useAuth();
    const rehearsals = useResource("rehearsal");

    const today = todayIso();
    const upcoming = rehearsals.items
        .filter((rehearsal) => rehearsal.date >= today)
        .sort((a, b) => a.date.localeCompare(b.date));
    const [next, ...later] = upcoming;

    if (rehearsals.loading) return null;

    return (
        <>
            <Notice message={rehearsals.error} onDismiss={() => rehearsals.setError(null)} />

            {next ? (
                // The next rehearsal, set like a gig flyer
                <section className="flyer" aria-labelledby="next-band">
                    <p className="scrawl">Next up</p>
                    <RisoHeading id="next-band">{next.band?.name ?? "Unknown band"}</RisoHeading>
                    <p className="flyer-when">{formatDateLong(next.date)}</p>
                    <p className="flyer-where">
                        <strong>{next.rehearsalRoom?.name}</strong>, {next.rehearsalRoom?.address}.
                        {" "}Booked for {formatDuration(next.durationMinutes)}.
                    </p>
                </section>
            ) : (
                <section className="flyer">
                    <RisoHeading>Nothing booked</RisoHeading>
                    <p className="flyer-where">
                        No rehearsals are scheduled from today on.
                        {isAdmin && <> Schedule one on the <Link href="/rehearsals">Rehearsals</Link> page.</>}
                    </p>
                </section>
            )}

            {later.length > 0 && (
                <>
                    <h2 className="section-title">After that</h2>
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
                                {later.map((rehearsal) => (
                                    <tr key={rehearsal.id}>
                                        <td className="nowrap">{formatDate(rehearsal.date)}</td>
                                        <td className="lead-cell">{rehearsal.band?.name}</td>
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
                </>
            )}
        </>
    );
}
