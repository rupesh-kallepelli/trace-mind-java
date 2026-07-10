import { useMemo, useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

import {
  ChevronDown,
  ChevronRight,
  Loader2,
  CheckCircle,
  AlertCircle
} from "lucide-react";

const AGENT_ORDER = [
  "ORCHESTRATOR",
  "RUNTIME_AGENT",
  "METRICS_AGENT",
  "TRACE_AGENT",
  "LOG_AGENT",
  "DATABASE_AGENT",
  "CORRELATION_ENGINE"
];

function ThinkingDots() {
  return (
    <div className="flex gap-1">
      <div className="h-2 w-2 rounded-full bg-blue-500 animate-bounce" />
      <div className="h-2 w-2 rounded-full bg-blue-500 animate-bounce [animation-delay:200ms]" />
      <div className="h-2 w-2 rounded-full bg-blue-500 animate-bounce [animation-delay:400ms]" />
    </div>
  );
}

export default function InvestigationTimeline({
  events = []
}) {

  const [collapsed, setCollapsed] =
    useState(false);

  const [expandedAgents,
    setExpandedAgents] =
    useState({});

  const agentGroups =
    useMemo(() => {

      const grouped = {};

      events.forEach(event => {

        const source =
          event.source || "UNKNOWN";

        if (!grouped[source]) {
          grouped[source] = [];
        }

        grouped[source].push(event);

      });

      return grouped;

    }, [events]);

  const agents =
    Object.keys(agentGroups).sort((a, b) => {

      const aIndex =
        AGENT_ORDER.indexOf(a);

      const bIndex =
        AGENT_ORDER.indexOf(b);

      if (aIndex === -1 && bIndex === -1) {
        return a.localeCompare(b);
      }

      if (aIndex === -1) {
        return 1;
      }

      if (bIndex === -1) {
        return -1;
      }

      return aIndex - bIndex;

    });

  const visibleAgents =
    agents.filter(
      a => a !== "ORCHESTRATOR"
    );

  const completedAgents =
    visibleAgents.filter(agent =>
      agentGroups[agent].some(
        e =>
          [
            "AGENT_COMPLETED",
            "COMPLETED"
          ].includes(e.eventType)
      )
    ).length;

  const progress =
    visibleAgents.length === 0
      ? 0
      : Math.round(
        (
          completedAgents /
          visibleAgents.length
        ) * 100
      );

  const toggleAgent =
    (agent) => {

      setExpandedAgents(prev => ({
        ...prev,
        [agent]:!prev[agent]
      }));

};

const investigationCompleted =
  events.some(
    e =>
      [
        "INVESTIGATION_COMPLETED",
        "COMPLETED"
      ].includes(e.eventType)
  );

return (

  <div
    className="
        card-surface
        border
        border-surface
        rounded-2xl
        overflow-hidden
      "
  >

    <button
      onClick={() =>
        setCollapsed(!collapsed)
      }
      className="
          w-full
          p-6
          flex
          justify-between
          items-center
        "
    >

      <div className="text-left">

        <h2
          className="
              text-xl
              font-bold
            "
        >
          Investigation Timeline
        </h2>

        <div
          className="
              app-muted
              text-sm
              mt-1
            "
        >
          Progress {progress}%
        </div>

      </div>

      {collapsed
        ? <ChevronRight />
        : <ChevronDown />}

    </button>

    {!collapsed && (

      <div className="px-6 pb-6">

        <div
          className="
              h-3
              rounded-full
              overflow-hidden
              bg-slate-200
              dark:bg-slate-800
              mb-6
            "
        >

          <div
            className="
                h-full
                bg-blue-500
                transition-all
                duration-700
              "
            style={{
              width: `${progress}%`
            }}
          />

        </div>

        {events.length === 0 && (

          <div className="app-muted">
            Waiting for events...
          </div>

        )}

        <div className="space-y-4">

          {agents.map(agent => {

            const agentEvents =
              agentGroups[agent];

            const completed =
              agentEvents.some(
                e =>
                  [
                    "AGENT_COMPLETED",
                    "COMPLETED"
                  ].includes(
                    e.eventType
                  )
              );

            const failed =
              agentEvents.some(
                e =>
                  [
                    "AGENT_FAILED",
                    "FAILED"
                  ].includes(
                    e.eventType
                  )
              );

            const running =
              !completed &&
              !failed;

            const expanded =
              expandedAgents[agent] ??
              running;

            return (

              <div
                key={agent}
                className="
                    border
                    border-surface
                    rounded-xl
                    overflow-hidden
                  "
              >

                <button
                  onClick={() =>
                    toggleAgent(agent)
                  }
                  className="
                      w-full
                      p-4
                      flex
                      justify-between
                      items-center
                    "
                >

                  <div
                    className="
                        flex
                        items-center
                        gap-3
                      "
                  >

                    {expanded
                      ? <ChevronDown size={16} />
                      : <ChevronRight size={16} />}

                    {completed ? (

                      <CheckCircle
                        size={18}
                        className="text-green-500"
                      />

                    ) : failed ? (

                      <AlertCircle
                        size={18}
                        className="text-red-500"
                      />

                    ) : (

                      <Loader2
                        size={18}
                        className="
                            animate-spin
                            text-blue-500
                          "
                      />

                    )}

                    <span
                      className="
                          font-semibold
                          text-blue-500
                        "
                    >
                      {agent.replaceAll(
                        "_",
                        " "
                      )}
                    </span>

                  </div>

                  {running &&
                    <ThinkingDots />}

                </button>

                {expanded && (

                  <div
                    className="
                        border-t
                        border-surface
                        p-4
                        space-y-4
                      "
                  >

                    {agentEvents.map(
                      (
                        event,
                        index
                      ) => (

                        <div
                          key={
                            event.id ||
                            `${agent}-${index}`
                          }
                          className="
                              border-l-2
                              border-blue-500
                              pl-4
                            "
                        >

                          <div
                            className="
                                flex
                                justify-between
                                gap-4
                              "
                          >

                            <div>
                              {event.message}
                            </div>

                            <div
                              className="
                                  text-xs
                                  app-muted
                                "
                            >
                              {event.eventType}
                            </div>

                          </div>

                          {event.payload &&
                            event.payload !== "{}" && (

                              <div
                                className="
                                  mt-3
                                  rounded-lg
                                  border
                                  border-surface
                                  p-4
                                "
                              >

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
                                      typeof event.payload === "string"
                                        ? event.payload
                                        : JSON.stringify(
                                          event.payload,
                                          null,
                                          2
                                        )
                                    }
                                  </ReactMarkdown>

                                </div>

                              </div>

                            )}

                          <div
                            className="
                                mt-2
                                text-xs
                                app-muted
                              "
                          >
                            {event.createdAt}
                          </div>

                        </div>

                      )
                    )}

                  </div>

                )}

              </div>

            );

          })}

        </div>

        {investigationCompleted && (

          <div
            className="
                mt-6
                border
                border-surface
                bg-slate-100
                dark:bg-slate-800/40
                rounded-xl
                p-4
              "
          >

            <div
              className="
                  flex
                  items-center
                  gap-3
                "
            >

              <CheckCircle
                className="text-green-500"
              />

              <div>

                <div className="font-semibold">
                  Root Cause Identified
                </div>

                <div
                  className="
                      text-sm
                      app-muted
                    "
                >
                  Investigation completed successfully
                </div>

              </div>

            </div>

          </div>

        )}

      </div>

    )}

  </div>

);

}