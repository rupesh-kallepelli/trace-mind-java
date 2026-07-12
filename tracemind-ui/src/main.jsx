import React from "react";
import ReactDOM from "react-dom/client";

import App from "./App";
import "./index.css";

import { ThemeProvider } from "./context/ThemeContext";

import { GoogleOAuthProvider } from "@react-oauth/google";

import { MsalProvider } from "@azure/msal-react";

import { msalInstance } from "./auth/msalInstance";

async function bootstrap() {

  await msalInstance.initialize();

  const response =
    await msalInstance.handleRedirectPromise();

  console.log(
    "MSAL RESPONSE",
    response
  );

  if (response?.account) {

    localStorage.setItem(
      "token",
      response.idToken
    );

    localStorage.setItem(
      "userEmail",
      response.account.username
    );

    localStorage.setItem(
      "userName",
      response.account.name || ""
    );

    localStorage.setItem(
      "authProvider",
      "microsoft"
    );

    window.history.replaceState(
      {},
      document.title,
      "/"
    );
  }

  ReactDOM.createRoot(
    document.getElementById("root")
  ).render(
    <React.StrictMode>

      <GoogleOAuthProvider
        clientId={
          import.meta.env
            .VITE_GOOGLE_CLIENT_ID
        }
      >

        <MsalProvider
          instance={msalInstance}
        >

          <ThemeProvider>
            <App />
          </ThemeProvider>

        </MsalProvider>

      </GoogleOAuthProvider>

    </React.StrictMode>
  );
}

bootstrap();