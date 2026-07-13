import { useEffect, useState } from "react";
import { Plus, Database } from "lucide-react";

import {
  getDatabasesByApplication,
} from "../services/databaseService";

import AddDatabaseModal from "./AddDatabaseModal";

export default function DatabasesTab({
  applicationId,
}) {

  const [databases, setDatabases] =
    useState([]);

  const [showModal, setShowModal] =
    useState(false);

  const loadData = async () => {
    try {

      const data =
        await getDatabasesByApplication(
          applicationId
        );

      setDatabases(
        Array.isArray(data) ? data : []
      );

    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    loadData();
  }, [applicationId]);

  return (
    <div className="mt-6">

      <div className="flex justify-between mb-6">

        <h2 className="text-xl font-semibold">
          Databases
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
          Add Database
        </button>

      </div>

      <div className="grid gap-4">

        {databases.map((db) => (

          <div
            key={db.id}
            className="
              app-card
              border
              app-border
              rounded-2xl
              p-5
            "
          >
            <div className="flex justify-between">

              <div>

                <h3 className="font-semibold">
                  {db.name}
                </h3>

                <div className="text-sm app-muted mt-1">
                  {db.dbType}
                  {" • "}
                  {db.version}
                </div>

              </div>

              <Database
                className="text-cyan-400"
                size={22}
              />

            </div>

            <div className="mt-3 text-sm">
              Host: {db.host}
            </div>

            <div className="mt-1 text-sm">
              Port: {db.port}
            </div>

            <div className="mt-3 app-muted">
              {db.databaseContext}
            </div>

          </div>

        ))}

      </div>

      <AddDatabaseModal
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