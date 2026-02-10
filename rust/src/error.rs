use thiserror::Error;

#[derive(Error, Debug)]
pub enum Error {
    #[error("Configuration error: {0}")]
    Config(String),
    #[error("OpenTelemetry error: {0}")]
    Otel(#[from] opentelemetry::trace::TraceError),
    #[error("SDK already initialized")]
    AlreadyInitialized,
}

pub type Result<T> = std::result::Result<T, Error>;
