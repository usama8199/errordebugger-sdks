using System;
using System.Diagnostics;
using System.Collections.Generic;
using OpenTelemetry;
using OpenTelemetry.Trace;
using OpenTelemetry.Resources;
using OpenTelemetry.Exporter;

namespace ErrorDebugger
{
    public static class ErrorDebugger
    {
        private static TracerProvider? _tracerProvider;
        private static ActivitySource? _activitySource;
        private static bool _initialized = false;
        private static readonly object _lock = new object();

        public static void Init(Config config)
        {
            if (_initialized) return;

            lock (_lock)
            {
                if (_initialized) return;

                config.Validate();

                var endpoint = config.Dsn;
                if (!endpoint.EndsWith("/v1/otlp/traces"))
                {
                    endpoint = endpoint.TrimEnd('/') + "/v1/otlp/traces";
                }

                _activitySource = new ActivitySource("ErrorDebugger");

                var resourceBuilder = ResourceBuilder.CreateDefault()
                    .AddService(config.ServiceName)
                    .AddAttributes(new Dictionary<string, object>
                    {
                        ["project.token"] = config.Token,
                        ["deployment.environment"] = config.Environment,
                        ["telemetry.sdk.name"] = "errordebugger",
                        ["telemetry.sdk.language"] = "dotnet"
                    });
                
                if (!string.IsNullOrEmpty(config.Release))
                {
                    resourceBuilder.AddAttributes(new Dictionary<string, object> { ["service.version"] = config.Release! });
                }

                _tracerProvider = Sdk.CreateTracerProviderBuilder()
                    .SetResourceBuilder(resourceBuilder)
                    .AddSource("ErrorDebugger")
                    .AddOtlpExporter(options =>
                    {
                        options.Endpoint = new Uri(endpoint);
                        options.Protocol = OtlpExportProtocol.HttpProtobuf;
                        options.Headers = $"x-project-token={config.Token}";
                    })
                    .Build();

                // Setup global unhandled exception handler for non-ASP.NET apps
                AppDomain.CurrentDomain.UnhandledException += (sender, args) =>
                {
                    if (args.ExceptionObject is Exception ex)
                    {
                        CaptureException(ex);
                        Shutdown(); // Attempt to flush on crash
                    }
                };

                _initialized = true;

                if (config.Debug)
                {
                    Console.WriteLine("ErrorDebugger initialized.");
                }
            }
        }

        public static void CaptureException(Exception exception)
        {
            if (!_initialized || _activitySource == null || exception == null) return;

            // Start a new activity (span)
            using (var activity = _activitySource.StartActivity("captured_exception", ActivityKind.Internal))
            {
                if (activity != null)
                {
                    activity.RecordException(exception);
                    activity.SetStatus(Status.Error.WithDescription(exception.Message));
                }
            }
        }

        public static void CaptureMessage(string message)
        {
            if (!_initialized || _activitySource == null) return;

            using (var activity = _activitySource.StartActivity("log_message", ActivityKind.Internal))
            {
                if (activity != null)
                {
                    activity.SetTag("message", message);
                    activity.SetTag("level", "info");
                }
            }
        }

        public static void Shutdown()
        {
            _tracerProvider?.ForceFlush();
            _tracerProvider?.Dispose();
            _tracerProvider = null;
        }
    }
}
