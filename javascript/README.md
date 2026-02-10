# ErrorDebugger JavaScript SDK

A simplified Node.js SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration.

## Installation

```bash
npm install errordebugger-js
```

## Usage

```javascript
const { init, captureException } = require('errordebugger-js');

// Initialize the SDK
init({
  dsn: 'http://localhost:8000',
  token: 'your-project-token',
  environment: 'production' // optional, defaults to 'production'
});

// Auto-instrumentation is enabled by default for supported libraries (Express, Http, internal Node modules, etc.)

// Manual exception capture
try {
  throw new Error('Something went wrong');
} catch (e) {
  captureException(e);
}
```

## Features

- **One-line initialization**: Automatically sets up OpenTelemetry NodeSDK and OTLP Exporter.
- **Auto-instrumentation**: Uses `@opentelemetry/auto-instrumentations-node` to instrument common modules.
- **Manual capture**: Easily capture exceptions manually.
