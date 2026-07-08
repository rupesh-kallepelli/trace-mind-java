import { useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

import {
  Brain,
  AlertTriangle,
  Search,
  Loader2
} from "lucide-react";

import {
  analyzeIssue
} from "../services/investigationApi";

import InvestigationTimeline
  from "../components/InvestigationTimeline";

export default function NewInvestigation() {

  const [issue, setIssue] =
    useState("");

  const [loading, setLoading] =
    useState(false);

  const [response, setResponse] =
    useState("");

  const [events, setEvents] =
    useState([]);

  const [investigationId,
          setInvestigationId] =
    useState(null);

  useEffect(() => {

    if (!investigationId) {
      return;
    }

    const eventSource =
      new EventSource(
        `${window.location.origin}/api/v1/investigations/${investigationId}/stream`
      );

    eventSource.addEventListener(
      "investigation-event",
      (event) => {

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

      }
    );

    eventSource.onerror = (error) => {

      console.error(
        "SSE ERROR",
        error
      );

    };

    return () => {

      eventSource.close();

    };

  }, [investigationId]);

  const handleInvestigate =
    async () => {

      if (!issue.trim()) {
        return;
      }

      try {

        setLoading(true);

        setResponse("");

        setEvents([]);

        const result =
          await analyzeIssue(issue);

        if (
          result.investigationId
        ) {

          setInvestigationId(
            result.investigationId
          );

        }

        if (result.report) {

          setResponse(
            result.report
          );

        } else if (
          typeof result === "string"
        ) {

          setResponse(result);

        }

      } catch (err) {

        console.error(err);

        alert(
          "Investigation failed"
        );

      } finally {

        setLoading(false);

      }
    };

  return (

    <div className="max-w-7xl mx-auto space-y-8">

      <div className="flex items-center gap-3">

        <Brain
          className="text-green-400"
          size={32}
        />

        <div>

          <h1 className="text-4xl font-bold">
            AI Investigation
          </h1>

          <p className="text-gray-400">
            Agentic Root Cause Analysis Platform
          </p>

        </div>

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

        <div className="text-sm text-gray-400 mb-4">
          Describe the production issue
        </div>

        <textarea
          value={issue}
          onChange={(e) =>
            setIssue(e.target.value)
          }
          placeholder="Customers are not listing..."
          className="
            w-full
            h-40
            bg-slate-950
            border
            border-slate-700
            rounded-xl
            p-4
            text-white
            focus:outline-none
            focus:ring-2
            focus:ring-green-500
          "
        />

        <button
          onClick={
            handleInvestigate
          }
          disabled={loading}
          className="
            mt-6
            bg-green-500
            hover:bg-green-400
            disabled:opacity-50
            text-black
            px-8
            py-3
            rounded-xl
            font-semibold
            flex
            items-center
            gap-2
          "
        >

          {loading
            ? (
              <Loader2
                className="animate-spin"
                size={18}
              />
            )
            : (
              <Search size={18} />
            )}

          {loading
            ? "Investigating..."
            : "Investigate"}

        </button>

      </div>

      {(loading ||
        events.length > 0) && (

        <InvestigationTimeline
          events={events}
        />

      )}

      {response && (

        <div className="space-y-6">

          <div
            className="
              bg-gradient-to-r
              from-red-950
              via-red-900/30
              to-transparent
              border
              border-red-500
              rounded-2xl
              p-6
            "
          >

            <div className="flex items-center gap-4">

              <AlertTriangle
                className="text-red-400"
                size={30}
              />

              <div>

                <h2 className="text-2xl font-bold">
                  Root Cause Analysis Completed
                </h2>

                <p className="text-gray-300">
                  Multi-agent investigation
                  finished successfully
                </p>

              </div>

            </div>

          </div>

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
                p-5
                border-b
                border-slate-700
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

                <h2 className="font-bold text-xl">
                  Investigation Report
                </h2>

                <p className="text-sm text-gray-400">
                  AI Generated RCA Report
                </p>

              </div>

            </div>

            <div className="p-8">

              <div
                className="
                  prose
                  prose-invert
                  max-w-none
                  prose-headings:text-green-400
                  prose-p:text-gray-300
                  prose-li:text-gray-300
                  prose-strong:text-white
                "
              >

                <ReactMarkdown
                  remarkPlugins={[
                    remarkGfm
                  ]}
                >
                  {response}
                </ReactMarkdown>

              </div>

            </div>

          </div>

        </div>

      )}

    </div>

  );

}