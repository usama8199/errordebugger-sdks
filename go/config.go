package errordebugger

import "fmt"

// Config holds the configuration for the ErrorDebugger SDK.
type Config struct {
	// DSN is the base URL of the ErrorDebugger backend (e.g. "http://localhost:8000").
	DSN string
	// Token is the project API token.
	Token string
	// ServiceName is the name of the service (default: "go-app").
	ServiceName string
	// Environment is the deployment environment (default: "production").
	Environment string
	// Debug enables debug logging.
	Debug bool
}

// WithDefaults sets default values for missing configuration fields.
func (c *Config) WithDefaults() {
	if c.ServiceName == "" {
		c.ServiceName = "go-app"
	}
	if c.Environment == "" {
		c.Environment = "production"
	}
}

// Validate checks if the configuration is valid.
func (c *Config) Validate() error {
	if c.DSN == "" {
		return fmt.Errorf("DSN is required")
	}
	if c.Token == "" {
		return fmt.Errorf("Token is required")
	}
	return nil
}
