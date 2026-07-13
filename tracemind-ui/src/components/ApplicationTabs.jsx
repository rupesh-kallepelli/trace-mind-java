export default function ApplicationTabs({
  activeTab,
  setActiveTab
}) {

  const tabs = [
    "Overview",
    "Microservices",
    "Databases"
  ];

  return (
    <div className="flex gap-2 mt-6">

      {tabs.map((tab) => (

        <button
          key={tab}
          onClick={() =>
            setActiveTab(tab)
          }
          className={`
            px-4 py-2 rounded-xl

            ${
              activeTab === tab
                ? "bg-blue-600 text-white"
                : "bg-slate-800 text-slate-300"
            }
          `}
        >
          {tab}
        </button>
      ))}
    </div>
  );
}