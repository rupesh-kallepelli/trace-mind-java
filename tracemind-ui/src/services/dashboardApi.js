import api from "./api";

export const getDashboardOverview = async () => {
  const response = await api.get(
    "/v1/dashboard/overview"
  );

  return response.data;
};