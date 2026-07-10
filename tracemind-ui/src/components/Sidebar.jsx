import {
  AlertTriangle,
  Brain,
  LayoutDashboard,
  Search
} from "lucide-react";

import { NavLink } from "react-router-dom";

export default function Sidebar() {
  const linkClass = ({ isActive }) =>
    `
    flex
    items-center
    gap-3
    p-3
    rounded-xl
    transition-all

    ${isActive
      ? `
            bg-white
            border
            border-slate-300

            text-slate-900

            font-semibold
          `
      : `
            text-slate-500

            hover:bg-slate-100
            hover:text-slate-900
          `
    }
  `;

  return (

    <aside
      className="
        w-72
        app-card
        border-r
        app-border
        min-h-screen
        flex
        flex-col
      "
    >

      <div
        className="
          p-6
          border-b
          app-border
        "
      >

        <h1
          className="
              text-3xl
              font-bold
            "
        >
          TraceMind
        </h1>

        <p
          className="
            text-xs
            mt-2
            app-muted
          "
        >
          AI Powered RCA Platform
        </p>

      </div>

      <nav
        className="
          flex-1
          p-4
          space-y-2
        "
      >

        <NavLink
          to="/"
          className={linkClass}
        >
          <LayoutDashboard size={18} />
          Dashboard
        </NavLink>

        <NavLink
          to="/new-investigation"
          className={linkClass}
        >
          <Brain size={18} />
          New Investigation
        </NavLink>

        <NavLink
          to="/investigations"
          className={linkClass}
        >
          <Search size={18} />
          Investigations
        </NavLink>

        <NavLink
          to="/incidents"
          className={linkClass}
        >
          <AlertTriangle size={18} />
          Incidents
        </NavLink>
      </nav>

      <div
        className="
          p-4
          border-t
          app-border
        "
      >

        <div
          className="
            text-xs
            app-muted
          "
        >
          TraceMind v1.0
        </div>

      </div>

    </aside>

  );

}