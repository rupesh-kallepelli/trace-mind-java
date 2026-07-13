import {
  Boxes,
  Shield
} from "lucide-react";

export default function ApplicationHeader({
  application
}) {

  return (
    <div
      className="
        app-card
        rounded-2xl
        p-6
        border
        app-border
      "
    >
      <div className="flex justify-between">

        <div>
          <h1
            className="
              text-3xl
              font-bold
            "
          >
            {application.name}
          </h1>

          <div
            className="
              mt-2
              flex
              gap-3
            "
          >
            <span className="text-blue-400">
              {application.businessDomain}
            </span>

            <span className="app-muted">
              {application.criticality}
            </span>
          </div>
        </div>

        <Boxes
          size={32}
          className="text-blue-400"
        />
      </div>

      <p
        className="
          mt-4
          app-muted
        "
      >
        {application.applicationContext}
      </p>
    </div>
  );
}