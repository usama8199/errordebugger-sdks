# ErrorDebugger Python SDK

A simplified Python SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration.

## Installation

```bash
pip install errordebugger
```

## Usage

```python
from errordebugger import init, capture_exception
import os

# Initialize the SDK
init(
    dsn="http://localhost:8000",
    token="your-project-token",
    environment="production"
)

# Auto-instrumentation is enabled by default for supported libraries (Flask, Requests, etc.)

# Manual exception capture
try:
    1 / 0
except Exception as e:
    capture_exception(e)
```

## Features

- **One-line initialization**: Automatically sets up OpenTelemetry TracerProvider and OTLP Exporter.
- **Auto-instrumentation**: Automatically instruments supported libraries if they are installed.
- **Manual capture**: Easily capture exceptions manually.
- **Metadata**: Automatically attaches service name, environment, and other metadata to traces.
