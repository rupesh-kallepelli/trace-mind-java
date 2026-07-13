import { useEffect, useState } from "react";
import { Plus } from "lucide-react";

import {
  getApisByMicroservice
} from "../services/apiDefinitionService";

import AddApiModal from "./AddApiModal";

export default function APIsTab({
  microserviceId
}) {

  const [apis, setApis] =
    useState([]);

  const [showModal, setShowModal] =
    useState(false);

  const loadApis = async () => {

    const data =
      await getApisByMicroservice(
        microserviceId
      );

    setApis(
      Array.isArray(data)
        ? data
        : []
    );
  };

  useEffect(() => {
    loadApis();
  }, []);

  return (
    <div>

      <div className="flex justify-between mb-6">

        <h2 className="text-xl font-semibold">
          APIs
        </h2>

        <button
          onClick={() =>
            setShowModal(true)
          }
          className="
            app-button-primary
            flex
            gap-2
            items-center
          "
        >
          <Plus size={16} />
          Add API
        </button>

      </div>

      <div className="grid gap-4">

        {apis.map(api => (

          <div
            key={api.id}
            className="
              app-card
              rounded-2xl
              p-5
            "
          >

            <div className="font-semibold">
              {api.method}
            </div>

            <div className="text-cyan-400">
              {api.path}
            </div>

            <div className="mt-2 app-muted">
              {api.description}
            </div>

          </div>

        ))}

      </div>

      <AddApiModal
        microserviceId={microserviceId}
        open={showModal}
        onClose={() =>
          setShowModal(false)
        }
        onSaved={loadApis}
      />

    </div>
  );
}