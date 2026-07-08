import {
  LayoutDashboard,
  Search,
  AlertTriangle,
  Brain,
  Activity,
  BarChart3
} from "lucide-react";

import { NavLink } from "react-router-dom";

export default function Sidebar() {

  const linkClass = ({ isActive }) =>
    `flex items-center gap-3 p-3 rounded-xl transition-all ${
      isActive
        ? "bg-green-500 text-black font-semibold"
        : "text-gray-400 hover:bg-gray-800 hover:text-white"
    }`;

  return (
    <div className="w-72 bg-black border-r border-gray-800 min-h-screen">

      <div className="p-6 border-b border-gray-800">

        <h1 className="text-3xl font-bold text-green-400">
          TraceMind
        </h1>

        <p className="text-xs text-gray-500 mt-2">
          AI Powered RCA Platform
        </p>

      </div>

      <nav className="p-4 space-y-2">

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

        <NavLink
          to="/services"
          className={linkClass}
        >
          <Activity size={18} />
          Services
        </NavLink>

        <NavLink
          to="/analytics"
          className={linkClass}
        >
          <BarChart3 size={18} />
          Analytics
        </NavLink>

      </nav>
    </div>
  );
}