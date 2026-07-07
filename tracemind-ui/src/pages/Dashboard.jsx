import {
  useEffect,
  useState
} from "react";

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
          .then(setData);

  }, []);

  if (!data) {
     return <div>Loading...</div>;
  }

  return (

    <div>

      <div
        className="
        grid
        grid-cols-3
        gap-5
      ">

        <Metric
            title="Investigations"
            value={
               data.totalInvestigations
            }
        />

        <Metric
            title="Open Incidents"
            value={
               data.openIncidents
            }
        />

        <Metric
            title="Critical"
            value={
               data.criticalIncidents
            }
        />

      </div>

      <div
        className="
          grid
          grid-cols-2
          gap-5
          mt-6
        ">

         <IncidentTrendChart
             data={
                 data.incidentTrend
             }
         />

         <SeverityChart
             data={
                 data.severityDistribution
             }
         />

      </div>

      <div className="mt-6">

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
  value
}) {

  return (

    <div
      className="
      p-6
      rounded-2xl
      bg-gray-900
    ">

      <div
        className="
        text-gray-400
      ">

        {title}

      </div>

      <div
        className="
        text-4xl
        font-bold
        mt-2
      ">

        {value}

      </div>

    </div>
  );
}