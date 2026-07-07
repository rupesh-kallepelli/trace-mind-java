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
  data
}) {

  return (

    <div className="bg-gray-900 rounded-2xl p-6">

      <h3 className="mb-4 font-semibold">

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
            nameKey="severity">

            {
              data.map(
                (entry, index) => (

                <Cell
                  key={index}
                  fill={
                     COLORS[
                       index
                       % COLORS.length
                     ]
                  }
                />
              ))
            }

          </Pie>

          <Tooltip />

        </PieChart>

      </ResponsiveContainer>

    </div>
  );
}