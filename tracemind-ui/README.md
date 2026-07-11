# TraceMind UI

This is the user interface for the TraceMind project, a comprehensive Application Performance Monitoring (APM) and distributed tracing solution. It's built with React and Vite, providing a fast and modern user experience for visualizing traces, metrics, and logs from your applications.

## About TraceMind

TraceMind is a suite of tools designed to provide deep insights into your application's performance and behavior. It consists of several components:

*   **`tracemind-agent`**: A Java agent for instrumenting your applications to collect trace data.
*   **`orchestrator-agent`**: An agent for coordinating data collection and other tasks.
*   **`log-search-mcp-server`**: A backend service for aggregating and searching application logs.
*   **`metrics-mcp-server`**: A backend service for collecting and querying application metrics.
*   **`database-query-mcp-server`**: A general-purpose backend for querying stored monitoring data.
*   **`tracemind-ui`**: This React-based web interface for visualizing all the collected data.

## Features

*   **Trace Visualization**: Analyze distributed traces to understand request flows and identify bottlenecks.
*   **Metrics Dashboard**: Monitor key application and system metrics in real-time.
*   **Log Search & Analysis**: A powerful interface to search, filter, and analyze aggregated logs from all your services.
*   **Fast & Responsive UI**: Built with React and Vite for a modern and efficient user experience.

## Getting Started

This project uses Vite for a fast development experience.

### Prerequisites

*   Node.js (v18 or later recommended)
*   npm, pnpm, or yarn

### Installation

1.  Clone the repository.
2.  Navigate to the `tracemind-ui` directory:
    ```sh
    cd tracemind-ui
    ```
3.  Install the dependencies:
    ```sh
    npm install
    ```

### Running the Development Server

To start the local development server, run:

```sh
npm run dev
```

The UI will be available at `http://localhost:5173` by default.
