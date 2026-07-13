import {
  Server,
  Database,
  Table2
} from "lucide-react";

export default function OverviewTab({
  microservices = [],
  databases = [],
  tables = []
}) {

  const cards = [
    {
      label: "Microservices",
      value: microservices.length,
      icon: Server,
    },
    {
      label: "Databases",
      value: databases.length,
      icon: Database,
    },
    {
      label: "Tables",
      value: tables.length,
      icon: Table2,
    },
  ];

  return (
    <div className="grid md:grid-cols-3 gap-4 mt-6">

      {cards.map((card) => {

        const Icon = card.icon;

        return (
          <div
            key={card.label}
            className="
              app-card
              rounded-2xl
              p-5
              border
              app-border
            "
          >
            <div className="flex justify-between">
              <span>{card.label}</span>

              <Icon
                size={20}
                className="text-blue-400"
              />
            </div>

            <h2
              className="
                text-3xl
                font-bold
                mt-4
              "
            >
              {card.value}
            </h2>
          </div>
        );
      })}
    </div>
  );
}