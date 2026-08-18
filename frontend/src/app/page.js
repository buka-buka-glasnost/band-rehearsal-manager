"use client";

import { useState, useEffect } from "react";
import axios from "axios";

export default function Home() {
  const [bands, setBands] = useState([]);
  const [name, setName] = useState("");
  const[genre, setGenre] = useState("");
  const[foundedYear, setFoundedYear] = useState("");
  const [editingId, setEditingId] = useState(null);

  function loadBands() {
      axios.get("http://localhost:8080/band/get-list")
          .then(response => setBands(response.data))
          .catch(error => console.log(error));
  }

   useEffect(() => {
        loadBands();
   }, []);

  function saveBand() {
      const band = {
          id: editingId,
          name: name,
          genre: genre,
          foundedYear: foundedYear
      };

      if (editingId === null) {
          axios.post("http://localhost:8080/band/create", band)
              .then(() => { loadBands(); clearForm(); })
              .catch(error => console.log(error));
      } else {
          axios.put("http://localhost:8080/band/update", band)
              .then(() => { loadBands(); clearForm(); })
              .catch(error => console.log(error));
      }
  }
  function editBand(band) {
      setEditingId(band.id);
      setName(band.name);
      setGenre(band.genre);
      setFoundedYear((band.foundedYear));
  }

  function clearForm() {
      setEditingId(null);
      setName("");
      setGenre("");
      setFoundedYear("");
  }

  function deleteBand(id) {
      axios.delete("http://localhost:8080/band/delete/" + id)
          .then(() => loadBands())
          .catch(error => console.log(error));
  }

  return (
      <div>
          <h1>Bands</h1>

          <div>

            <input
                placeholder="Band name"
                value={name}
                onChange={e => setName(e.target.value)}
            />
            <input
                placeholder="Genre"
                value={genre}
                onChange={e => setGenre(e.target.value)}
            />
            <input
                placeholder="Year founded"
                value={foundedYear}
                onChange={e => setFoundedYear(e.target.value)}
            />
            <button onClick={saveBand}>
                {editingId === null ? "Add band" : "Save changes"}</button>
          </div>


        <ul>
          {bands.map(band => (
              <li key={band.id}>
                  {band.name} - {band.genre} ({band.foundedYear})
                  <button onClick={() => editBand(band)}>Edit</button>
                  <button onClick={() => deleteBand(band.id)}>Delete</button>
              </li>
          ))}
        </ul>

      </div>
  );
}