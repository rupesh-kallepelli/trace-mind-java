import api from "./api";

export const getApisByMicroservice =
  async (microserviceId) => {
    const response = await api.get(
      `/apis/microservice/${microserviceId}`
    );

    return response.data;
  };

export const createApi = async (data) => {
  const response = await api.post(
    "/apis",
    data
  );

  return response.data;
};