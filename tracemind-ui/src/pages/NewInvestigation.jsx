import { useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

import {
  Brain,
  Search,
  Loader2
} from "lucide-react";

import {
  analyzeIssue,
  getInvestigationDetail
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

    console.log(
      "Opening SSE for",
      investigationId
    );

    const eventSource =
      new EventSource(
        `${window.location.origin}/api/v1/investigations/${investigationId}/stream`
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
              e => e.id === eventData.id
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
          "INVESTIGATION_COMPLETED"

          ||

          eventData.eventType ===
          "COMPLETED"

        ) {

          try {

            const investigation =
              await getInvestigationDetail(
                investigationId
              );

            setResponse(
              investigation.reportMarkdown ||
              investigation.report ||
              ""
            );

          } catch (error) {

            console.error(
              "Failed to load RCA",
              error
            );

          } finally {

            setLoading(false);

          }

        }

        if (

          eventData.eventType ===
          "INVESTIGATION_FAILED"

          ||

          eventData.eventType ===
          "FAILED"

        ) {

          setLoading(false);

        }

      }
    );

    eventSource.onerror =
      (error) => {

        console.error(
          "SSE ERROR",
          error
        );

      };

    return () => {

      console.log(
        "Closing SSE"
      );

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

        setInvestigationId(null);

        const result =
          await analyzeIssue(issue);

        console.log(
          "Investigation Started",
          result
        );

        setInvestigationId(
          result.investigationId
        );

      } catch (err) {

        console.error(err);

        setLoading(false);

        alert(
          "Investigation failed"
        );

      }

    };

  return (

    <div className="max-w-7xl mx-auto space-y-8">

      <div className="flex items-center gap-3">

        <Brain
          className="text-slate-500"
          size={32}
        />

        <div>

          <h1 className="text-4xl font-bold">
            AI Investigation
          </h1>

          <p className="app-muted">
            Agentic Root Cause Analysis Platform
          </p>

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

        <div className="text-sm app-muted mb-4">
          Describe the production issue
        </div>

        <textarea
          value={issue}
          onChange={(e) =>
            setIssue(
              e.target.value
            )
          }
          placeholder="Customers are not able to access the application after deployment..."
          className="
            w-full
            h-40
            card-surface
            border
            border-surface
            rounded-xl
            p-4
            app-text
            focus:outline-none
            focus:ring-2
            focus:ring-slate-400
          "
        />

        <div className="flex justify-end mt-6">

          <button
            onClick={
              handleInvestigate
            }
            disabled={loading}
            className="
              bg-white
              hover:bg-slate-100

              dark:bg-slate-800
              dark:hover:bg-slate-700

              border
              border-slate-300
              dark:border-slate-700

              text-slate-900
              dark:text-white

              disabled:opacity-50

              px-8
              py-3
              rounded-xl

              font-semibold

              flex
              items-center
              gap-2

              transition-all
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

      </div>

      {loading && (

        <div
          className="
            card-surface
            border
            border-surface
            rounded-2xl
            p-5
          "
        >

          <div
            className="
              flex
              items-center
              gap-3
            "
          >

            <Loader2
              className="
                animate-spin
                text-slate-500
              "
            />

            <div>

              <div className="font-semibold">
                AI Investigation Running
              </div>

              <div className="app-muted text-sm">
                Agents are collecting evidence and generating root cause analysis
              </div>

            </div>

          </div>

        </div>

      )}

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
              border
              border-surface
              bg-slate-100
              dark:bg-slate-800/40
              rounded-2xl
              p-6
            "
          >

            <div className="flex items-center gap-4">

              <Brain
                className="text-slate-500"
                size={28}
              />

              <div>

                <h2 className="text-2xl font-bold">
                  Root Cause Analysis Completed
                </h2>

                <p className="app-muted">
                  Multi-agent investigation completed successfully
                </p>

              </div>

            </div>

          </div>

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
                p-5
                border-b
                border-surface
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

                <h2 className="font-bold text-xl">
                  Investigation Report
                </h2>

                <p className="app-muted text-sm">
                  AI Generated RCA Report
                </p>

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