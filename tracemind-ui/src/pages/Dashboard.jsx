import { useEffect, useState } from "react";

import {
  Loader2,
  Brain,
  AlertTriangle,
  Search
} from "lucide-react";

import {
  getDashboardOverview
} from "../services/dashboardApi";

import IncidentTrendChart
  from "../components/IncidentTrendChart";

import SeverityChart
  from "../components/SeverityChart";

import AIInsights
  from "../components/AIInsights";

export default function Dashboard() {

  const [data, setData] =
    useState(null);

  useEffect(() => {

    getDashboardOverview()
      .then(setData)
      .catch(console.error);

  }, []);

  if (!data) {

    return (

      <div
        className="
          flex
          items-center
          gap-3
          p-8
        "
      >

        <Loader2
          className="
            animate-spin
            text-green-400
          "
        />

        <span>
          Loading dashboard...
        </span>

      </div>

    );

  }

  return (

    <div className="space-y-8">

      <div>

        <h1
          className="
            text-4xl
            font-bold
          "
        >
          Dashboard
        </h1>

        <p
          className="
            mt-2
            app-muted
          "
        >
          AI powered observability and root cause analytics
        </p>

      </div>

      <div
        className="
          grid
          grid-cols-1
          md:grid-cols-3
          gap-5
        "
      >

        <Metric
          title="Investigations"
          value={
            data.totalInvestigations
          }
          icon={<Brain />}
          color="text-cyan-400"
        />

        <Metric
          title="Open Incidents"
          value={
            data.openIncidents
          }
          icon={<Search />}
          color="text-yellow-400"
        />

        <Metric
          title="Critical"
          value={
            data.criticalIncidents
          }
          icon={<AlertTriangle />}
          color="text-red-400"
        />

      </div>

      <div
        className="
          grid
          grid-cols-1
          lg:grid-cols-2
          gap-5
        "
      >

        <div
          className="
            card-surface
            border
            border-surface
            rounded-2xl
            p-5
          "
        >

          <IncidentTrendChart
            data={
              data.incidentTrend
            }
          />

        </div>

        <div
          className="
            card-surface
            border
            border-surface
            rounded-2xl
            p-5
          "
        >

          <SeverityChart
            data={
              data.severityDistribution
            }
          />

        </div>

      </div>

      <div
        className="
          card-surface
          border
          border-surface
          rounded-2xl
          p-6
        "
      >

        <AIInsights
          insights={
            data.aiInsights
          }
        />

      </div>

    </div>

  );

}

function Metric({
  title,
  value,
  icon,
  color = "text-green-400"
}) {

  return (

    <div
      className="
        card-surface
        border
        border-surface
        rounded-2xl
        p-6
        shadow-sm
      "
    >

      <div
        className="
          flex
          justify-between
          items-center
        "
      >

        <div
          className="
            app-muted
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
          text-4xl
          font-bold
          mt-3
        "
      >
        {value}
      </div>

    </div>

  );

}