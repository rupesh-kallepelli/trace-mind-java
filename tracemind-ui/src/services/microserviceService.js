import api from "./api";

export const getMicroservicesByApplication = async (
  applicationId
) => {
  const response = await api.get(
    `v1/microservices/application/${applicationId}`
  );

  return response.data;
};

export const createMicroservice = async (data) => {
  const response = await api.post(
    "v1/microservices",
    data
  );

  return response.data;
};

export const updateMicroservice = async (
  id,
  data
) => {
  const response = await api.put(
    `v1/microservices/${id}`,
    data
  );

  return response.data;
};

export const deleteMicroservice = async (
  id
) => {
  await api.delete(`v1/microservices/${id}`);
};