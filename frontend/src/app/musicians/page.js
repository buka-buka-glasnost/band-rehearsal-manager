"use client";

import { useState, useEffect } from "react";
import axios from "axios";

export default function Musicians() {
    const [musicians, setMusicians] = useState([]);
    const [bands, setBands] = useState([]);
    const [name, setName] = useState("");
    const [instrument, setInstrument] = useState("");
    const [selectedBandIds,setSelectedBandIds] = useState([]);
    const [editingId, setEditingId] = useState(null);

    function  loadMusicians() {
        axios.get("http://localhost:8080/musician/get-list")
            .then(response => setMusicians(response.data))
            .catch(error => console.log(error));
    }

    function loadBands() {
        axios.get("http://localhost:8080/band/get-list")
            .then(response => setBands(response.data))
            .catch(error => console.log(error));
    }

    useEffect(() => {
        loadMusicians();
        loadBands();
    }, []);

    function  toggleBand(bandId) {
        if (selectedBandIds.includes(bandId)) {
            setSelectedBandIds(selectedBandIds.filter(id => id !== bandId));
        } else {
            setSelectedBandIds([...selectedBandIds, bandId]);
        }
    }

    function saveMusician() {
        const musician = {
            id: editingId,
            name: name,
            instrument: instrument,
            bands: selectedBandIds.map(id => ({ id: id }))
        };

        if (editingId == null) {
            axios.post("http://localhost:8080/musician/create", musician)
                .then(() => { loadMusicians(); clearForm();})
                .catch(error => console.log(error));
        } else {
            axios.put("http://localhost:8080/musician/update", musician)
                .then(() => { loadMusicians(); clearForm(); })
                .catch(error => console.log(error));
        }
    }

    function editMusician(musician) {
        setEditingId(musician.id);
        setName(musician.name);
        setInstrument(musician.instrument);
        setSelectedBandIds(musician.bands ? musician.bands.map(b => b.id) : []);
    }

    function deleteMusician(id) {
        axios.delete("http://localhost:8080/musician/delete/" + id)
            .then(() => loadMusicians())
            .catch(error => console.log(error));
    }

    function clearForm() {
        setEditingId(null);
        setName("");
        setInstrument("");
        setSelectedBandIds([]);
    }

    return (
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

                <div>
                    <p>Bands:</p>
                    {bands.map(band => (
                        <label key={band.id} style={{display: "block"}}>
                            <input
                                type="checkbox"
                                checked={selectedBandIds.includes(band.id)}
                                onChange={() => toggleBand(band.id)}
                            />
                            {band.name}
                        </label>
                    ))}
                </div>

                <button onClick={saveMusician}>
                    {editingId === null ? "Add musician" : "Save changes"}
                </button>
            </div>

            <ul>
                {musicians.map(musician => (
                    <li key={musician.id}>
                        {musician.name} - {musician.instrument}
                        {musician.bands && musician.bands.length > 0
                            ? " (" + musician.bands.map(b => b.name).join(", ") + ")"
                            : ""
                        }
                        <button onClick={() => editMusician(musician)}>Edit</button>
                        <button onClick={() => deleteMusician(musician.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}