"use client";

import { useAuth } from "@/lib/auth";

// The page title, written on a strip of console tape in the section's colour
export function PageHeader({ channel, title, summary }) {
    return (
        <div className="page-header" data-channel={channel}>
            <h1 className="tape">{title}</h1>
            {summary && <p className="summary">{summary}</p>}
        </div>
    );
}

export function Notice({ message, onDismiss }) {
    if (!message) return null;
    return (
        <div className="notice" role="alert">
            <p>{message}</p>
            {onDismiss && (
                <button type="button" className="button small" onClick={onDismiss}>
                    Dismiss
                </button>
            )}
        </div>
    );
}

// Shown instead of the form to users who are not admins
export function ReadOnlyNote() {
    return <p className="read-only">You can view this list. Only an admin can add, edit or delete.</p>;
}

// Shown for an empty list: admins get a pointer to the form, others a plain statement
export function EmptyState({ children, adminHint }) {
    const { isAdmin } = useAuth();
    return (
        <p className="empty">
            {children}
            {isAdmin && adminHint ? ` ${adminHint}` : ""}
        </p>
    );
}

export function RowActions({ label, onEdit, onDelete }) {
    return (
        <td className="row-actions">
            <button type="button" className="button link" onClick={onEdit} aria-label={`Edit ${label}`}>
                Edit
            </button>
            <button type="button" className="button link danger" onClick={onDelete} aria-label={`Delete ${label}`}>
                Delete
            </button>
        </td>
    );
}

export function FormActions({ editing, addLabel, onCancel }) {
    return (
        <div className="form-actions">
            <button type="submit" className="button primary">
                {editing ? "Save changes" : addLabel}
            </button>
            {editing && (
                <button type="button" className="button" onClick={onCancel}>
                    Cancel
                </button>
            )}
        </div>
    );
}
