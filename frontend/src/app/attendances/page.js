"use client";

import { useState, useEffect } from "react";
import axios from "axios";

export default function Attendances() {
    const [attendances, setAttendances] = useState([]);
    const [musicians, setMusicians] = useState([]);
    const [rehearsals, setRehearsals] = useState([]);
    const [musicianId, setMusicianId] = useState("");
    const [rehearsalId, setRehearsalId] = useState("");
    const [present, setPresent] = useState(false);
    const [editingId, setEditingId] = useState(null);

    function loadAttendances() {
        axios.get("http://localhost:8080/attendance/get-list")
            .then(response => setAttendances(response.data))
            .catch(error => console.log(error));
    }

    function loadMusicians() {
        axios.get("http://localhost:8080/musician/get-list")
            .then(response => setMusicians(response.data))
            .catch(error => console.log(error))
    }

    function loadRehearsals(){
        axios.get("http://localhost:8080/rehearsal/get-list")
            .then(response => setRehearsals(response.data))
            .catch(error => console.log(error));
    }

    useEffect(() => {
        loadAttendances();
        loadMusicians();
        loadRehearsals();
    }, [])

    function saveAttendance() {
        const attendance ={
            id: editingId,
            present: present,
            musician: musicianId ? { id: musicianId } : null,
            rehearsal: rehearsalId ? { id: rehearsalId } : null
        };

        if (editingId === null) {
            axios.post("http://localhost:8080/attendance/create", attendance)
                .then(() => { loadAttendances(); clearForm(); })
                .catch(error => console.log(error));
        } else {
            axios.put("http://localhost:8080/attendance/update", attendance)
                .then(() => { loadAttendances(); clearForm(); })
                .catch(error => console.log(error));
        }
    }

    function editAttendance(attendance) {
        setEditingId(attendance.id);
        setPresent(attendance.present);
        setMusicianId(attendance.musician ? attendance.musician.id : "");
        setRehearsalId(attendance.rehearsal ? attendance.rehearsal.id : "");
    }

    function deleteAttendance(id) {
        axios.delete("http://localhost:8080/attendance/delete/" + id)
            .then(() => loadAttendances())
            .catch(error => console.log(error));
    }

    function clearForm() {
        setEditingId(null);
        setPresent(false);
        setMusicianId("");
        setRehearsalId("");
    }

    return (
        <div>
            <h1>Attendances</h1>

            <div>
                <select value={musicianId} onChange={e => setMusicianId(e.target.value)}>
                    <option value="">-- Select musician --</option>
                    {musicians.map(musician => (
                        <option key={musician.id} value={musician.id}>{musician.name}</option>
                    ))}
                </select>

                <select value={rehearsalId} onChange={e => setRehearsalId(e.target.value)}>
                    <option value="">-- Select rehearsal --</option>
                    {rehearsals.map(rehearsal => (
                        <option key={rehearsal.id} value={rehearsal.id}>
                            {rehearsal.date} ({rehearsal.band ? rehearsal.band.name : "no band"})
                        </option>
                    ))}
                </select>

                <label>
                    <input
                        type="checkbox"
                        checked={present}
                        onChange={e => setPresent(e.target.checked)}
                    />
                    Present
                </label>

                <button onClick={saveAttendance}>
                    {editingId === null ? "Add attendance" : "Save changes"}
                </button>
            </div>

            <ul>
                {attendances.map(attendance => (
                    <li key={attendance.id}>
                        {attendance.musician ? attendance.musician.name : "?"}
                        {" - "}
                        {attendance.rehearsal ? attendance.rehearsal.date: "?"}
                        {" - "}
                        {attendance.present ? "Present" : "Absent"}
                        <button onClick={() => editAttendance(attendance)}>Edit</button>
                        <button onClick={() => deleteAttendance(attendance.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}