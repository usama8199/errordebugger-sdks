import pytest
from unittest.mock import MagicMock, patch

@pytest.fixture
def mock_otlp_exporter():
    with patch("errordebugger.OTLPSpanExporter") as mock:
        yield mock

@pytest.fixture
def mock_tracer_provider():
    with patch("errordebugger.TracerProvider") as mock:
        yield mock

@pytest.fixture
def mock_batch_span_processor():
    with patch("errordebugger.BatchSpanProcessor") as mock:
        yield mock
