import { useNavigate } from "react-router-dom";
import {
  Boxes,
  Shield,
  ChevronRight
} from "lucide-react";

export default function ApplicationCard({
  application,
}) {

  const navigate = useNavigate();

  return (
    <div
      className="
        app-card
        border
        app-border
        rounded-2xl
        p-5
        hover:border-blue-400
        transition-all
        cursor-pointer
      "
      onClick={() =>
        navigate(`/applications/${application.id}`)
      }
    >
      <div className="flex items-start justify-between">

        <div>
          <h3 className="text-lg font-semibold">
            {application.name}
          </h3>

          <p
            className="
              text-sm
              app-muted
              mt-1
            "
          >
            {application.businessDomain}
          </p>
        </div>

        <Boxes
          size={20}
          className="text-blue-400"
        />
      </div>

      <div className="mt-4">
        <span
          className="
            px-2 py-1
            rounded-lg
            bg-blue-500/10
            text-blue-400
            text-xs
          "
        >
          {application.criticality}
        </span>
      </div>

      <p
        className="
          mt-4
          text-sm
          app-muted
          line-clamp-3
        "
      >
        {application.description}
      </p>

      <div className="mt-5 flex justify-end">
        <ChevronRight size={18} />
      </div>
    </div>
  );
}