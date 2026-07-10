import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid
} from "recharts";

export default function IncidentTrendChart({
  data = []
}) {

  return (

    <div>

      <h3
        className="
          mb-4
          font-semibold
        "
      >
        Incident Trend
      </h3>

      <ResponsiveContainer
        width="100%"
        height={250}
      >

        <LineChart data={data}>

          <CartesianGrid
            strokeDasharray="3 3"
            opacity={0.2}
          />

          <XAxis
            dataKey="day"
          />

          <YAxis />

          <Tooltip />

          <Line
            type="monotone"
            dataKey="incidents"
            stroke="#22c55e"
            strokeWidth={3}
          />

        </LineChart>

      </ResponsiveContainer>

    </div>

  );

}