// Integration test app for Go SDK
package main

import (
	"context"
	"errors"
	"fmt"
	"os"
	"time"

	"errordebugger"
	"go.opentelemetry.io/otel/attribute"
)

func main() {
	if len(os.Args) < 3 {
		fmt.Println("Usage: test_app <dsn> <token>")
		os.Exit(1)
	}

	dsn := os.Args[1]
	token := os.Args[2]

	// Initialize SDK
	config := errordebugger.Config{
		DSN:         dsn,
		Token:       token,
		ServiceName: "go-integration-test",
		Debug:       true,
	}

	if err := errordebugger.Init(config); err != nil {
		fmt.Printf("Init failed: %v\n", err)
		os.Exit(1)
	}
	defer errordebugger.Shutdown()

	// Trigger a test error
	testErr := errors.New("Integration test error from Go SDK")
	errordebugger.CaptureException(
		context.Background(),
		testErr,
		attribute.String("test", "integration"),
		attribute.String("language", "go"),
	)

	// Allow time for export
	time.Sleep(2 * time.Second)
	fmt.Println("Go SDK test completed")
}
