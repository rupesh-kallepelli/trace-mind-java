import { useParams } from "react-router-dom";
import { useState } from "react";

import TablesTab from "../components/TablesTab";

export default function DatabaseDetailsPage() {

  const { databaseId } = useParams();

  const [activeTab, setActiveTab] =
    useState("Tables");

  return (
    <div className="space-y-6">

      <div className="app-card p-6 rounded-3xl">

        <h1 className="text-3xl font-bold">
          Database Details
        </h1>

        <p className="app-muted mt-2">
          Database metadata and tables
        </p>

      </div>

      <div className="flex gap-2">

        <button
          className="app-button-primary"
        >
          Tables
        </button>

      </div>

      {activeTab === "Tables" && (
        <TablesTab
          databaseId={databaseId}
        />
      )}

    </div>
  );
}