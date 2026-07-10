import Sidebar from "../components/Sidebar";
import Header from "../components/Header";

export default function MainLayout({
  children
}) {

  return (

    <div
      className="
        flex
        h-screen
        app-bg
        app-text
      "
    >

      <Sidebar />

      <div className="flex-1">

        <Header />

        <div
          className="
            p-6
            overflow-auto
            h-[calc(100vh-64px)]
          "
        >
          {children}
        </div>

      </div>

    </div>

  );

}