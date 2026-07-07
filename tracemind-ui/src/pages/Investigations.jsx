import { useEffect, useState } from "react";

import {
  getInvestigations
}
from "../services/investigationApi";

import { useNavigate } from "react-router-dom";

export default function Investigations() {

  const [items, setItems] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {

    getInvestigations()
      .then(data =>
        setItems(data.content));

  }, []);

  return (

    <div>

      <h1 className="text-3xl font-bold mb-6">
        Investigations
      </h1>

      <div className="space-y-4">

        {items.map(item => (

          <div
            key={item.id}
            onClick={() =>
                navigate(
                  `/investigations/${item.id}`
                )
            }
            className="bg-gray-900 rounded-xl p-5 cursor-pointer">

            <div className="font-semibold">
              {item.serviceName}
            </div>

            <div className="text-gray-400">
              {item.status}
            </div>

          </div>

        ))}

      </div>

    </div>
  );
}