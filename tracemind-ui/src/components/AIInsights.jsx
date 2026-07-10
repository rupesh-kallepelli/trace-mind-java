import {
  Sparkles
} from "lucide-react";

export default function AIInsights({
  insights = []
}) {

  return (

    <div>

      <div
        className="
          flex
          items-center
          gap-2
          mb-5
        "
      >

        <Sparkles
          className="text-green-500"
        />

        <h3 className="font-semibold">
          AI Insights
        </h3>

      </div>

      {insights.length === 0 ? (

        <div className="app-muted">
          No insights available
        </div>

      ) : (

        <div className="space-y-4">

          {insights.map(
            (insight, index) => (

              <div
                key={index}
                className="
                  border-l-4
                  border-green-500
                  pl-4
                  app-text
                "
              >

                {insight}

              </div>

            )
          )}

        </div>

      )}

    </div>

  );

}