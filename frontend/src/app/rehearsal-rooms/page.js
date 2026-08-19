"use client";

import { useState, useEffect } from "react";
import axios from "axios";

export default function RehearsalRooms() {
    const [rooms, setRooms] = useState([]);
    const [name, setName] = useState("");
    const [address, setAddress] = useState("");
    const [capacity, setCapacity] = useState("");
    const [editingId, setEditingId] = useState(null);

    function loadRooms() {
        axios.get("http://localhost:8080/rehearsal-room/get-list")
            .then(response => setRooms(response.data))
            .catch(error => console.log(error));
    }

    useEffect(() => {
        loadRooms();
    },  []);

    function saveRoom() {
        const room = {
            id: editingId,
            name: name,
            address: address,
            capacity: capacity
        };

        if (editingId === null) {
            axios.post("http://localhost:8080/rehearsal-room/create", room)
                .then(() => { loadRooms(); clearForm(); })
                .catch(error => console.log(error));
        } else {
            axios.put("http://localhost:8080/rehearsal-room/update ", room)
                .then(() => { loadRooms(); clearForm(); })
                .catch(error => console.log(error));
        }
    }

    function editRoom(room) {
        setEditingId(room.id);
        setName(room.name);
        setAddress(room.address);
        setCapacity(room.capacity);
    }

    function deleteRoom(id) {
        axios.delete("http://localhost:8080/rehearsal-room/delete/" + id)
            .then(() => loadRooms())
            .catch(error => console.log(error));
    }

    function clearForm() {
        setEditingId(null);
        setName("");
        setAddress("");
        setCapacity("");
    }

    return (
        <div>
            <h1>Rehearsal Rooms</h1>

            <div>
                <input
                    placeholder="Name"
                    value={name}
                    onChange={e => setName(e.target.value)}
                />

                <input
                    placeholder="Address"
                    value={address}
                    onChange={e => setAddress(e.target.value)}
                />
                <input
                    placeholder="Capacity"
                    value={capacity}
                    onChange={e => setCapacity(e.target.value)}
                />
                <button onClick={saveRoom}>
                    {editingId === null ? "Add room" : "Save changes"}
                </button>
            </div>

            <ul>
                {rooms.map(room => (
                    <li key={room.id}>
                        {room.name} - {room.address} (capacity: {room.capacity})
                        <button onClick={() => editRoom(room)}>Edit</button>
                        <button onClick={() => deleteRoom(room.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}