package com.errordebugger;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

public class ErrorDebugger {
    private static boolean initialized = false;

    public static void init(Config config) {
        if (initialized)
            return;

        String endpoint = config.getDsn() + (config.getDsn().endsWith("/") ? "v1/otlp/traces" : "/v1/otlp/traces");

        OtlpHttpSpanExporter exporter = OtlpHttpSpanExporter.builder()
                .setEndpoint(endpoint)
                .addHeader("x-project-token", config.getToken())
                .build();

        Resource resource = Resource.getDefault().merge(Resource.create(Attributes.of(
                AttributeKey.stringKey("service.name"), config.getServiceName(),
                AttributeKey.stringKey("deployment.environment"), config.getEnvironment(),
                AttributeKey.stringKey("project.token"), config.getToken(),
                AttributeKey.stringKey("telemetry.sdk.language"), "java",
                AttributeKey.stringKey("telemetry.sdk.name"), "errordebugger-java")));

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
                .setResource(resource)
                .build();

        OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .buildAndRegisterGlobal();

        initialized = true;
    }

    public static void captureException(Exception e) {
        // Safe to call even if init not called (GlobalOpenTelemetry returns no-op)
        Span span = GlobalOpenTelemetry.getTracer("errordebugger").spanBuilder("manual_exception").startSpan();
        span.recordException(e);
        span.setStatus(StatusCode.ERROR);
        span.end();
    }

    // For testing reset
    protected static void reset() {
        initialized = false;
        GlobalOpenTelemetry.resetForTest();
    }
}
