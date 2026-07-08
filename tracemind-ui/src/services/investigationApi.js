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
        `/v1/investigations/${id}`
      );

    return response.data;
};


export const analyzeIssue = async (
  issue,
  namespace = "default"
) => {

  const response = await api.get(
    `/observai/analyze`,
    {
      params: {
        issue,
        namespace
      }
    }
  );

  return response.data;
};

export const getInvestigationEvents =
  async (id) => {

    const response =
      await api.get(
        `/v1/investigations/${id}/events`
      );

    return response.data;
  };