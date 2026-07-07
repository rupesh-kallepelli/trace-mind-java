import {
  CheckCircle
} from "lucide-react";

export default function AgentTimeline({
  evidence
}) {

  return (

    <div
      className="
      bg-gray-900
      rounded-2xl
      p-6
    ">

      <h3
        className="
        font-semibold
        mb-5
      ">

        Agent Analysis

      </h3>

      {
        evidence.map(item => (

          <div
            key={item.id}
            className="
            flex
            gap-3
            mb-4
          ">

            <CheckCircle
              className="
              text-green-500
            "
            />

            <div>

              <div
                className="
                font-medium
              ">

                {item.agentType}

              </div>

              <div
                className="
                text-gray-400
              ">

                {item.summary}

              </div>

            </div>

          </div>

        ))
      }

    </div>
  );
}