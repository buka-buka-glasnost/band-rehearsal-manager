"use client";

import { useState } from "react";
import { useAuth } from "@/lib/auth";
import { countLabel, toNumberOrNull } from "@/lib/format";
import { useResource } from "@/lib/useResource";
import { EmptyState, FormActions, Notice, PageHeader, ReadOnlyNote, RowActions } from "@/components/ui";

const EMPTY_FORM = { id: null, name: "", address: "", capacity: "" };

export default function RehearsalRoomsPage() {
    const { isAdmin } = useAuth();
    const rooms = useResource("rehearsal-room");
    const [form, setForm] = useState(EMPTY_FORM);
    const editing = form.id !== null;

    const field = (name) => (event) => setForm({ ...form, [name]: event.target.value });

    async function handleSubmit(event) {
        event.preventDefault();
        const saved = await rooms.save({
            id: form.id,
            name: form.name.trim(),
            address: form.address.trim(),
            capacity: toNumberOrNull(form.capacity),
        });
        if (saved) setForm(EMPTY_FORM);
    }

    function startEditing(room) {
        setForm({ id: room.id, name: room.name ?? "", address: room.address ?? "", capacity: room.capacity ?? "" });
    }

    async function handleDelete(room) {
        if (!window.confirm(`Delete the room "${room.name}"?`)) return;
        if (await rooms.remove(room.id) && form.id === room.id) setForm(EMPTY_FORM);
    }

    return (
        <>
            <PageHeader channel="rooms" title="Rehearsal rooms" summary={countLabel(rooms.items.length, "room")} />
            <Notice message={rooms.error} onDismiss={() => rooms.setError(null)} />

            {isAdmin ? (
                <form className="editor" onSubmit={handleSubmit}>
                    <h2>{editing ? `Edit ${form.name || "room"}` : "Add a room"}</h2>
                    <div className="fields">
                        <label>
                            Name
                            <input required maxLength={255} value={form.name} onChange={field("name")} />
                        </label>
                        <label>
                            Address
                            <input required maxLength={255} value={form.address} onChange={field("address")} />
                        </label>
                        <label>
                            Capacity (people)
                            <input type="number" required min="1" max="100" value={form.capacity} onChange={field("capacity")} />
                        </label>
                    </div>
                    <FormActions editing={editing} addLabel="Add room" onCancel={() => setForm(EMPTY_FORM)} />
                </form>
            ) : (
                <ReadOnlyNote />
            )}

            <div className="table-wrap">
                <table className="data">
                    <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">Address</th>
                            <th scope="col" className="num">Capacity</th>
                            {isAdmin && <th scope="col"><span className="sr-only">Actions</span></th>}
                        </tr>
                    </thead>
                    <tbody>
                        {rooms.items.map((room) => (
                            <tr key={room.id} className={form.id === room.id ? "is-editing" : undefined}>
                                <td>{room.name}</td>
                                <td>{room.address}</td>
                                <td className="num">{room.capacity}</td>
                                {isAdmin && (
                                    <RowActions
                                        label={room.name}
                                        onEdit={() => startEditing(room)}
                                        onDelete={() => handleDelete(room)}
                                    />
                                )}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {!rooms.loading && rooms.items.length === 0 && (
                <EmptyState adminHint="Add the first one with the form above.">No rooms yet.</EmptyState>
            )}
        </>
    );
}
