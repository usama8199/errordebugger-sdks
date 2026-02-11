using OpenTelemetry;
using OpenTelemetry.Trace;
using OpenTelemetry.Resources;
using OpenTelemetry.Exporter;
using System.Diagnostics;

namespace ErrorDebugger;

public class ErrorDebugger
{
    private static bool _initialized = false;
    private static TracerProvider? _tracerProvider;
    internal static readonly ActivitySource ActivitySource = new("errordebugger");

    public static void Init(Config config)
    {
        if (_initialized) return;
        
        if (string.IsNullOrEmpty(config.Dsn)) throw new ArgumentException("DSN is required");
        if (string.IsNullOrEmpty(config.Token)) throw new ArgumentException("Token is required");

        var serviceName = config.ServiceName ?? "csharp-app";
        
        var endpoint = config.Dsn.EndsWith("/") 
            ? new Uri($"{config.Dsn}v1/otlp/traces") 
            : new Uri($"{config.Dsn}/v1/otlp/traces");

        _tracerProvider = Sdk.CreateTracerProviderBuilder()
            .AddSource("errordebugger")
            .SetResourceBuilder(ResourceBuilder.CreateDefault()
                .AddService(serviceName)
                .AddAttributes(new[] {
                    new KeyValuePair<string, object>("deployment.environment", config.Environment),
                    new KeyValuePair<string, object>("project.token", config.Token),
                    new KeyValuePair<string, object>("telemetry.sdk.language", "csharp"),
                    new KeyValuePair<string, object>("telemetry.sdk.name", "errordebugger-csharp")
                }))
            .AddOtlpExporter(options => {
                options.Endpoint = endpoint;
                options.Headers = $"x-project-token={config.Token}";
                options.Protocol = OtlpExportProtocol.HttpProtobuf;
            })
            .Build();
        
        _initialized = true;
    }
    
    public static void CaptureException(Exception exception)
    {
        // Start a new activity (span)
        using var activity = ActivitySource.StartActivity("manual_exception");
        if (activity != null)
        {
            activity.RecordException(exception);
            activity.SetStatus(Status.Error);
        }
    }
}
