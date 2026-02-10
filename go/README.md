# ErrorDebugger Go SDK

A simplified Go SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration.

## Installation

```bash
go get github.com/usama8199/errordebugger-go
```

## Usage

```go
package main

import (
	"context"
	"errors"
	"log"

	"github.com/usama8199/errordebugger-go"
)

func main() {
	// Initialize SDK
	err := errordebugger.Init(errordebugger.Config{
		DSN:   "http://localhost:8000",
		Token: "your-project-token",
	})
	if err != nil {
		log.Fatalf("Failed to init errordebugger: %v", err)
	}

	// Manual exception capture
	err = errors.New("something went wrong")
	errordebugger.CaptureException(context.Background(), err)
}
```

## Features

- **Simple Initialization**: Configures OpenTelemetry TracerProvider and OTLP HTTP exporter.
- **Manual Capture**: Helper function `CaptureException` to record errors.
