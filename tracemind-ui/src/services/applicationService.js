import api from "./api";

export const getApplications = async () => {
  const response = await api.get("v1/applications");
  return response.data;
};

export const createApplication = async (data) => {
  const response = await api.post("v1/applications", data);
  return response.data;
};

export const getApplication = async (id) => {
  const response = await api.get(`v1/applications/${id}`);
  return response.data;
};

export const updateApplication = async (id, data) => {
  const response = await api.put(`v1/applications/${id}`, data);
  return response.data;
};

export const deleteApplication = async (id) => {
  await api.delete(`v1/applications/${id}`);
};

export const getApplicationContext = async (id) => {
  const response = await api.get(
    `v1/applications/${id}/full-context`
  );

  return response.data;
};