package errordebugger

import (
	"context"
	"fmt"
	"testing"
)

func TestInit(t *testing.T) {
	// Test missing DSN
	err := Init(Config{Token: "token"})
	if err == nil {
		t.Error("Expected error for missing DSN, got nil")
	}

	// Test missing Token
	err = Init(Config{DSN: "http://localhost:8000"})
	if err == nil {
		t.Error("Expected error for missing Token, got nil")
	}

	// Test valid init
	// Note: this will actually try to connect if we don't mock, but it shouldn't fail Init itself 
	// as connection happens in background usually, or at least exporter creation is local.
	// However, otlptracehttp.New might check connection if insecure/etc.
	// For basic unit test of our wrapper logic:
	err = Init(Config{
		DSN:   "http://localhost:8000",
		Token: "test-token",
		Debug: true,
	})
	if err != nil {
		t.Errorf("Expected nil error for valid config, got %v", err)
	}
}

func TestCaptureException(t *testing.T) {
	// Ensure Init is called first (idempotent-ish for this test suite context)
	_ = Init(Config{
		DSN:   "http://localhost:8000",
		Token: "test-token",
	})
    
    // Just ensure it doesn't panic
    CaptureException(context.Background(), fmt.Errorf("test error"))
}
