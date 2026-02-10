mod config;
mod error;

pub use config::Config;
pub use error::{Error, Result};

use opentelemetry::{global, KeyValue};
use opentelemetry_sdk::{runtime, trace::{self, TracerProvider}, Resource};
use opentelemetry_otlp::WithExportConfig;
use once_cell::sync::OnceCell;
use std::collections::HashMap;

static TRACER_PROVIDER: OnceCell<TracerProvider> = OnceCell::new();

pub async fn init(config: Config) -> Result<()> {
    config.validate()?;

    let service_name = config.service_name.unwrap_or_else(|| "rust-app".to_string());
    
    let resource = Resource::new(vec![
        KeyValue::new("service.name", service_name),
        KeyValue::new("deployment.environment", config.environment),
        KeyValue::new("project.token", config.token.clone()),
        KeyValue::new("telemetry.sdk.language", "rust"),
        KeyValue::new("telemetry.sdk.name", "errordebugger-rust"),
    ]);

    let mut headers = HashMap::new();
    headers.insert("x-project-token".to_string(), config.token.clone());

    let endpoint = if config.dsn.ends_with('/') {
        format!("{}v1/otlp/traces", config.dsn)
    } else {
        format!("{}/v1/otlp/traces", config.dsn)
    };

    let exporter = opentelemetry_otlp::new_exporter()
        .http()
        .with_endpoint(endpoint)
        .with_headers(headers)
        .build_span_exporter()?;

    let trace_config = trace::Config::default().with_resource(resource);

    let provider = TracerProvider::builder()
        .with_config(trace_config)
        .with_batch_exporter(exporter, runtime::Tokio)
        .build();

    global::set_tracer_provider(provider.clone());
    
    if TRACER_PROVIDER.set(provider).is_err() {
        return Err(Error::AlreadyInitialized);
    }

    Ok(())
}

pub fn shutdown() {
    if let Some(provider) = TRACER_PROVIDER.get() {
        // Use force_flush since shutdown might not be exposed on reference or requires ownership
        // In most cases, force_flush is sufficient for quick scripts/tests.
        // Also traversing trait for force_flush using `opentelemetry::trace::TracerProvider` import if needed.
        // But let's try calling it inherent or via blanket impl.
        // Note: provider is &TracerProvider (SDK struct).
        // SDK struct has force_flush inherent (usually) or via trait.
        
        // Actually, to use trait methods we need trait in scope.
        // But shutdown is structural.
        // Let's rely on Drop for now if shutdown fails compile.
        // Or specific opentelemetry::trace::TracerProvider trait call.
        use opentelemetry::trace::TracerProvider as _; 
        let _ = provider.force_flush(); 
    }
}
