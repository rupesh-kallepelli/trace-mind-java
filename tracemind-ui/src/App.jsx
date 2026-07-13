import {
  BrowserRouter,
  Routes,
  Route,
  Navigate
} from "react-router-dom";

import MainLayout from "./layouts/MainLayout";

import Dashboard from "./pages/Dashboard";
import Investigations from "./pages/Investigations";
import InvestigationDetail from "./pages/InvestigationDetail";
import Incidents from "./pages/Incidents";
import NewInvestigation from "./pages/NewInvestigation";
import ApplicationManagementPage from "./pages/ApplicationManagementPage";
import ApplicationDetailsPage from "./pages/ApplicationDetailsPage";
import MicroserviceDetailsPage from "./pages/MicroserviceDetailsPage";
import DatabaseDetailsPage from "./pages/DatabaseDetailsPage";

import LoginPage from "./pages/LoginPage";

export default function App() {

  const token = localStorage.getItem("token");

  if (!token) {
    return <LoginPage />;
  }

  return (
    <BrowserRouter>
      <MainLayout>
        <Routes>

          <Route
            path="/"
            element={<Dashboard />}
          />

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

          <Route
            path="/new-investigation"
            element={<NewInvestigation />}
          />

          <Route
            path="/applications"
            element={<ApplicationManagementPage />}
          />

          <Route
            path="/applications/:id"
            element={<ApplicationDetailsPage />}
          />
          <Route
            path="*"
            element={<Navigate to="/" replace />}
          />
        <Route
          path="/applications/:appId/microservices/:serviceId"
          element={<MicroserviceDetailsPage />}
        />

        <Route
          path="/applications/:appId/databases/:databaseId"
          element={<DatabaseDetailsPage />}
        />
        </Routes>
      </MainLayout>
    </BrowserRouter>
  );
}