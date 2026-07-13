import { useEffect, useState } from "react";

import {
  AlertTriangle,
  ShieldAlert,
  CheckCircle
} from "lucide-react";

import {
  getIncidents
} from "../services/incidentApi";

export default function Incidents() {

  const [items, setItems] =
    useState([]);

  useEffect(() => {
    getIncidents()
      .then((data) => {
        console.log("INCIDENTS RESPONSE", data);

        setItems(
          Array.isArray(data)
            ? data
            : data.data || data.content || data.result || []
        );
      })
      .catch((error) => {
        console.error(error);
        setItems([]);
      });
  }, []);

  const severityColor =
    (severity) => {

      switch (severity) {

        case "CRITICAL":
          return "text-red-500";

        case "HIGH":
          return "text-orange-500";

        case "MEDIUM":
          return "text-yellow-500";

        default:
          return "text-blue-500";

      }

    };

  const statusColor =
    (status) => {

      switch (status) {

        case "OPEN":
          return `
            bg-red-500/10
            text-red-500
          `;

        case "IN_PROGRESS":
          return `
            bg-blue-500/10
            text-blue-500
          `;

        case "RESOLVED":
          return `
            bg-green-500/10
            text-green-500
          `;

        default:
          return `
            bg-slate-500/10
            text-slate-500
          `;

      }

    };

  return (

    <div className="space-y-6">

      <div>

        <h1
          className="
            text-4xl
            font-bold
          "
        >
          Incidents
        </h1>

        <p
          className="
            mt-2
            app-muted
          "
        >
          Active and historical incidents
        </p>

      </div>

      {items.length === 0 && (

        <div
          className="
            card-surface
            border
            border-surface
            rounded-2xl
            p-8
            text-center
            app-muted
          "
        >
          No incidents found
        </div>

      )}

      <div className="space-y-4">

        {items.map(item => (

          <div
            key={item.id}
            className="
              card-surface
              border
              border-surface
              rounded-2xl
              p-6
              hover:border-slate-400
              transition-all
            "
          >

            <div
              className="
                flex
                justify-between
                items-start
                gap-4
              "
            >

              <div>

                <div
                  className="
                    font-semibold
                    text-lg
                  "
                >
                  {
                    item.incidentNumber ||
                    item.id
                  }
                </div>

                <div
                  className="
                    mt-2
                    app-muted
                  "
                >
                  {
                    item.description ||
                    "No description available"
                  }
                </div>

              </div>

              <span
                className={`
                  px-3
                  py-1
                  rounded-full
                  text-sm
                  font-medium
                  ${statusColor(
                  item.status
                )}
                `}
              >
                {item.status}
              </span>

            </div>

            <div
              className="
                mt-5
                grid
                md:grid-cols-3
                gap-4
              "
            >

              <div>

                <div className="app-muted text-sm">
                  Severity
                </div>

                <div
                  className={`
                    mt-1
                    flex
                    items-center
                    gap-2
                    ${severityColor(
                    item.severity
                  )}
                  `}
                >

                  <ShieldAlert
                    size={16}
                  />

                  {item.severity}

                </div>

              </div>

              <div>

                <div className="app-muted text-sm">
                  Created
                </div>

                <div className="mt-1">
                  {item.createdAt
                    ? new Date(
                      item.createdAt
                    ).toLocaleString()
                    : "N/A"}
                </div>

              </div>

              <div>

                <div className="app-muted text-sm">
                  Investigation
                </div>

                <div className="mt-1 truncate">
                  {
                    item.investigationId ||
                    "-"
                  }
                </div>

              </div>

            </div>

          </div>

        ))}

      </div>

    </div>

  );

}