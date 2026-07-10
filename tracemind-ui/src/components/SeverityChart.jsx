import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer
} from "recharts";

const COLORS = [
  "#ef4444",
  "#f59e0b",
  "#3b82f6",
  "#22c55e"
];

export default function SeverityChart({
  data = []
}) {

  if (!data || data.length === 0) {

    return (

      <div>

        <h3
          className="
            mb-4
            font-semibold
          "
        >
          Severity Distribution
        </h3>

        <div className="app-muted">
          No data available
        </div>

      </div>

    );

  }

  return (

    <div>

      <h3
        className="
          mb-4
          font-semibold
        "
      >
        Severity Distribution
      </h3>

      <ResponsiveContainer
        width="100%"
        height={250}
      >

        <PieChart>

          <Pie
            data={data}
            dataKey="count"
            nameKey="severity"
            outerRadius={90}
            label
          >

            {data.map(
              (entry, index) => (

                <Cell
                  key={index}
                  fill={
                    COLORS[
                    index %
                    COLORS.length
                    ]
                  }
                />

              )
            )}

          </Pie>

          <Tooltip />

        </PieChart>

      </ResponsiveContainer>

    </div>

  );

}