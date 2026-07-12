import {
  AlertTriangle,
  Brain,
  LayoutDashboard,
  Search,
  LogOut,
  User
} from "lucide-react";

import { NavLink } from "react-router-dom";

export default function Sidebar() {

  const email =
    localStorage.getItem("userEmail");

  const logout = () => {
    localStorage.clear();
    sessionStorage.clear();

    window.location.href = "/";
  };

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
          space-y-3
        "
      >
        <div
          className="
            flex
            items-center
            gap-2
            text-sm
          "
        >
          <User size={16} />
          <span className="truncate">
            {email || "Authenticated User"}
          </span>
        </div>

        <button
          onClick={logout}
          className="
            w-full
            flex
            items-center
            justify-center
            gap-2
            px-3
            py-2
            rounded-lg
            bg-red-600
            text-white
            hover:bg-red-700
            transition
          "
        >
          <LogOut size={16} />
          Logout
        </button>

        <div
          className="
            text-xs
            app-muted
            text-center
          "
        >
          TraceMind v1.0
        </div>
      </div>
    </aside>
  );
}