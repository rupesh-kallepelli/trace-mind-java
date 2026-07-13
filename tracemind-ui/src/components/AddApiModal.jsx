import { useState } from "react";

import {
  createApi
} from "../services/apiDefinitionService";

export default function AddApiModal({
  open,
  onClose,
  onSaved,
  microserviceId
}) {

  const [form, setForm] =
    useState({
      microservice: {
        id: microserviceId
      },
      name: "",
      method: "GET",
      path: "",
      description: "",
      requestContext: "",
      responseContext: "",
      failurePatterns: ""
    });

  if (!open) return null;

  const save = async () => {

    await createApi(form);

    onSaved();

    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">

      <div className="app-card w-full max-w-4xl p-6 rounded-3xl">

        <h2 className="text-2xl font-bold">
          Add API
        </h2>

        <div className="grid md:grid-cols-2 gap-4 mt-6">

          <input
            className="app-input"
            placeholder="API Name"
            onChange={(e)=>
              setForm({
                ...form,
                name:e.target.value
              })
            }
          />

          <select
            className="app-input"
            onChange={(e)=>
              setForm({
                ...form,
                method:e.target.value
              })
            }
          >
            <option>GET</option>
            <option>POST</option>
            <option>PUT</option>
            <option>DELETE</option>
          </select>

          <input
            className="app-input md:col-span-2"
            placeholder="/api/customers/{id}"
            onChange={(e)=>
              setForm({
                ...form,
                path:e.target.value
              })
            }
          />

        </div>

        <textarea
          className="app-input w-full mt-4"
          rows={3}
          placeholder="Description"
          onChange={(e)=>
            setForm({
              ...form,
              description:e.target.value
            })
          }
        />

        <textarea
          className="app-input w-full mt-4"
          rows={3}
          placeholder="Failure Patterns"
          onChange={(e)=>
            setForm({
              ...form,
              failurePatterns:e.target.value
            })
          }
        />

        <div className="flex justify-end gap-3 mt-5">

          <button
            onClick={onClose}
            className="app-button-secondary"
          >
            Cancel
          </button>

          <button
            onClick={save}
            className="app-button-primary"
          >
            Save API
          </button>

        </div>

      </div>

    </div>
  );
}