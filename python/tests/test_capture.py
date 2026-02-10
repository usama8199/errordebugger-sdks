import pytest
from unittest.mock import MagicMock, patch
import errordebugger

def test_capture_exception():
    mock_tracer = MagicMock()
    mock_span = MagicMock()
    
    # Configure mock tracer to return mock span context manager
    mock_tracer.start_as_current_span.return_value.__enter__.return_value = mock_span
    
    with patch("opentelemetry.trace.get_tracer", return_value=mock_tracer):
        try:
            1 / 0
        except ZeroDivisionError as e:
            errordebugger.capture_exception(e, **{"user.id": "123"})
            
            # Verify exception recorded
            mock_span.record_exception.assert_called_once_with(e)
            
            # Verify status set to ERROR
            mock_span.set_status.assert_called_once()
            
            # Verify attributes set
            mock_span.set_attribute.assert_any_call("user.id", "123")
            mock_span.set_attribute.assert_any_call("error.level", "error")
