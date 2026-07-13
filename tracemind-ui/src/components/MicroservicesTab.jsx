import { useEffect, useState } from "react";
import { Plus } from "lucide-react";

import {
  getMicroservicesByApplication,
} from "../services/microserviceService";

import AddMicroserviceModal
  from "./AddMicroserviceModal";

export default function MicroservicesTab({
  applicationId,
}) {

  const [services, setServices] =
    useState([]);

  const [showModal, setShowModal] =
    useState(false);

  const loadData = async () => {
    const data =
      await getMicroservicesByApplication(
        applicationId
      );

    setServices(
      Array.isArray(data) ? data : []
    );
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <div className="mt-6">

      <div className="flex justify-between mb-6">

        <h2 className="text-xl font-semibold">
          Microservices
        </h2>

        <button
          onClick={() =>
            setShowModal(true)
          }
          className="
            app-button-primary
            flex
            items-center
            gap-2
          "
        >
          <Plus size={16} />
          Add Service
        </button>

      </div>

      <div className="grid gap-4">

        {services.map((service) => (

          <div
            key={service.id}
            className="
              app-card
              border
              app-border
              rounded-2xl
              p-5
            "
          >
            <h3 className="font-semibold">
              {service.name}
            </h3>

            <div className="text-sm app-muted mt-2">
              {service.language}
              {" • "}
              {service.framework}
            </div>

            <div className="mt-3">
              Port: {service.port}
            </div>

            <div className="mt-3 text-sm">
              {service.businessCapability}
            </div>
          </div>

        ))}
      </div>

      <AddMicroserviceModal
        applicationId={applicationId}
        open={showModal}
        onClose={() =>
          setShowModal(false)
        }
        onSaved={loadData}
      />

    </div>
  );
}