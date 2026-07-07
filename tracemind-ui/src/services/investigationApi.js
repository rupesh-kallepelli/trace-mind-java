import api from "./api";

export const getInvestigations = async (
  page = 0,
  size = 20
) => {

  const response =
    await api.get(
      `/v1/investigations?page=${page}&size=${size}`
    );

  return response.data;
};

export const getInvestigationDetail =
  async (id) => {

    const response =
      await api.get(
        `/v1/investigations/${id}/detail`
      );

    return response.data;
};