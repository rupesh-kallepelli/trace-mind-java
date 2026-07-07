export default function Header() {

  return (
    <div className="h-16 border-b border-[#1f2937] px-8 flex items-center justify-between">

      <h2 className="text-xl font-semibold">
        ObserveAI
      </h2>

      <input
        placeholder="Search investigations..."
        className="bg-[#111827] px-4 py-2 rounded-lg border border-[#1f2937] outline-none w-96"
      />

    </div>
  );
}