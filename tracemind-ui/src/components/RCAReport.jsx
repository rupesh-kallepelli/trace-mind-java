import {
  AlertTriangle,
  CheckCircle,
  Database,
  Activity,
  FileText,
  Network,
  Server
} from "lucide-react";

export default function RCAReport({ report }) {

  if (!report) {
    return null;
  }

  const lines = report.split("\n");

  return (
    <div className="space-y-6">

      <div className="bg-red-900/20 border border-red-500 rounded-2xl p-6">

        <div className="flex items-center gap-3">

          <AlertTriangle
            className="text-red-500"
            size={28}
          />

          <div>

            <h2 className="text-2xl font-bold">
              Critical RCA Identified
            </h2>

            <p className="text-gray-400">
              AI Generated Root Cause Analysis
            </p>

          </div>

        </div>

      </div>

      <div className="grid grid-cols-5 gap-4">

        <AgentCard
          icon={<Server size={20} />}
          title="Runtime"
          color="green"
        />

        <AgentCard
          icon={<Activity size={20} />}
          title="Metrics"
          color="blue"
        />

        <AgentCard
          icon={<Network size={20} />}
          title="Traces"
          color="purple"
        />

        <AgentCard
          icon={<FileText size={20} />}
          title="Logs"
          color="yellow"
        />

        <AgentCard
          icon={<Database size={20} />}
          title="Database"
          color="red"
        />

      </div>

      <div className="bg-gray-900 rounded-2xl border border-gray-700 p-6">

        <h2 className="text-xl font-bold text-green-400 mb-5">
          Investigation Report
        </h2>

        <div className="space-y-3">

          {lines.map((line, index) => {

            if (!line.trim()) {
              return null;
            }

            const headingPattern =
              /Issue|Namespace|Business Capability|Affected Service|Affected Component|Executive Summary|Investigation Timeline|Investigation Agents Invoked|Runtime Findings|Metrics Findings|Trace Findings|Database Findings|Root Cause|Recommendations/i;

            if (headingPattern.test(line)) {

              return (
                <div
                  key={index}
                  className="
                    text-green-400
                    text-lg
                    font-bold
                    mt-6
                  "
                >
                  {line}
                </div>
              );
            }

            return (
              <div
                key={index}
                className="
                  text-gray-200
                  leading-7
                "
              >
                {line}
              </div>
            );

          })}

        </div>

      </div>

    </div>
  );
}

function AgentCard({
  icon,
  title
}) {

  return (

    <div
      className="
      bg-gray-900
      border
      border-gray-700
      rounded-xl
      p-5
    "
    >

      <div
        className="
        flex
        justify-between
      "
      >

        {icon}

        <CheckCircle
          className="text-green-500"
          size={18}
        />

      </div>

      <div className="mt-3 font-medium">
        {title}
      </div>

    </div>
  );
}