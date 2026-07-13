import { useState } from "react";
import {
  ChevronLeft,
  ChevronRight,
  Building2,
  Layers
} from "lucide-react";
import { createApplication } from "../services/applicationService";

export default function ApplicationModal({
  open,
  onClose,
  onSaved,
}) {
  const [step, setStep] = useState(1);

  const [form, setForm] = useState({
    name: "",
    applicationCode: "",
    businessDomain: "",
    criticality: "Tier1",
    ownerTeam: "",
    supportTeam: "",
    description: "",
    applicationContext: "",
  });

  if (!open) return null;

  const handleSave = async () => {
    await createApplication(form);
    onSaved();
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex justify-center items-center z-50 p-6">
      <div className="w-full max-w-5xl bg-slate-900 border border-slate-800 rounded-3xl shadow-2xl overflow-hidden">

        {/* Header */}
        <div className="border-b border-slate-800 p-6">
          <h2 className="text-2xl font-bold text-white">
            Add Application
          </h2>

          <p className="text-slate-400 mt-1">
            Onboard application knowledge for TraceMind investigations
          </p>

          <div className="flex gap-3 mt-5">
            <div
              className={`px-3 py-1 rounded-full text-sm ${
                step === 1
                  ? "bg-blue-600 text-white"
                  : "bg-slate-800 text-slate-400"
              }`}
            >
              Basic Information
            </div>

            <div
              className={`px-3 py-1 rounded-full text-sm ${
                step === 2
                  ? "bg-blue-600 text-white"
                  : "bg-slate-800 text-slate-400"
              }`}
            >
              Business Context
            </div>
          </div>
        </div>

        <div className="p-8">

          {step === 1 && (
            <>
              <div className="flex items-center gap-3 mb-6">
                <Building2 className="text-cyan-400" />
                <h3 className="text-xl font-semibold">
                  Application Details
                </h3>
              </div>

              <div className="grid md:grid-cols-2 gap-5">

                <div>
                  <label className="text-sm text-slate-400">
                    Application Name
                  </label>
                  <input
                    className="app-input w-full mt-2"
                    value={form.name}
                    onChange={(e) =>
                      setForm({
                        ...form,
                        name: e.target.value,
                      })
                    }
                  />
                </div>

                <div>
                  <label className="text-sm text-slate-400">
                    Application Code
                  </label>
                  <input
                    className="app-input w-full mt-2"
                    value={form.applicationCode}
                    onChange={(e) =>
                      setForm({
                        ...form,
                        applicationCode:
                          e.target.value,
                      })
                    }
                  />
                </div>

                <div>
                  <label className="text-sm text-slate-400">
                    Business Domain
                  </label>

                  <select
                    className="app-input w-full mt-2"
                    value={form.businessDomain}
                    onChange={(e) =>
                      setForm({
                        ...form,
                        businessDomain:
                          e.target.value,
                      })
                    }
                  >
                    <option value="">
                      Select Domain
                    </option>
                    <option>Banking</option>
                    <option>Retail</option>
                    <option>Insurance</option>
                    <option>Payments</option>
                    <option>Healthcare</option>
                  </select>
                </div>

                <div>
                  <label className="text-sm text-slate-400">
                    Criticality
                  </label>

                  <select
                    className="app-input w-full mt-2"
                    value={form.criticality}
                    onChange={(e) =>
                      setForm({
                        ...form,
                        criticality:
                          e.target.value,
                      })
                    }
                  >
                    <option>Tier1</option>
                    <option>Tier2</option>
                    <option>Tier3</option>
                  </select>
                </div>

                <div>
                  <label className="text-sm text-slate-400">
                    Owner Team
                  </label>

                  <input
                    className="app-input w-full mt-2"
                    value={form.ownerTeam}
                    onChange={(e) =>
                      setForm({
                        ...form,
                        ownerTeam:
                          e.target.value,
                      })
                    }
                  />
                </div>

                <div>
                  <label className="text-sm text-slate-400">
                    Support Team
                  </label>

                  <input
                    className="app-input w-full mt-2"
                    value={form.supportTeam}
                    onChange={(e) =>
                      setForm({
                        ...form,
                        supportTeam:
                          e.target.value,
                      })
                    }
                  />
                </div>
              </div>
            </>
          )}

          {step === 2 && (
            <>
              <div className="flex items-center gap-3 mb-6">
                <Layers className="text-cyan-400" />
                <h3 className="text-xl font-semibold">
                  Business Context
                </h3>
              </div>

              <div className="space-y-5">

                <div>
                  <label className="text-sm text-slate-400">
                    Description
                  </label>

                  <textarea
                    rows={4}
                    className="app-input w-full mt-2"
                    value={form.description}
                    onChange={(e) =>
                      setForm({
                        ...form,
                        description:
                          e.target.value,
                      })
                    }
                  />
                </div>

                <div>
                  <label className="text-sm text-slate-400">
                    Application Context
                  </label>

                  <textarea
                    rows={6}
                    className="app-input w-full mt-2"
                    placeholder="Explain what this application does, critical business processes, dependencies and expected investigation context..."
                    value={
                      form.applicationContext
                    }
                    onChange={(e) =>
                      setForm({
                        ...form,
                        applicationContext:
                          e.target.value,
                      })
                    }
                  />
                </div>
              </div>
            </>
          )}
        </div>

        <div className="border-t border-slate-800 p-6 flex justify-between">

          <button
            onClick={() =>
              step === 1
                ? onClose()
                : setStep(1)
            }
            className="app-button-secondary flex items-center gap-2"
          >
            <ChevronLeft size={16} />
            {step === 1 ? "Cancel" : "Back"}
          </button>

          {step === 1 ? (
            <button
              onClick={() => setStep(2)}
              className="app-button-primary flex items-center gap-2"
            >
              Next
              <ChevronRight size={16} />
            </button>
          ) : (
            <button
              onClick={handleSave}
              className="app-button-primary"
            >
              Create Application
            </button>
          )}
        </div>
      </div>
    </div>
  );
}