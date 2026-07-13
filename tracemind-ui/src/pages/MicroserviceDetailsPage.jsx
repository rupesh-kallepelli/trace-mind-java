import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";

import APIsTab from "../components/APIsTab";

export default function MicroserviceDetailsPage() {

  const { serviceId } = useParams();

  const [activeTab, setActiveTab] =
    useState("APIs");

  return (
    <div className="space-y-6">

      <div className="app-card p-6 rounded-3xl">

        <h1 className="text-3xl font-bold">
          Microservice
        </h1>

        <p className="app-muted mt-2">
          Service Details
        </p>

      </div>

      <div className="flex gap-2">

        <button
          className="
            app-button-primary
          "
        >
          APIs
        </button>

      </div>

      {activeTab === "APIs" && (
        <APIsTab
          microserviceId={serviceId}
        />
      )}

    </div>
  );
}