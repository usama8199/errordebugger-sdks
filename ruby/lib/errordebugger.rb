require 'opentelemetry/sdk'
require 'opentelemetry/exporter/otlp'
require 'errordebugger/config'

module ErrorDebugger
  @initialized = false
  @tracer_provider = nil

  def self.init(config)
    return if @initialized

    resource = OpenTelemetry::SDK::Resources::Resource.create({
      'service.name' => config.service_name,
      'deployment.environment' => config.environment,
      'project.token' => config.token,
      'telemetry.sdk.language' => 'ruby',
      'telemetry.sdk.name' => 'errordebugger-ruby'
    })

    endpoint = config.dsn.end_with?('/') ? "#{config.dsn}v1/otlp/traces" : "#{config.dsn}/v1/otlp/traces"

    exporter = OpenTelemetry::Exporter::OTLP::Exporter.new(
      endpoint: endpoint,
      headers: { 'x-project-token' => config.token }
    )

    @tracer_provider = OpenTelemetry::SDK::Trace::TracerProvider.new(
      resource: resource
    )
    @tracer_provider.add_span_processor(
      OpenTelemetry::SDK::Trace::Export::BatchSpanProcessor.new(exporter)
    )

    OpenTelemetry.tracer_provider = @tracer_provider
    @initialized = true
  end

  def self.capture_exception(exception)
    # OpenTelemetry returns a NOOP tracer if not initialized, so this is safe
    tracer = OpenTelemetry.tracer_provider.tracer('errordebugger')
    span = tracer.start_span('manual_exception')
    span.record_exception(exception)
    span.status = OpenTelemetry::Trace::Status.error
    span.finish
  end
  
  # For testing
  def self.reset
    @initialized = false
    @tracer_provider = nil
  end
end
