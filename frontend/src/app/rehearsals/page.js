"use client";

import { useState, useEffect } from "react";
import axios from "axios";

export default function Rehearsals() {
    const [rehearsals, setRehearsals] = useState([]);
    const [bands, setBands] = useState([]);
    const [date, setDate] = useState("");
    const [durationMinutes, setDurationMinutes] = useState("");
    const [bandId, setBandId] = useState("")
    const [editingId, setEditingId] = useState(null);
    const [rooms, setRooms] = useState([]);
    const [roomId, setRoomId] = useState("");

    function loadRehearsals() {
        axios.get("http://localhost:8080/rehearsal/get-list")
            .then(response => setRehearsals(response.data))
            .catch(error => console.log(error));
    }

    function loadBands() {
        axios.get("http://localhost:8080/band/get-list")
            .then(response => setBands(response.data))
            .catch(error => console.log(error));
    }

    function loadRooms() {
        axios.get("http://localhost:8080/rehearsal-room/get-list")
            .then(response => setRooms(response.data))
            .catch(error => console.log(error));
    }

    useEffect(() => {
        loadRehearsals();
        loadBands();
        loadRooms();
        }, []);


    function saveRehearsal() {
        const rehearsal = {
            id: editingId,
            date: date,
            durationMinutes: durationMinutes,
            band: bandId ? { id: bandId } : null,
            rehearsalRoom: roomId ? { id: roomId } : null
        };

        if (editingId === null) {
            axios.post("http://localhost:8080/rehearsal/create", rehearsal)
                .then(() => { loadRehearsals(); clearForm(); })
                .catch(error => console.log(error));
        } else {
            axios.put("http://localhost:8080/rehearsal/update", rehearsal)
                .then(() => { loadRehearsals(); clearForm();})
                .catch(error => console.log(error));
        }
    }

    function editRehearsal(rehearsal) {
        setEditingId(rehearsal.id);
        setDate(rehearsal.date);
        setDurationMinutes(rehearsal.durationMinutes);
        setBandId(rehearsal.band ? rehearsal.band.id : "");
        setRoomId(rehearsal.rehearsalRoom ? rehearsal.rehearsalRoom.id : "");
    }

    function deleteRehearsal(id) {
        axios.delete("http://localhost:8080/rehearsal/delete/" + id)
            .then(() => loadRehearsals())
            .catch(error => console.log(error));
    }
    function clearForm() {
        setEditingId(null);
        setDate("");
        setDurationMinutes("");
        setBandId("");
        setRoomId("");
    }

    return (
        <div>
            <h1>Rehearsals</h1>

            <div>
                <input
                    type="date"
                    value={date}
                    onChange={e => setDate(e.target.value)}
                />
                <input
                    placeholder="Duration (minutes)"
                    value={durationMinutes}
                    onChange={e => setDurationMinutes(e.target.value)}
                />
                <select value={bandId} onChange={e => setBandId(e.target.value)}>
                    <option value="">-- Select band --</option>
                    {bands.map(band => (
                        <option key={band.id} value={band.id}>{band.name}</option>
                    ))}
                </select>
                <select value={roomId} onChange={e => setRoomId(e.target.value)}>
                    <option value="">-- Select room --</option>
                    {rooms.map(room => (
                        <option key={room.id} value={room.id}>{room.name}</option>
                    ))}
                </select>
                <button onClick={saveRehearsal}>
                    {editingId === null ? "Add rehearsal" : "Save changes"}
                </button>
            </div>

            <ul>
                {rehearsals.map(rehearsal => (
                    <li key={rehearsal.id}>
                        {rehearsal.date} - {rehearsal.durationMinutes} min
                        {rehearsal.band ? " - " + rehearsal.band.name : ""}
                        {rehearsal.rehearsalRoom ? " @ " + rehearsal.rehearsalRoom.name : ""}
                        <button onClick={() => editRehearsal(rehearsal)}>Edit</button>
                        <button onClick={() => deleteRehearsal(rehearsal.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}