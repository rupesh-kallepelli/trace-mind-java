import ThemeToggle from "./ThemeToggle";

export default function Header() {

  return (

    <div
      className="
        h-16
        border-b
        app-border
        px-8
        flex
        items-center
        justify-between
      "
    >

      <h2
        className="
          text-xl
          font-semibold
        "
      >
        ObserveAI
      </h2>

      <input
        placeholder="Search investigations..."
        className="
          app-card
          border
          app-border
          px-4
          py-2
          rounded-lg
          outline-none
          w-96
        "
      />

      <ThemeToggle />

    </div>

  );

}