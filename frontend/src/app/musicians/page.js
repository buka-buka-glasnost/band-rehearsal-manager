"use client";

import { useState } from "react";
import { useAuth } from "@/lib/auth";
import { countLabel } from "@/lib/format";
import { useResource } from "@/lib/useResource";
import { EmptyState, FormActions, Notice, PageHeader, ReadOnlyNote, RowActions } from "@/components/ui";

const EMPTY_FORM = { id: null, name: "", instrument: "", bandIds: [] };

export default function MusiciansPage() {
    const { isAdmin } = useAuth();
    const musicians = useResource("musician");
    const bands = useResource("band");
    const [form, setForm] = useState(EMPTY_FORM);
    const editing = form.id !== null;

    const field = (name) => (event) => setForm({ ...form, [name]: event.target.value });

    function toggleBand(bandId) {
        const bandIds = form.bandIds.includes(bandId)
            ? form.bandIds.filter((id) => id !== bandId)
            : [...form.bandIds, bandId];
        setForm({ ...form, bandIds });
    }

    async function handleSubmit(event) {
        event.preventDefault();
        const saved = await musicians.save({
            id: form.id,
            name: form.name.trim(),
            instrument: form.instrument.trim(),
            // ManyToMany: the backend only needs the ids of the bands
            bands: form.bandIds.map((id) => ({ id })),
        });
        if (saved) setForm(EMPTY_FORM);
    }

    function startEditing(musician) {
        setForm({
            id: musician.id,
            name: musician.name ?? "",
            instrument: musician.instrument ?? "",
            bandIds: (musician.bands ?? []).map((band) => band.id),
        });
    }

    async function handleDelete(musician) {
        if (!window.confirm(`Delete the musician "${musician.name}"?`)) return;
        if (await musicians.remove(musician.id) && form.id === musician.id) setForm(EMPTY_FORM);
    }

    return (
        <>
            <PageHeader title="Musicians" note={countLabel(musicians.items.length, "musician")} />
            <Notice message={musicians.error ?? bands.error} onDismiss={() => { musicians.setError(null); bands.setError(null); }} />

            {isAdmin ? (
                <form className="editor" onSubmit={handleSubmit}>
                    <h2>{editing ? `Edit ${form.name || "musician"}` : "Add a musician"}</h2>
                    <div className="fields">
                        <label>
                            Name
                            <input required maxLength={255} value={form.name} onChange={field("name")} />
                        </label>
                        <label>
                            Instrument
                            <input required maxLength={255} value={form.instrument} onChange={field("instrument")} />
                        </label>
                    </div>
                    <fieldset className="choices">
                        <legend>Plays in</legend>
                        {bands.items.length === 0 && <span className="field-help">Add a band first to link it here.</span>}
                        {bands.items.map((band) => (
                            <label key={band.id} className="choice">
                                <input
                                    type="checkbox"
                                    checked={form.bandIds.includes(band.id)}
                                    onChange={() => toggleBand(band.id)}
                                />
                                {band.name}
                            </label>
                        ))}
                    </fieldset>
                    <FormActions editing={editing} addLabel="Add musician" onCancel={() => setForm(EMPTY_FORM)} />
                </form>
            ) : (
                <ReadOnlyNote />
            )}

            <div className="table-wrap">
                <table className="data">
                    <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">Instrument</th>
                            <th scope="col">Bands</th>
                            {isAdmin && <th scope="col"><span className="sr-only">Actions</span></th>}
                        </tr>
                    </thead>
                    <tbody>
                        {musicians.items.map((musician) => (
                            <tr key={musician.id} className={form.id === musician.id ? "is-editing" : undefined}>
                                <td className="lead-cell">{musician.name}</td>
                                <td>{musician.instrument}</td>
                                <td>
                                    {musician.bands?.length
                                        ? musician.bands.map((band) => band.name).join(", ")
                                        : <span className="muted">No band</span>}
                                </td>
                                {isAdmin && (
                                    <RowActions
                                        label={musician.name}
                                        onEdit={() => startEditing(musician)}
                                        onDelete={() => handleDelete(musician)}
                                    />
                                )}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {!musicians.loading && musicians.items.length === 0 && (
                <EmptyState adminHint="Add the first one with the form above.">No musicians yet.</EmptyState>
            )}
        </>
    );
}
