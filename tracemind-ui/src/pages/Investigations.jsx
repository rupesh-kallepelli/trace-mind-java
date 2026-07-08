import { useEffect, useState } from "react";
import { getInvestigations } from "../services/investigationApi";
import { useNavigate } from "react-router-dom";

export default function Investigations() {

  const [items, setItems] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {

    loadInvestigations();

    const interval = setInterval(
      loadInvestigations,
      5000
    );

    return () => clearInterval(interval);

  }, []);

  const loadInvestigations = () => {

    getInvestigations()
      .then(data => setItems(data.content))
      .catch(console.error);

  };

  const getStatusColor = (status) => {

    switch (status) {

      case "COMPLETED":
        return "text-green-400";

      case "RUNNING":
        return "text-yellow-400";

      case "FAILED":
        return "text-red-400";

      default:
        return "text-gray-400";
    }
  };

  return (

    <div>

      <h1 className="text-3xl font-bold mb-6">
        Investigations
      </h1>

      <div className="space-y-4">

        {items.map(item => (

          <div
            key={item.id}
            onClick={() =>
              navigate(`/investigations/${item.id}`)
            }
            className="
              bg-gray-900
              rounded-xl
              p-5
              cursor-pointer
              hover:bg-gray-800
            "
          >

            <div className="font-semibold">
              {item.serviceName}
            </div>

            <div className={getStatusColor(item.status)}>
              {item.status}
            </div>

            <div className="text-xs text-gray-500 mt-2">
              ID: {item.id}
            </div>

          </div>

        ))}

      </div>

    </div>
  );
}