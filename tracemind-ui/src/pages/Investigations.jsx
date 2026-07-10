import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  Activity,
  CheckCircle,
  AlertTriangle,
  Loader2,
  Search,
  Brain
} from "lucide-react";

import {
  getInvestigations
} from "../services/investigationApi";

export default function Investigations() {

  const [items, setItems] =
    useState([]);

  const [search, setSearch] =
    useState("");

  const [statusFilter,
    setStatusFilter] =
    useState("ALL");

  const navigate =
    useNavigate();

  useEffect(() => {

    loadInvestigations();

    const interval =
      setInterval(
        loadInvestigations,
        5000
      );

    return () =>
      clearInterval(interval);

  }, []);

  const loadInvestigations = () => {

    getInvestigations()
      .then(data => {

        setItems(
          data.content || []
        );

      })
      .catch(console.error);

  };

  const filteredItems =
    useMemo(() => {

      return items.filter(item => {

        const matchesSearch =

          (
            item.serviceName || ""
          )
            .toLowerCase()
            .includes(
              search.toLowerCase()
            ) ||

          (
            item.issueDescription ||
            ""
          )
            .toLowerCase()
            .includes(
              search.toLowerCase()
            );

        const matchesStatus =

          statusFilter === "ALL" ||

          item.status ===
          statusFilter;

        return (
          matchesSearch &&
          matchesStatus
        );

      });

    }, [
      items,
      search,
      statusFilter
    ]);

  const total =
    items.length;

  const completed =
    items.filter(
      i =>
        i.status ===
        "COMPLETED"
    ).length;

  const running =
    items.filter(
      i =>
        i.status ===
        "RUNNING"
    ).length;

  const failed =
    items.filter(
      i =>
        i.status ===
        "FAILED"
    ).length;

  const statusBadge =
    (status) => {

      switch (status) {

        case "COMPLETED":

          return `
            bg-green-500/20
            text-green-400
            border-green-500/30
          `;

        case "RUNNING":

          return `
            bg-cyan-500/20
            text-cyan-400
            border-cyan-500/30
          `;

        case "FAILED":

          return `
            bg-red-500/20
            text-red-400
            border-red-500/30
          `;

        default:

          return `
            bg-gray-500/20
            text-gray-400
            border-gray-500/30
          `;

      }

    };

  return (

    <div className="space-y-8">

      <div>

        <h1
          className="
            text-4xl
            font-bold
          "
        >
          Investigations
        </h1>

        <p
          className="
            text-gray-400
            mt-2
          "
        >
          Browse and monitor
          AI powered RCA
          investigations
        </p>

      </div>

      <div
        className="
          grid
          grid-cols-1
          md:grid-cols-4
          gap-4
        "
      >

        <StatCard
          title="Total"
          value={total}
          icon={
            <Brain size={22} />
          }
        />

        <StatCard
          title="Running"
          value={running}
          icon={
            <Loader2
              size={22}
            />
          }
          color="text-cyan-400"
        />

        <StatCard
          title="Completed"
          value={completed}
          icon={
            <CheckCircle
              size={22}
            />
          }
          color="text-green-400"
        />

        <StatCard
          title="Failed"
          value={failed}
          icon={
            <AlertTriangle
              size={22}
            />
          }
          color="text-red-400"
        />

      </div>

      <div
        className="
          flex
          flex-col
          md:flex-row
          gap-4
        "
      >

        <div className="relative flex-1">

          <Search
            size={18}
            className="
              absolute
              left-3
              top-3
              text-gray-500
            "
          />

          <input
            value={search}
            onChange={(e) =>
              setSearch(e.target.value)
            }
            placeholder="Search investigations..."
            className="
                w-full
                card-surface
                border
                border-surface
                rounded-xl
                pl-10
                pr-4
                py-3
                app-text
              "
          />

        </div>

        <select
          value={statusFilter}
          onChange={(e) =>
            setStatusFilter(
              e.target.value
            )
          }
          className="
              card-surface
              border
              border-surface
              rounded-xl
              px-4
              py-3
              app-text
            "
        >

          <option value="ALL">
            All Statuses
          </option>

          <option value="RUNNING">
            Running
          </option>

          <option value="COMPLETED">
            Completed
          </option>

          <option value="FAILED">
            Failed
          </option>

        </select>

      </div>

      <div className="space-y-4">

        {filteredItems.length === 0 && (
          <div
            className="
                card-surface
                border
                border-surface
                rounded-xl
                p-8
                text-center
                app-muted
              "
          >
            No investigations found
          </div>

        )}

        {filteredItems.map(item => (

          <div
            key={item.id}
            onClick={() =>
              navigate(
                `/investigations/${item.id}`
              )
            }
            className="
                card-surface
                border
                border-surface
                rounded-2xl
                p-6
                cursor-pointer
                hover:border-green-500
                hover:shadow-lg
                hover:shadow-green-500/10
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

              <div className="flex-1">

                <div
                  className="
                    text-xl
                    font-semibold
                  "
                >
                  {item.serviceName ||
                    "Unknown Service"}
                </div>

                <div
                  className="
                    mt-2
                    text-gray-400
                  "
                >
                  {
                    item.issueDescription ||
                    "No issue description available"
                  }
                </div>

              </div>

              <span
                className={`
                  px-3
                  py-1
                  rounded-full
                  border
                  text-sm
                  font-medium
                  ${statusBadge(
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
                md:grid-cols-4
                gap-4
                text-sm
              "
            >

              <div>

                <div className="text-gray-500">
                  Namespace
                </div>

                <div className="mt-1">
                  {item.namespace ||
                    "-"}
                </div>

              </div>

              <div>

                <div className="text-gray-500">
                  Confidence
                </div>

                <div className="mt-1">
                  {item.confidenceScore
                    ? `${item.confidenceScore}%`
                    : "N/A"}
                </div>

              </div>

              <div>

                <div className="text-gray-500">
                  Root Cause
                </div>

                <div
                  className="
                    truncate
                  "
                >
                  {item.rootCause ||
                    "Pending"}
                </div>

              </div>

              <div>

                <div className="text-gray-500">
                  Started
                </div>

                <div className="mt-1">
                  {item.startedAt
                    ? new Date(
                      item.startedAt
                    ).toLocaleString()
                    : "N/A"}
                </div>

              </div>

            </div>

            <div
              className="
                mt-4
                text-xs
                text-gray-500
              "
            >
              Investigation ID:
              {" "}
              {item.id}
            </div>

          </div>

        ))}

      </div>

    </div>

  );

}

function StatCard({
  title,
  value,
  icon,
  color = "text-green-400"
}) {

  return (

    <div
      className="
  w-full
  card-surface
  border
  border-surface
  rounded-xl
  pl-10
  pr-4
  py-3
"
    >

      <div
        className="
          flex
          items-center
          justify-between
        "
      >

        <div
          className="
            text-gray-400
            text-sm
          "
        >
          {title}
        </div>

        <div className={color}>
          {icon}
        </div>

      </div>

      <div
        className="
          text-3xl
          font-bold
          mt-3
        "
      >
        {value}
      </div>

    </div>

  );

}