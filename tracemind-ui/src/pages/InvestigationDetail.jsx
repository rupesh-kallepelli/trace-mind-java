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

        console.log(
          "SSE EVENT",
          eventData
        );

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
          eventData.eventType ===
            "COMPLETED" ||
          eventData.eventType ===
            "FAILED"
        ) {

          try {

            const latest =
              await getInvestigationDetail(
                id
              );

            setData(latest);

          } catch (err) {

            console.error(err);

          }

        }

      }
    );

    eventSource.onerror = (error) => {

      console.error(
        "SSE Connection Error",
        error
      );

    };

    return () => {

      eventSource.close();

    };

  }, [id]);

  if (loading) {

    return (
      <div className="p-6">
        Loading investigation...
      </div>
    );

  }

  if (!data) {

    return (
      <div className="p-6 text-red-400">
        Investigation not found
      </div>
    );

  }

  return (

    <div className="space-y-6">

      <div
        className="
          bg-gradient-to-r
          from-red-950
          to-red-900/20
          border
          border-red-500
          rounded-2xl
          p-6
        "
      >

        <div className="flex items-center gap-4">

          <AlertTriangle
            className="text-red-400"
            size={32}
          />

          <div>

            <h1 className="text-3xl font-bold">
              Investigation Details
            </h1>

            <div className="text-gray-300 mt-1">
              Investigation ID: {data.id}
            </div>

          </div>

        </div>

      </div>

      <div className="grid md:grid-cols-3 gap-4">

        <InfoCard
          icon={<Server size={20} />}
          title="Service"
          value={data.serviceName}
        />

        <InfoCard
          icon={<Target size={20} />}
          title="Namespace"
          value={data.namespace}
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
            bg-slate-900
            border
            border-slate-700
            rounded-2xl
            p-6
          "
        >

          <h2 className="font-semibold text-xl mb-4">
            Issue Description
          </h2>

          <p className="text-gray-300">
            {data.issueDescription}
          </p>

        </div>

        <div
          className="
            bg-slate-900
            border
            border-slate-700
            rounded-2xl
            p-6
          "
        >

          <h2 className="font-semibold text-xl mb-4">
            Root Cause
          </h2>

          <p className="text-gray-300">
            {data.rootCause || "Not Available"}
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
          value={data.startedAt || "N/A"}
        />

        <InfoCard
          icon={<Calendar size={20} />}
          title="Completed At"
          value={
            data.completedAt ||
            "In Progress"
          }
        />

      </div>

      <InvestigationTimeline
        events={events}
      />

      <div
        className="
          bg-slate-900
          border
          border-slate-700
          rounded-2xl
          overflow-hidden
        "
      >

        <div
          className="
            border-b
            border-slate-700
            p-5
            flex
            items-center
            gap-3
          "
        >

          <Brain
            className="text-green-400"
            size={24}
          />

          <div>

            <h2 className="text-xl font-bold">
              RCA Report
            </h2>

            <div className="text-sm text-gray-400">
              AI Generated Investigation Report
            </div>

          </div>

        </div>

        <div className="p-8">

          <div
            className="
              prose
              prose-invert
              max-w-none
              prose-headings:text-green-400
              prose-strong:text-white
              prose-p:text-gray-300
              prose-li:text-gray-300
            "
          >

            <ReactMarkdown
              remarkPlugins={[remarkGfm]}
            >
              {data.reportMarkdown || ""}
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
        bg-slate-900
        border
        border-slate-700
        rounded-xl
        p-5
      "
    >

      <div
        className="
          flex
          items-center
          gap-2
          text-green-400
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
          text-white
          break-words
        "
      >
        {value}
      </div>

    </div>

  );

}