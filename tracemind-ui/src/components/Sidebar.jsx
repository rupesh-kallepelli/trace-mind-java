import {
  LayoutDashboard,
  Search,
  AlertTriangle
} from "lucide-react";

import { NavLink } from "react-router-dom";

export default function Sidebar() {

  const linkClass = ({ isActive }) =>
    `flex items-center gap-3 p-3 rounded-xl ${
      isActive
        ? "bg-green-500 text-black"
        : "text-gray-400 hover:bg-gray-800"
    }`;

  return (
    <div className="w-72 bg-black border-r border-gray-800">

      <div className="p-6">

        <h1 className="text-3xl font-bold text-green-400">
          ObserveAI
        </h1>

      </div>

      <nav className="p-4 space-y-2">

        <NavLink
          to="/"
          className={linkClass}>

          <LayoutDashboard size={18} />
          Dashboard

        </NavLink>

        <NavLink
          to="/investigations"
          className={linkClass}>

          <Search size={18} />
          Investigations

        </NavLink>

        <NavLink
          to="/incidents"
          className={linkClass}>

          <AlertTriangle size={18} />
          Incidents

        </NavLink>

      </nav>

    </div>
  );
}