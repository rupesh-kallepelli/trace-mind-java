import api from "./api";

export const getDatabasesByApplication = async (
  applicationId
) => {
  const response = await api.get(
    `v1/databases/application/${applicationId}`
  );

  return response.data;
};

export const createDatabase = async (data) => {
  const response = await api.post(
    "v1/databases",
    data
  );

  return response.data;
};

export const updateDatabase = async (
  id,
  data
) => {
  const response = await api.put(
    `v1/databases/${id}`,
    data
  );

  return response.data;
};

export const deleteDatabase = async (id) => {
  await api.delete(`v1/databases/${id}`);
};