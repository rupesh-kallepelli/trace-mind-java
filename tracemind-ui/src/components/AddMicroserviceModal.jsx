import { useState } from "react";

import {
  createMicroservice,
} from "../services/microserviceService";

export default function AddMicroserviceModal({
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
    serviceCode: "",
    language: "",
    framework: "",
    version: "",
    port: "",
    repositoryUrl: "",
    healthEndpoint: "",
    metricsEndpoint: "",
    businessCapability: "",
    serviceContext: "",
  });

  if (!open) return null;

  const handleSave = async () => {

    await createMicroservice(form);

    onSaved();
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/60 flex justify-center items-center z-50">

      <div className="app-card w-full max-w-4xl p-6 rounded-3xl">

        <h2 className="text-2xl font-bold mb-6">
          Add Microservice
        </h2>

        <div className="grid md:grid-cols-2 gap-4">

          <input
            className="app-input"
            placeholder="Service Name"
            onChange={(e) =>
              setForm({
                ...form,
                name: e.target.value,
              })
            }
          />

          <input
            className="app-input"
            placeholder="Service Code"
            onChange={(e) =>
              setForm({
                ...form,
                serviceCode:
                  e.target.value,
              })
            }
          />

          <input
            className="app-input"
            placeholder="Language"
            onChange={(e) =>
              setForm({
                ...form,
                language:
                  e.target.value,
              })
            }
          />

          <input
            className="app-input"
            placeholder="Framework"
            onChange={(e) =>
              setForm({
                ...form,
                framework:
                  e.target.value,
              })
            }
          />

          <input
            className="app-input"
            placeholder="Port"
            onChange={(e) =>
              setForm({
                ...form,
                port:
                  Number(
                    e.target.value
                  ),
              })
            }
          />

          <input
            className="app-input"
            placeholder="Version"
            onChange={(e) =>
              setForm({
                ...form,
                version:
                  e.target.value,
              })
            }
          />

        </div>

        <textarea
          className="
            app-input
            w-full
            mt-4
          "
          rows={4}
          placeholder="Business Capability"
          onChange={(e) =>
            setForm({
              ...form,
              businessCapability:
                e.target.value,
            })
          }
        />

        <textarea
          className="
            app-input
            w-full
            mt-4
          "
          rows={5}
          placeholder="Service Context"
          onChange={(e) =>
            setForm({
              ...form,
              serviceContext:
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
            Save Service
          </button>

        </div>

      </div>

    </div>
  );
}