import { useState } from "react";

import {
  createDatabase,
} from "../services/databaseService";

export default function AddDatabaseModal({
  applicationId,
  open,
  onClose,
  onSaved,
}) {

  const [form, setForm] = useState({
    application: {
      id: applicationId,
    },
    name: "",
    dbType: "",
    version: "",
    host: "",
    port: 5432,
    environment: "PROD",
    description: "",
    databaseContext: "",
  });

  if (!open) return null;

  const handleSave = async () => {

    try {

      await createDatabase(form);

      onSaved();
      onClose();

    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/70 flex justify-center items-center z-50">

      <div className="app-card p-6 rounded-3xl w-full max-w-4xl">

        <h2 className="text-2xl font-bold mb-6">
          Add Database
        </h2>

        <div className="grid md:grid-cols-2 gap-4">

          <input
            className="app-input"
            placeholder="Database Name"
            onChange={(e) =>
              setForm({
                ...form,
                name: e.target.value,
              })
            }
          />

          <select
            className="app-input"
            onChange={(e) =>
              setForm({
                ...form,
                dbType: e.target.value,
              })
            }
          >
            <option>PostgreSQL</option>
            <option>Oracle</option>
            <option>MySQL</option>
            <option>SQL Server</option>
            <option>MongoDB</option>
          </select>

          <input
            className="app-input"
            placeholder="Version"
            onChange={(e) =>
              setForm({
                ...form,
                version: e.target.value,
              })
            }
          />

          <input
            className="app-input"
            placeholder="Host"
            onChange={(e) =>
              setForm({
                ...form,
                host: e.target.value,
              })
            }
          />

          <input
            className="app-input"
            placeholder="Port"
            onChange={(e) =>
              setForm({
                ...form,
                port: Number(
                  e.target.value
                ),
              })
            }
          />

          <select
            className="app-input"
            onChange={(e) =>
              setForm({
                ...form,
                environment:
                  e.target.value,
              })
            }
          >
            <option>DEV</option>
            <option>QA</option>
            <option>UAT</option>
            <option>PROD</option>
          </select>

        </div>

        <textarea
          rows={4}
          className="
            app-input
            w-full
            mt-4
          "
          placeholder="Description"
          onChange={(e) =>
            setForm({
              ...form,
              description:
                e.target.value,
            })
          }
        />

        <textarea
          rows={5}
          className="
            app-input
            w-full
            mt-4
          "
          placeholder="Database Context"
          onChange={(e) =>
            setForm({
              ...form,
              databaseContext:
                e.target.value,
            })
          }
        />

        <div className="flex justify-end gap-3 mt-6">

          <button
            className="app-button-secondary"
            onClick={onClose}
          >
            Cancel
          </button>

          <button
            className="app-button-primary"
            onClick={handleSave}
          >
            Save Database
          </button>

        </div>

      </div>

    </div>
  );
}