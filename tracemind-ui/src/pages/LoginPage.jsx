import { GoogleLogin } from "@react-oauth/google";
import { jwtDecode } from "jwt-decode";

import {
  Brain,
  ChevronRight
} from "lucide-react";

import { msalInstance } from "../auth/msalInstance";

export default function LoginPage() {

  const handleGoogleSuccess = (
    response
  ) => {

    const decoded =
      jwtDecode(
        response.credential
      );

    localStorage.setItem(
      "token",
      response.credential
    );

    localStorage.setItem(
      "userEmail",
      decoded.email
    );

    localStorage.setItem(
      "userName",
      decoded.name
    );

    localStorage.setItem(
      "authProvider",
      "google"
    );

    window.location.href = "/";
  };

  const handleMicrosoftLogin =
    async () => {

      try {

        await msalInstance.loginRedirect({
          scopes: [
            "openid",
            "profile",
            "User.Read"
          ]
        });

      } catch (error) {

        console.error(
          "Microsoft login failed",
          error
        );

      }
    };

  return (

    <div className="min-h-screen bg-slate-950 flex">

      <div
        className="
          hidden
          lg:flex
          flex-col
          justify-center
          w-1/2
          px-24
        "
      >
        <div className="max-w-xl">

          <div className="flex items-center gap-4 mb-8">

            <div
              className="
                h-16
                w-16
                rounded-2xl
                bg-blue-600/10
                border
                border-blue-500/20
                flex
                items-center
                justify-center
              "
            >
              <Brain
                size={34}
                className="text-blue-400"
              />
            </div>

            <div>
              <h1 className="text-6xl font-bold text-white">
                TraceMind
              </h1>

              <p className="text-slate-400 text-lg">
                AI Powered RCA Platform
              </p>
            </div>

          </div>

          <h2
            className="
              text-4xl
              font-bold
              text-white
              leading-tight
            "
          >
            AI Powered Root Cause Analysis
            for Modern Applications.
          </h2>

          <p
            className="
              mt-6
              text-lg
              text-slate-400
            "
          >
            Investigate incidents faster using
            logs, traces, metrics, runtime
            signals and AI-driven reasoning.
          </p>

        </div>
      </div>

      <div
        className="
          flex
          flex-1
          items-center
          justify-center
          p-6
        "
      >

        <div
          className="
            w-full
            max-w-md
            rounded-3xl
            border
            border-slate-800
            bg-slate-900
            p-8
            shadow-2xl
          "
        >

          <div className="text-center">

            <h2 className="text-4xl font-bold text-white">
              Welcome Back
            </h2>

            <p className="text-slate-400 mt-3">
              Continue with your preferred
              identity provider
            </p>

          </div>

          <div className="mt-10 space-y-4">

            <button
              onClick={handleMicrosoftLogin}
              className="
                h-14
                w-full
                rounded-xl
                border
                border-slate-700
                bg-slate-800
                hover:bg-slate-700
                transition-all
                flex
                items-center
                justify-between
                px-5
                text-white
                font-medium
              "
            >
              <div className="flex items-center gap-3">

                <div className="grid grid-cols-2 gap-[2px]">
                  <div className="w-2.5 h-2.5 bg-red-500" />
                  <div className="w-2.5 h-2.5 bg-green-500" />
                  <div className="w-2.5 h-2.5 bg-blue-500" />
                  <div className="w-2.5 h-2.5 bg-yellow-500" />
                </div>

                <span>
                  Continue with Microsoft
                </span>

              </div>

              <ChevronRight size={18} />

            </button>

            <div
              className="
                h-14
                w-full
                rounded-xl
                border
                border-slate-700
                bg-slate-800
                hover:bg-slate-700
                transition-all
                flex
                items-center
                justify-center
              "
            >
              <GoogleLogin
                onSuccess={
                  handleGoogleSuccess
                }
                onError={() =>
                  console.error(
                    "Google login failed"
                  )
                }
                theme="filled_black"
                size="large"
                text="signin_with"
                shape="rectangular"
                width="320"
              />
            </div>

          </div>

          <div
            className="
              mt-8
              text-center
              text-sm
              text-slate-500
            "
          >
            Secure enterprise authentication
          </div>

        </div>

      </div>

    </div>
  );
}