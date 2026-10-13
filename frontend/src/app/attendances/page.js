"use client";

import { useState } from "react";
import { useAuth } from "@/lib/auth";
import { countLabel, formatDate } from "@/lib/format";
import { useResource } from "@/lib/useResource";
import { EmptyState, FormActions, Notice, PageHeader, ReadOnlyNote, RowActions } from "@/components/ui";

const EMPTY_FORM = { id: null, musicianId: "", rehearsalId: "", present: true };

function rehearsalLabel(rehearsal) {
    return `${formatDate(rehearsal.date)}, ${rehearsal.band?.name ?? "no band"}`;
}

export default function AttendancePage() {
    const { isAdmin } = useAuth();
    const attendances = useResource("attendance");
    const musicians = useResource("musician");
    const rehearsals = useResource("rehearsal");
    const [form, setForm] = useState(EMPTY_FORM);
    const editing = form.id !== null;

    const field = (name) => (event) => setForm({ ...form, [name]: event.target.value });

    // Newest rehearsal first
    const sorted = [...attendances.items].sort((a, b) =>
        (b.rehearsal?.date ?? "").localeCompare(a.rehearsal?.date ?? ""));
    const rehearsalOptions = [...rehearsals.items].sort((a, b) => b.date.localeCompare(a.date));

    async function handleSubmit(event) {
        event.preventDefault();
        const saved = await attendances.save({
            id: form.id,
            present: form.present,
            musician: { id: Number(form.musicianId) },
            rehearsal: { id: Number(form.rehearsalId) },
        });
        if (saved) setForm(EMPTY_FORM);
    }

    function startEditing(attendance) {
        setForm({
            id: attendance.id,
            musicianId: String(attendance.musician?.id ?? ""),
            rehearsalId: String(attendance.rehearsal?.id ?? ""),
            present: Boolean(attendance.present),
        });
    }

    async function handleDelete(attendance) {
        const who = attendance.musician?.name ?? "this musician";
        if (!window.confirm(`Delete the attendance record for ${who}?`)) return;
        if (await attendances.remove(attendance.id) && form.id === attendance.id) setForm(EMPTY_FORM);
    }

    const loadError = attendances.error ?? musicians.error ?? rehearsals.error;

    return (
        <>
            <PageHeader channel="attendance" title="Attendance" summary={countLabel(attendances.items.length, "record")} />
            <Notice
                message={loadError}
                onDismiss={() => { attendances.setError(null); musicians.setError(null); rehearsals.setError(null); }}
            />

            {isAdmin ? (
                <form className="editor" onSubmit={handleSubmit}>
                    <h2>{editing ? "Edit attendance" : "Record attendance"}</h2>
                    <div className="fields">
                        <label>
                            Musician
                            <select required value={form.musicianId} onChange={field("musicianId")}>
                                <option value="">Choose a musician</option>
                                {musicians.items.map((musician) => (
                                    <option key={musician.id} value={musician.id}>{musician.name}</option>
                                ))}
                            </select>
                        </label>
                        <label>
                            Rehearsal
                            <select required value={form.rehearsalId} onChange={field("rehearsalId")}>
                                <option value="">Choose a rehearsal</option>
                                {rehearsalOptions.map((rehearsal) => (
                                    <option key={rehearsal.id} value={rehearsal.id}>{rehearsalLabel(rehearsal)}</option>
                                ))}
                            </select>
                        </label>
                        <label className="inline">
                            <input
                                type="checkbox"
                                checked={form.present}
                                onChange={(e) => setForm({ ...form, present: e.target.checked })}
                            />
                            Was present
                        </label>
                    </div>
                    <FormActions editing={editing} addLabel="Record attendance" onCancel={() => setForm(EMPTY_FORM)} />
                </form>
            ) : (
                <ReadOnlyNote />
            )}

            <div className="table-wrap">
                <table className="data">
                    <thead>
                        <tr>
                            <th scope="col">Rehearsal</th>
                            <th scope="col">Band</th>
                            <th scope="col">Musician</th>
                            <th scope="col">Status</th>
                            {isAdmin && <th scope="col"><span className="sr-only">Actions</span></th>}
                        </tr>
                    </thead>
                    <tbody>
                        {sorted.map((attendance) => (
                            <tr key={attendance.id} className={form.id === attendance.id ? "is-editing" : undefined}>
                                <td className="nowrap">{formatDate(attendance.rehearsal?.date)}</td>
                                <td>{attendance.rehearsal?.band?.name}</td>
                                <td>{attendance.musician?.name}</td>
                                <td>
                                    <span className="status" data-present={attendance.present ? "yes" : "no"}>
                                        {attendance.present ? "Present" : "Absent"}
                                    </span>
                                </td>
                                {isAdmin && (
                                    <RowActions
                                        label={`attendance of ${attendance.musician?.name ?? "musician"}`}
                                        onEdit={() => startEditing(attendance)}
                                        onDelete={() => handleDelete(attendance)}
                                    />
                                )}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {!attendances.loading && attendances.items.length === 0 && (
                <EmptyState adminHint="Record the first one with the form above.">No attendance records yet.</EmptyState>
            )}
        </>
    );
}
