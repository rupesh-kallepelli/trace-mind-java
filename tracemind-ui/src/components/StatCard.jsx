function StatCard({
  title,
  value,
  icon,
  color = "text-green-400"
}) {

  return (

    <div
      className="
        card-surface
        border
        border-surface
        rounded-2xl
        p-5
        transition-all
      "
    >

      <div
        className="
          flex
          items-center
          justify-between
        "
      >

        <div
          className="
            app-muted
            text-sm
          "
        >
          {title}
        </div>

        <div className={color}>
          {icon}
        </div>

      </div>

      <div
        className="
          text-3xl
          font-bold
          mt-3
        "
      >
        {value}
      </div>

    </div>

  );

}