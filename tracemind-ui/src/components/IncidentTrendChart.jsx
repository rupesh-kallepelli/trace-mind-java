import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer
} from "recharts";

export default function IncidentTrendChart({
  data
}) {

  return (

    <div className="bg-gray-900 rounded-2xl p-6">

      <h3 className="mb-4 font-semibold">
        Incident Trend
      </h3>

      <ResponsiveContainer
        width="100%"
        height={250}
      >

        <LineChart data={data}>

          <XAxis dataKey="day" />

          <YAxis />

          <Tooltip />

          <Line
            type="monotone"
            dataKey="incidents"
            stroke="#22c55e"
          />

        </LineChart>

      </ResponsiveContainer>

    </div>
  );
}