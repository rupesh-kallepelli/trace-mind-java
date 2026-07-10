import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

import {
  AlertTriangle,
  Brain,
  CheckCircle,
  Calendar,
  Server,
  Target
} from "lucide-react";

import {
  getInvestigationDetail,
  getInvestigationEvents
} from "../services/investigationApi";

import InvestigationTimeline
  from "../components/InvestigationTimeline";

export default function InvestigationDetail() {

  const { id } = useParams();

  const [data, setData] =
    useState(null);

  const [events, setEvents] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  useEffect(() => {

    loadData();

  }, [id]);

  const loadData = async () => {

    try {

      const investigation =
        await getInvestigationDetail(id);

      setData(investigation);

      const history =
        await getInvestigationEvents(id);

      setEvents(history);

    } catch (error) {

      console.error(
        "Failed loading investigation",
        error
      );

    } finally {

      setLoading(false);

    }

  };

  useEffect(() => {

    if (!id) {
      return;
    }

    const eventSource =
      new EventSource(
        `${window.location.origin}/api/v1/investigations/${id}/stream`
      );

    eventSource.addEventListener(
      "investigation-event",
      async (event) => {

        const eventData =
          JSON.parse(event.data);

        setEvents(prev => {

          const exists =
            prev.some(
              e =>
                e.id &&
                e.id === eventData.id
            );

          if (exists) {
            return prev;
          }

          return [
            ...prev,
            eventData
          ];

        });

        if (
          eventData.eventType === "COMPLETED" ||
          eventData.eventType === "FAILED" ||
          eventData.eventType === "INVESTIGATION_COMPLETED"
        ) {

          try {

            const latest =
              await getInvestigationDetail(id);

            setData(latest);

          } catch (err) {

            console.error(err);

          }

        }

      }
    );

    return () => {

      eventSource.close();

    };

  }, [id]);

  const formatDate = (
    value
  ) => {

    if (!value) {
      return "N/A";
    }

    try {

      if (
        String(value).length > 15
      ) {

        return value;

      }

      return new Date(
        value
      ).toLocaleString();

    } catch {

      return value;

    }

  };

  if (loading) {

    return (
      <div className="p-6">
        Loading investigation...
      </div>
    );

  }

  if (!data) {

    return (
      <div className="p-6 text-red-500">
        Investigation not found
      </div>
    );

  }

  return (

    <div className="space-y-6">

      <div
        className="
          card-surface
          border
          border-surface
          rounded-2xl
          p-6
        "
      >

        <div className="flex items-center gap-4">

          <AlertTriangle
            className="text-slate-500"
            size={30}
          />

          <div>

            <h1 className="text-3xl font-bold">
              Investigation Details
            </h1>

            <div className="app-muted mt-1">
              Investigation ID: {data.id}
            </div>

          </div>

        </div>

      </div>

      <div className="grid md:grid-cols-3 gap-4">

        <InfoCard
          icon={<Server size={20} />}
          title="Service"
          value={
            data.serviceName ||
            "UNKNOWN"
          }
        />

        <InfoCard
          icon={<Target size={20} />}
          title="Namespace"
          value={
            data.namespace ||
            "-"
          }
        />

        <InfoCard
          icon={<CheckCircle size={20} />}
          title="Status"
          value={data.status}
        />

      </div>

      <div className="grid md:grid-cols-2 gap-6">

        <div
          className="
            card-surface
            border
            border-surface
            rounded-2xl
            p-6
          "
        >

          <h2
            className="
              font-semibold
              text-xl
              mb-4
            "
          >
            Issue Description
          </h2>

          <p className="app-text">
            {
              data.issueDescription
            }
          </p>

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

          <h2
            className="
              font-semibold
              text-xl
              mb-4
            "
          >
            Root Cause
          </h2>

          <p className="app-text">
            {
              data.rootCause ||
              "Generated From AI Investigation"
            }
          </p>

        </div>

      </div>

      <div className="grid md:grid-cols-3 gap-4">

        <InfoCard
          icon={<Brain size={20} />}
          title="Confidence"
          value={
            data.confidenceScore
              ? `${data.confidenceScore}%`
              : "N/A"
          }
        />

        <InfoCard
          icon={<Calendar size={20} />}
          title="Started At"
          value={formatDate(
            data.startedAt
          )}
        />

        <InfoCard
          icon={<Calendar size={20} />}
          title="Completed At"
          value={formatDate(
            data.completedAt
          )}
        />

      </div>

      <InvestigationTimeline
        events={events}
      />

      <div
        className="
          card-surface
          border
          border-surface
          rounded-2xl
          overflow-hidden
        "
      >

        <div
          className="
            border-b
            border-surface
            p-5
            flex
            items-center
            gap-3
          "
        >

          <Brain
            className="text-slate-500"
            size={24}
          />

          <div>

            <h2
              className="
                text-xl
                font-bold
              "
            >
              RCA Report
            </h2>

            <div
              className="
                text-sm
                app-muted
              "
            >
              AI Generated Investigation Report
            </div>

          </div>

        </div>

        <div className="p-8">

          <div
            className="
              prose
              max-w-none
              dark:prose-invert
            "
          >

            <ReactMarkdown
              remarkPlugins={[
                remarkGfm
              ]}
            >
              {
                data.reportMarkdown ||
                ""
              }
            </ReactMarkdown>

          </div>

        </div>

      </div>

    </div>

  );

}

function InfoCard({
  icon,
  title,
  value
}) {

  return (

    <div
      className="
        card-surface
        border
        border-surface
        rounded-xl
        p-5
      "
    >

      <div
        className="
          flex
          items-center
          gap-2
          text-slate-500
        "
      >

        {icon}

        <span className="font-medium">
          {title}
        </span>

      </div>

      <div
        className="
          mt-3
          break-words
          app-text
        "
      >
        {value}
      </div>

    </div>

  );

}