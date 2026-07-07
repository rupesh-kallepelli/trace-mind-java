import { useEffect, useState }
from "react";

import {
  getIncidents
}
from "../services/incidentApi";

export default function Incidents() {

  const [items, setItems] =
    useState([]);

  useEffect(() => {

    getIncidents()
      .then(setItems);

  }, []);

  return (

    <div>

      <h1 className="text-3xl font-bold mb-6">
        Incidents
      </h1>

      <div className="space-y-4">

        {items.map(item => (

          <div
            key={item.id}
            className="bg-gray-900 rounded-xl p-5">

            <div>
              {item.incidentNumber}
            </div>

            <div>
              Severity:
              {item.severity}
            </div>

            <div>
              Status:
              {item.status}
            </div>

          </div>

        ))}

      </div>

    </div>
  );
}