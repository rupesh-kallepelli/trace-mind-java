import { useEffect } from "react";

export const useInvestigationStream = (
  investigationId,
  onEvent
) => {

  useEffect(() => {

    if (!investigationId) {
      return;
    }

    const eventSource = new EventSource(
      `/api/v1/investigations/${investigationId}/stream`
    );

    eventSource.addEventListener(
      "investigation-event",
      (event) => {

        const data = JSON.parse(event.data);

        console.log(
          "STREAM EVENT",
          data
        );

        onEvent(data);
      }
    );

    eventSource.onerror = (error) => {
      console.error(
        "SSE ERROR",
        error
      );
    };

    return () => {
      eventSource.close();
    };

  }, [investigationId, onEvent]);
};