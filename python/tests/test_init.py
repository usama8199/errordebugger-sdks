import pytest
import errordebugger

def test_init_sdk(mock_otlp_exporter, mock_tracer_provider, mock_batch_span_processor):
    # Reset initialized state
    errordebugger._initialized = False
    
    provider = errordebugger.init(
        dsn="http://localhost:8000",
        token="test-token",
        service_name="test-service",
        environment="test-env"
    )
    
    # Verify OTLP Exporter created with correct args
    mock_otlp_exporter.assert_called_once()
    call_args = mock_otlp_exporter.call_args
    assert call_args.kwargs["endpoint"] == "http://localhost:8000/v1/otlp/traces"
    assert call_args.kwargs["headers"] == {"x-project-token": "test-token"}
    
    # Verify TracerProvider created
    mock_tracer_provider.assert_called_once()
    
    # Verify global state updated
    assert errordebugger._initialized is True
    assert provider is not None
