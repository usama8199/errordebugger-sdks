# ErrorDebugger Rust SDK

A simplified Rust SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration.

## Installation

Add this to your `Cargo.toml`:

```toml
[dependencies]
errordebugger = { git = "https://github.com/usama8199/errordebugger-sdks" }
```

## Usage

```rust
use errordebugger::{init, Config};
use std::error::Error;

#[tokio::main]
async fn main() -> Result<(), Box<dyn Error>> {
    // Initialize SDK
    init(Config {
        dsn: "http://localhost:8000".to_string(),
        token: "your-project-token".to_string(),
        service_name: Some("rust-app".to_string()),
        environment: "production".to_string(),
    }).await?;

    // Application logic...

    Ok(())
}
```

## Features

- **Simple Initialization**: Configures OpenTelemetry TracerProvider and OTLP HTTP exporter.
- **Async Support**: Built on Tokio runtime.
