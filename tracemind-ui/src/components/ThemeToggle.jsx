import {
  Moon,
  Sun
} from "lucide-react";

import {
  useTheme
} from "../context/ThemeContext";

export default function ThemeToggle() {

  const {
    theme,
    toggleTheme
  } = useTheme();

  return (

    <button
      onClick={toggleTheme}
      className="
        flex
        items-center
        gap-2
        px-4
        py-2
        rounded-xl
        border
        border-slate-700
        hover:border-green-500
        transition-all
      "
    >

      {theme === "dark"
        ? <Sun size={18} />
        : <Moon size={18} />
      }

      {theme === "dark"
        ? "Light"
        : "Dark"
      }

    </button>

  );

}