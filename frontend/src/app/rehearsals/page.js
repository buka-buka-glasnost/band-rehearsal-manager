"use client";

import { useState } from "react";
import { useAuth } from "@/lib/auth";
import { countLabel, formatDate, formatDuration, todayIso, toNumberOrNull } from "@/lib/format";
import { useResource } from "@/lib/useResource";
import { EmptyState, FormActions, Notice, PageHeader, ReadOnlyNote, RowActions } from "@/components/ui";

const EMPTY_FORM = { id: null, date: "", durationMinutes: "", bandId: "", roomId: "" };

export default function RehearsalsPage() {
    const { isAdmin } = useAuth();
    const rehearsals = useResource("rehearsal");
    const bands = useResource("band");
    const rooms = useResource("rehearsal-room");
    const [form, setForm] = useState(EMPTY_FORM);
    const editing = form.id !== null;

    const field = (name) => (event) => setForm({ ...form, [name]: event.target.value });

    const today = todayIso();
    const sorted = [...rehearsals.items].sort((a, b) => a.date.localeCompare(b.date));

    async function handleSubmit(event) {
        event.preventDefault();
        const saved = await rehearsals.save({
            id: form.id,
            date: form.date,
            durationMinutes: toNumberOrNull(form.durationMinutes),
            // OneToMany from Band and from RehearsalRoom: send only the ids
            band: { id: Number(form.bandId) },
            rehearsalRoom: { id: Number(form.roomId) },
        });
        if (saved) setForm(EMPTY_FORM);
    }

    function startEditing(rehearsal) {
        setForm({
            id: rehearsal.id,
            date: rehearsal.date ?? "",
            durationMinutes: rehearsal.durationMinutes ?? "",
            bandId: String(rehearsal.band?.id ?? ""),
            roomId: String(rehearsal.rehearsalRoom?.id ?? ""),
        });
    }

    async function handleDelete(rehearsal) {
        const name = `${rehearsal.band?.name ?? "the"} rehearsal on ${formatDate(rehearsal.date)}`;
        if (!window.confirm(`Delete ${name}?`)) return;
        if (await rehearsals.remove(rehearsal.id) && form.id === rehearsal.id) setForm(EMPTY_FORM);
    }

    const loadError = rehearsals.error ?? bands.error ?? rooms.error;

    return (
        <>
            <PageHeader title="Rehearsals" note={countLabel(rehearsals.items.length, "rehearsal")} />
            <Notice
                message={loadError}
                onDismiss={() => { rehearsals.setError(null); bands.setError(null); rooms.setError(null); }}
            />

            {isAdmin ? (
                <form className="editor" onSubmit={handleSubmit}>
                    <h2>{editing ? "Edit rehearsal" : "Schedule a rehearsal"}</h2>
                    <div className="fields">
                        <label>
                            Date
                            <input type="date" required value={form.date} onChange={field("date")} />
                        </label>
                        <label>
                            Length (minutes)
                            <input
                                type="number"
                                required
                                min="15"
                                max="720"
                                step="15"
                                value={form.durationMinutes}
                                onChange={field("durationMinutes")}
                            />
                        </label>
                        <label>
                            Band
                            <select required value={form.bandId} onChange={field("bandId")}>
                                <option value="">Choose a band</option>
                                {bands.items.map((band) => (
                                    <option key={band.id} value={band.id}>{band.name}</option>
                                ))}
                            </select>
                        </label>
                        <label>
                            Room
                            <select required value={form.roomId} onChange={field("roomId")}>
                                <option value="">Choose a room</option>
                                {rooms.items.map((room) => (
                                    <option key={room.id} value={room.id}>{room.name}</option>
                                ))}
                            </select>
                        </label>
                    </div>
                    <FormActions editing={editing} addLabel="Schedule rehearsal" onCancel={() => setForm(EMPTY_FORM)} />
                </form>
            ) : (
                <ReadOnlyNote />
            )}

            <div className="table-wrap">
                <table className="data">
                    <thead>
                        <tr>
                            <th scope="col">Date</th>
                            <th scope="col">Band</th>
                            <th scope="col">Room</th>
                            <th scope="col" className="num">Length</th>
                            {isAdmin && <th scope="col"><span className="sr-only">Actions</span></th>}
                        </tr>
                    </thead>
                    <tbody>
                        {sorted.map((rehearsal) => {
                            const past = rehearsal.date < today;
                            const classes = [past && "is-past", form.id === rehearsal.id && "is-editing"]
                                .filter(Boolean).join(" ");
                            return (
                                <tr key={rehearsal.id} className={classes || undefined}>
                                    <td className="nowrap">
                                        {formatDate(rehearsal.date)}
                                        {past && <span className="sub scrawl">Past</span>}
                                    </td>
                                    <td className="lead-cell">{rehearsal.band?.name}</td>
                                    <td>{rehearsal.rehearsalRoom?.name}</td>
                                    <td className="num nowrap">{formatDuration(rehearsal.durationMinutes)}</td>
                                    {isAdmin && (
                                        <RowActions
                                            label={`rehearsal on ${formatDate(rehearsal.date)}`}
                                            onEdit={() => startEditing(rehearsal)}
                                            onDelete={() => handleDelete(rehearsal)}
                                        />
                                    )}
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
            {!rehearsals.loading && rehearsals.items.length === 0 && (
                <EmptyState adminHint="Schedule the first one with the form above.">No rehearsals yet.</EmptyState>
            )}
        </>
    );
}
