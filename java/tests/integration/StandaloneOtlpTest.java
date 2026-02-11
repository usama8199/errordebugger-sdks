import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.ResourceAttributes;

public class StandaloneOtlpTest {
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.err.println("Usage: java StandaloneOtlpTest <otlp-endpoint> <token>");
            System.exit(1);
        }

        String otlpEndpoint = args[0];
        String token = args[1];

        try {
            // Configure OTLP exporter
            OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(otlpEndpoint + "/v1/otlp/traces")
                .addHeader("X-Project-Token", token)
                .build();

            // Configure resource
            Resource resource = Resource.getDefault()
                .merge(Resource.create(
                    io.opentelemetry.api.common.Attributes.of(
                        ResourceAttributes.SERVICE_NAME, "java-integration-test"
                    )
                ));

            // Build SDK
            SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .setResource(resource)
                .build();

            OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .build();

            // Get tracer
            Tracer tracer = openTelemetry.getTracer("java-integration-test", "1.0.0");

            // Create a span with an error
            Span span = tracer.spanBuilder("integration-test-operation").startSpan();
            
            try {
                // Simulate an error
                throw new RuntimeException("Integration test error from Java SDK");
            } catch (Exception e) {
                span.setStatus(StatusCode.ERROR, e.getMessage());
                span.recordException(e);
            } finally {
                span.end();
            }

            // Wait for export
            Thread.sleep(2000);
            
            // Shutdown to flush
            sdkTracerProvider.shutdown();

            System.out.println("Java SDK test completed");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

