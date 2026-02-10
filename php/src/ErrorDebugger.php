<?php
namespace ErrorDebugger;

use OpenTelemetry\SDK\Trace\TracerProvider;
use OpenTelemetry\SDK\Trace\SpanProcessor\BatchSpanProcessorBuilder;
use OpenTelemetry\SDK\Resource\ResourceInfo;
use OpenTelemetry\SDK\Resource\ResourceInfoFactory;
use OpenTelemetry\Contrib\Otlp\SpanExporter;
use OpenTelemetry\Contrib\Otlp\OtlpHttpTransportFactory;

class ErrorDebugger
{
    private static bool $initialized = false;
    private static TracerProvider $tracerProvider;
    
    public static function init(Config $config): void
    {
        if (self::$initialized) return;
        
        $resource = ResourceInfoFactory::defaultResource()->merge(ResourceInfo::create(
            \OpenTelemetry\SDK\Common\Attribute\Attributes::create([
                'service.name' => $config->serviceName,
                'deployment.environment' => $config->environment,
                'project.token' => $config->token,
                'telemetry.sdk.language' => 'php',
                'telemetry.sdk.name' => 'errordebugger-php'
            ])
        ));
        
        $endpoint = $config->dsn . (str_ends_with($config->dsn, '/') ? 'v1/otlp/traces' : '/v1/otlp/traces');
        
        $transport = (new OtlpHttpTransportFactory())->create(
            $endpoint,
            'application/x-protobuf',
            ['x-project-token' => $config->token]
        );
        
        $exporter = new SpanExporter($transport);
        
        self::$tracerProvider = new TracerProvider(
            (new BatchSpanProcessorBuilder($exporter))->build(),
            null,
            $resource
        );
        
        self::$initialized = true;
    }
    
    public static function captureException(\Throwable $exception): void
    {
        if (!self::$initialized) return;

        $tracer = self::$tracerProvider->getTracer('errordebugger');
        $span = $tracer->spanBuilder('manual_exception')->startSpan();
        $span->recordException($exception);
        $span->setStatus(\OpenTelemetry\API\Trace\StatusCode::STATUS_ERROR);
        $span->end();
    }
    
    public static function shutdown(): void
    {
        if (self::$initialized) {
            self::$tracerProvider->shutdown();
        }
    }
}
