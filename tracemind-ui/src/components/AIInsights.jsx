import {
  Sparkles
} from "lucide-react";

export default function AIInsights({
  insights
}) {

  return (

    <div className="bg-gray-900 rounded-2xl p-6">

      <div className="flex items-center gap-2 mb-5">

        <Sparkles
          className="text-green-500"
        />

        <h3 className="font-semibold">
          AI Insights
        </h3>

      </div>

      <div className="space-y-4">

        {
          insights?.map(
            (insight, index) => (

            <div
              key={index}
              className="text-gray-300">

              • {insight}

            </div>
          ))
        }

      </div>

    </div>
  );
}