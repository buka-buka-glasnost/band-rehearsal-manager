"use client";

import { useState } from "react";
import { useAuth } from "@/lib/auth";
import { countLabel, toNumberOrNull } from "@/lib/format";
import { useResource } from "@/lib/useResource";
import { EmptyState, FormActions, Notice, PageHeader, ReadOnlyNote, RowActions } from "@/components/ui";

const EMPTY_FORM = { id: null, name: "", genre: "", foundedYear: "" };

export default function BandsPage() {
    const { isAdmin } = useAuth();
    const bands = useResource("band");
    const [form, setForm] = useState(EMPTY_FORM);
    const editing = form.id !== null;

    const field = (name) => (event) => setForm({ ...form, [name]: event.target.value });

    async function handleSubmit(event) {
        event.preventDefault();
        const saved = await bands.save({
            id: form.id,
            name: form.name.trim(),
            genre: form.genre.trim(),
            foundedYear: toNumberOrNull(form.foundedYear),
        });
        if (saved) setForm(EMPTY_FORM);
    }

    function startEditing(band) {
        setForm({ id: band.id, name: band.name ?? "", genre: band.genre ?? "", foundedYear: band.foundedYear ?? "" });
    }

    async function handleDelete(band) {
        if (!window.confirm(`Delete the band "${band.name}"?`)) return;
        if (await bands.remove(band.id) && form.id === band.id) setForm(EMPTY_FORM);
    }

    return (
        <>
            <PageHeader title="Bands" note={countLabel(bands.items.length, "band")} />
            <Notice message={bands.error} onDismiss={() => bands.setError(null)} />

            {isAdmin ? (
                <form className="editor" onSubmit={handleSubmit}>
                    <h2>{editing ? `Edit ${form.name || "band"}` : "Add a band"}</h2>
                    <div className="fields">
                        <label>
                            Name
                            <input required maxLength={255} value={form.name} onChange={field("name")} />
                        </label>
                        <label>
                            Genre
                            <input required maxLength={255} value={form.genre} onChange={field("genre")} />
                        </label>
                        <label>
                            Founded in
                            <input type="number" min="1900" max="2100" value={form.foundedYear} onChange={field("foundedYear")} />
                        </label>
                    </div>
                    <FormActions editing={editing} addLabel="Add band" onCancel={() => setForm(EMPTY_FORM)} />
                </form>
            ) : (
                <ReadOnlyNote />
            )}

            <div className="table-wrap">
                <table className="data">
                    <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">Genre</th>
                            <th scope="col" className="num">Founded</th>
                            {isAdmin && <th scope="col"><span className="sr-only">Actions</span></th>}
                        </tr>
                    </thead>
                    <tbody>
                        {bands.items.map((band) => (
                            <tr key={band.id} className={form.id === band.id ? "is-editing" : undefined}>
                                <td className="lead-cell">{band.name}</td>
                                <td>{band.genre}</td>
                                <td className="num">{band.foundedYear}</td>
                                {isAdmin && (
                                    <RowActions
                                        label={band.name}
                                        onEdit={() => startEditing(band)}
                                        onDelete={() => handleDelete(band)}
                                    />
                                )}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {!bands.loading && bands.items.length === 0 && (
                <EmptyState adminHint="Add the first one with the form above.">No bands yet.</EmptyState>
            )}
        </>
    );
}
