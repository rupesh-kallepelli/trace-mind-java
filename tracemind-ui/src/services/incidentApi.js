import api from "./api";

export const getIncidents = async () => {

  const response =
    await api.get("/v1/incidents");

  return response.data;
};