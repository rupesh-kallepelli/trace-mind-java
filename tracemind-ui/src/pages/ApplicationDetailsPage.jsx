import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import {
  Server,
  Database,
  Table2,
  FileCode
} from "lucide-react";

import {
  getApplicationContext
} from "../services/applicationService";

import ApplicationTabs from "../components/ApplicationTabs";
import MicroservicesTab from "../components/MicroservicesTab";
import DatabasesTab from "../components/DatabasesTab";

export default function ApplicationDetailsPage() {

  const { id } = useParams();

  const [activeTab, setActiveTab] =
    useState("Overview");

  const [context, setContext] =
    useState(null);

  useEffect(() => {
    loadData();
  }, [id]);

  const loadData = async () => {

    try {

      const data =
        await getApplicationContext(id);

      setContext(data);

    } catch (error) {
      console.error(error);
    }
  };

  if (!context) {
    return (
      <div className="p-6">
        Loading...
      </div>
    );
  }

  const app = context.application;

  return (
    <div className="space-y-6">

      {/* Header */}

      <div className="app-card p-6 rounded-3xl">

        <h1 className="text-4xl font-bold">
          {app.name}
        </h1>

        <div className="flex gap-3 mt-3">

          <span className="text-cyan-400">
            {app.businessDomain}
          </span>

          <span className="text-slate-400">
            {app.criticality}
          </span>

        </div>

        <p className="mt-4 app-muted">
          {app.applicationContext}
        </p>
      </div>

      {/* Metrics */}

      <div className="grid md:grid-cols-4 gap-4">

        <MetricCard
          icon={Server}
          label="Services"
          value={
            context.microservices?.length || 0
          }
        />

        <MetricCard
          icon={Database}
          label="Databases"
          value={
            context.databases?.length || 0
          }
        />

        <MetricCard
          icon={Table2}
          label="Tables"
          value={
            context.tables?.length || 0
          }
        />

        <MetricCard
          icon={FileCode}
          label="APIs"
          value={
            context.apis?.length || 0
          }
        />

      </div>

      <ApplicationTabs
        activeTab={activeTab}
        setActiveTab={setActiveTab}
      />

      {activeTab === "Overview" && (

        <div className="app-card p-6 rounded-3xl">

          <h2 className="text-xl font-semibold">
            Application Overview
          </h2>

          <div className="mt-4 app-muted">
            {app.description}
          </div>

        </div>

      )}

      {activeTab === "Microservices" && (

        <MicroservicesTab
          applicationId={id}
        />

      )}

        {activeTab === "Databases" && (
          <DatabasesTab
            applicationId={id}
          />
        )}

    </div>
  );
}

function MetricCard({
  icon: Icon,
  label,
  value
}) {

  return (
    <div
      className="
        app-card
        rounded-2xl
        p-5
      "
    >
      <div className="flex justify-between">

        <span>{label}</span>

        <Icon
          size={20}
          className="text-cyan-400"
        />

      </div>

      <div
        className="
          text-3xl
          font-bold
          mt-4
        "
      >
        {value}
      </div>
    </div>
  );
}