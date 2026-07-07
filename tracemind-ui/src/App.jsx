import {
  BrowserRouter,
  Routes,
  Route
} from "react-router-dom";

import MainLayout from "./layouts/MainLayout";

import Dashboard from "./pages/Dashboard";
import Investigations from "./pages/Investigations";
import InvestigationDetail from "./pages/InvestigationDetail";
import Incidents from "./pages/Incidents";


export default function App() {

  return (
    <BrowserRouter>

      <MainLayout>

        <Routes>

          <Route path="/" element={<Dashboard />} />

          <Route
            path="/investigations"
            element={<Investigations />}
          />

          <Route
            path="/investigations/:id"
            element={<InvestigationDetail />}
          />

          <Route
            path="/incidents"
            element={<Incidents />}
          />

          {/* <Route
            path="/services"
            element={<Services />}
          />

          <Route
            path="/analytics"
            element={<Analytics />}
          /> */}

        </Routes>

      </MainLayout>

    </BrowserRouter>
  );
}