package errordebugger

import (
	"context"
	"fmt"
	"strings"

	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/codes"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracehttp"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.17.0"
	"go.opentelemetry.io/otel/trace"
)

var (
	tracerProvider *sdktrace.TracerProvider
	tracer         trace.Tracer
)

// Init initializes the ErrorDebugger SDK with the provided configuration.
func Init(cfg Config) error {
	cfg.WithDefaults()
	if err := cfg.Validate(); err != nil {
		return err
	}

	// Create Resource
	res, err := resource.New(context.Background(),
		resource.WithAttributes(
			semconv.ServiceName(cfg.ServiceName),
			semconv.DeploymentEnvironment(cfg.Environment),
			attribute.String("project.token", cfg.Token),
			semconv.TelemetrySDKLanguageGo,
			semconv.TelemetrySDKName("errordebugger-go"),
			semconv.TelemetrySDKVersion(Version),
		),
	)
	if err != nil {
		return fmt.Errorf("failed to create resource: %w", err)
	}

	// Create OTLP HTTP Exporter
	endpoint := strings.TrimRight(cfg.DSN, "/")
	if strings.HasPrefix(endpoint, "http://") {
		endpoint = strings.TrimPrefix(endpoint, "http://")
	} else if strings.HasPrefix(endpoint, "https://") {
		endpoint = strings.TrimPrefix(endpoint, "https://")
	}
	// Note: otlptracehttp.New expects endpoint without scheme, and uses WithInsecure for http
	
	opts := []otlptracehttp.Option{
		otlptracehttp.WithEndpoint(endpoint),
		otlptracehttp.WithURLPath("/v1/otlp/traces"),
		otlptracehttp.WithHeaders(map[string]string{
			"x-project-token": cfg.Token,
		}),
	}

	if strings.Contains(cfg.DSN, "http://") {
		opts = append(opts, otlptracehttp.WithInsecure())
	}

	exporter, err := otlptracehttp.New(context.Background(), opts...)
	if err != nil {
		return fmt.Errorf("failed to create exporter: %w", err)
	}

	// Create TracerProvider
	tracerProvider = sdktrace.NewTracerProvider(
		sdktrace.WithBatcher(exporter),
		sdktrace.WithResource(res),
	)

	// Set global TracerProvider
	otel.SetTracerProvider(tracerProvider)
	
	tracer = otel.Tracer("errordebugger-go")

	return nil
}

// Shutdown shuts down the SDK, flushing any remaining spans.
func Shutdown(ctx context.Context) error {
	if tracerProvider != nil {
		return tracerProvider.Shutdown(ctx)
	}
	return nil
}

// CaptureException manually records an error as a span.
func CaptureException(ctx context.Context, err error, attrs ...attribute.KeyValue) {
	if tracer == nil {
		// If SDK not initialized, do nothing or log warning
		return
	}

	_, span := tracer.Start(ctx, "manual_exception")
	defer span.End()

	span.RecordError(err)
	span.SetStatus(codes.Error, err.Error())
	
	if len(attrs) > 0 {
		span.SetAttributes(attrs...)
	}
}
