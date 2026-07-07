export default function StatCard({
  title,
  value,
  color
}) {

  return (
    <div className="bg-[#111827] rounded-2xl border border-[#1f2937] p-5">

      <div className="text-gray-400 text-sm">
        {title}
      </div>

      <div
        className={`text-3xl font-bold mt-3 ${color}`}>
        {value}
      </div>

    </div>
  );
}