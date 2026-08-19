"use client";

import { useState, useEffect } from "react";
import axios from "axios";

export default function Musicians() {
    const [musicians, setMusicians] = useState([]);
    const [name, setName] = useState("");
    const [instrument, setInstrument] = useState("");
    const [editingId, setEditingId] = useState(null);

    function loadMusicians() {
        axios.get("http://localhost:8080/musician/get-list")
            .then(response => setMusicians(response.data))
            .catch(error => console.log(error));
    }

    useEffect(() => {
        loadMusicians();
    }, []);

    function saveMusician() {
        const musician ={
            id: editingId,
            name: name,
            instrument: instrument
        };

        if (editingId === null) {
            axios.post("http://localhost:8080/musician/create", musician)
                .then(() => { loadMusicians(); clearForm(); })
                .catch(error => console.log(error));
        } else {
            axios.put("http://localhost:8080/musician/update", musician)
                .then(() => { loadMusicians(); clearForm(); })
                .catch(error => console.log(error));
        }
    }

    function editMusician(id) {
        axios.delete("http://localhost:8080/musician/delete/" + id)
            .then(() => loadMusicians())
            .catch(error => console.log(error));
    }

    function deleteMusician(id) {
        axios.delete("http://localhost:8080/musician/delete" + id)
            .then(() => loadMusicians())
            .catch(error => console.log(error));
    }

    function clearForm() {
        setEditingId(null);
        setName("");
        setInstrument("");
    }

    return(
        <div>
            <h1>Musicians</h1>

            <div>
                <input
                    placeholder="Name"
                    value={name}
                    onChange={e => setName(e.target.value)}
                />
                <input
                    placeholder="Instrument"
                    value={instrument}
                    onChange={e => setInstrument(e.target.value)}
                />
                <button onClick={saveMusician}>
                    {editingId === null ? "Add musician" : "Save changes"}
                </button>
            </div>

            <ul>
                {musicians.map(musician => (
                    <li key={musician.id}>
                        {musician.name} - {musician.instrument}
                        <button onClick={() => editMusician(musician)}>Edit</button>
                        <button onClick={() => deleteMusician(musician.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );

}