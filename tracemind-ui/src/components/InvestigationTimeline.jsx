export default function InvestigationTimeline({
  events = []
}) {

  return (

    <div
      className="
        bg-slate-900
        border
        border-slate-700
        rounded-2xl
        p-6
      "
    >

      <h2
        className="
          text-xl
          font-bold
          mb-6
        "
      >
        Investigation Timeline
      </h2>

      {events.length === 0 && (

        <div className="text-gray-400">
          Waiting for events...
        </div>

      )}

      <div className="space-y-4">

        {events.map((event, index) => (

          <div
            key={event.id || index}
            className="
              border-l-4
              border-cyan-500
              pl-4
            "
          >

            <div
              className="
                flex
                justify-between
              "
            >

              <span
                className="
                  font-semibold
                  text-cyan-400
                "
              >
                {event.source}
              </span>

              <span
                className="
                  text-xs
                  text-gray-400
                "
              >
                {event.eventType}
              </span>

            </div>

            <div
              className="
                mt-2
                text-gray-300
              "
            >
              {event.message}
            </div>

            <div
              className="
                mt-2
                text-xs
                text-gray-500
              "
            >
              {event.createdAt}
            </div>

          </div>

        ))}

      </div>

    </div>

  );
}