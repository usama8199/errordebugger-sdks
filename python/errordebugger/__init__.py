import logging
import os
import sys

from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.sdk.resources import Resource
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.trace import Status, StatusCode

from .instrumentation import _auto_instrument

# Global state
_tracer_provider = None
_initialized = False

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("errordebugger")

def init(
    dsn,
    token,
    service_name=None,
    environment="production",
    auto_instrument=True,
    debug=False,
    **kwargs
):
    """
    Initialize the ErrorDebugger SDK.
    
    Args:
        dsn (str): The base URL of the ErrorDebugger backend (e.g. "http://localhost:8000").
        token (str): The project API token.
        service_name (str, optional): The name of the service. Defaults to script name.
        environment (str, optional): The deployment environment (e.g. "production", "staging").
        auto_instrument (bool, optional): Whether to enable auto-instrumentation. Defaults to True.
        debug (bool, optional): Whether to enable debug logging. Defaults to False.
    """
    global _tracer_provider, _initialized
    
    if _initialized:
        logger.warning("ErrorDebugger SDK already initialized.")
        return _tracer_provider
        
    if debug:
        logger.setLevel(logging.DEBUG)
        
    # Default service name
    if not service_name:
        service_name = os.path.basename(sys.argv[0]) or "python-app"

    logger.debug(f"Initializing ErrorDebugger SDK for service: {service_name}")
        
    # Create Resource
    resource = Resource.create({
        "service.name": service_name,
        "project.token": token,
        "deployment.environment": environment,
        "telemetry.sdk.language": "python",
        "telemetry.sdk.name": "errordebugger-python",
        "telemetry.sdk.version": "0.1.0"
    })
    
    # Configure OTLP Exporter
    endpoint = f"{dsn.rstrip('/')}/v1/otlp/traces"
    exporter = OTLPSpanExporter(
        endpoint=endpoint,
        headers={"x-project-token": token}
    )
    
    # Configure Tracer Provider
    _tracer_provider = TracerProvider(resource=resource)
    _tracer_provider.add_span_processor(BatchSpanProcessor(exporter))
    
    # Set global provider
    trace.set_tracer_provider(_tracer_provider)
    
    if auto_instrument:
        _auto_instrument(debug)
        
    _initialized = True
    logger.info("ErrorDebugger SDK initialized successfully.")
    
    return _tracer_provider

def capture_exception(exception, level="error", **extra):
    """
    Manually capture an exception.
    
    Args:
        exception (Exception): The exception object.
        level (str, optional): The severity level. Defaults to "error".
        **extra: Additional attributes to attach to the span.
    """
    tracer = trace.get_tracer("errordebugger.manual")
    
    with tracer.start_as_current_span("manual_exception") as span:
        span.record_exception(exception)
        span.set_status(Status(StatusCode.ERROR, str(exception)))
        
        # Add extra attributes
        for key, value in extra.items():
            span.set_attribute(key, value)
            
        span.set_attribute("error.level", level)

def get_tracer(name="errordebugger"):
    """
    Get a tracer instance.
    """
    return trace.get_tracer(name)
