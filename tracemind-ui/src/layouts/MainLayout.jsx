import Sidebar from "../components/Sidebar";
import Header from "../components/Header";

export default function MainLayout({ children }) {

  return (
    <div className="flex h-screen bg-[#0A0A0A] text-white">

      <Sidebar />

      <div className="flex-1">

        <Header />

        <div className="p-8 overflow-auto h-[calc(100vh-64px)]">

          {children}

        </div>

      </div>

    </div>
  );
}