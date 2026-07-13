import { useEffect, useState } from "react";
import { Plus } from "lucide-react";
import { getApplications } from "../services/applicationService";
import ApplicationModal from "../components/ApplicationModal";
import ApplicationCard from "../components/ApplicationCard";

export default function ApplicationManagementPage() {
  const [applications, setApplications] = useState([]);
  const [showModal, setShowModal] = useState(false);

    const loadApplications = async () => {
      try {
        const data = await getApplications();

        setApplications(
          Array.isArray(data) ? data : []
        );
      } catch (error) {
        console.error(
          "Failed to load applications",
          error
        );

        setApplications([]);
      }
    };

  useEffect(() => {
    loadApplications();
  }, []);

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            Application Management
          </h1>

          <p className="text-gray-400">
            Manage application context and knowledge
          </p>
        </div>

        <button
          onClick={() => setShowModal(true)}
          className="bg-blue-600 px-4 py-2 rounded-lg flex gap-2 items-center"
        >
          <Plus size={18} />
          Add Application
        </button>
      </div>

      <div className="grid md:grid-cols-2 xl:grid-cols-3 gap-4">
        {applications.map((app) => (
          <ApplicationCard
            key={app.id}
            application={app}
          />
        ))}
      </div>

      <ApplicationModal
        open={showModal}
        onClose={() => setShowModal(false)}
        onSaved={loadApplications}
      />
    </div>
  );
}