import { useEffect, useState } from "react";

import { useParams } from "react-router-dom";

import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

import {
  getInvestigationDetail
}
from "../services/investigationApi";

export default function InvestigationDetail() {

  const { id } = useParams();

  const [data, setData] =
    useState(null);

  useEffect(() => {

    getInvestigationDetail(id)
      .then(setData);

  }, [id]);

  if (!data) {
    return <div>Loading...</div>;
  }

  return (

    <div>

      <h1 className="text-3xl font-bold">

        {data.investigation.serviceName}

      </h1>

      <div className="mt-6">

        <div className="bg-gray-900 p-6 rounded-xl">

          <h2 className="font-semibold mb-3">
            Root Cause
          </h2>

          <div>
            {data.investigation.rootCause}
          </div>

        </div>

      </div>

      <div className="grid grid-cols-2 gap-6 mt-6">

        <div className="bg-gray-900 p-6 rounded-xl">

          <h3 className="mb-4 font-semibold">
            Evidence
          </h3>

          {data.evidence.map(e => (
            <div
              key={e.id}
              className="mb-5">

              <div>
                {e.agentType}
              </div>

              <div className="text-gray-400">
                {e.summary}
              </div>

            </div>
          ))}

        </div>

        <div className="bg-gray-900 p-6 rounded-xl">

          <h3 className="font-semibold mb-4">
            Incident
          </h3>

          <div>
            {data.incident?.incidentNumber}
          </div>

          <div>
            {data.incident?.severity}
          </div>

          <div>
            {data.incident?.status}
          </div>

        </div>

      </div>

      <div className="bg-gray-900 p-6 rounded-xl mt-6">

        <ReactMarkdown
          remarkPlugins={[remarkGfm]}>

          {
            data.investigation
              .reportMarkdown || ""
          }

        </ReactMarkdown>

      </div>

    </div>

  );
}