import logging
import importlib

logger = logging.getLogger("errordebugger.instrumentation")

def _auto_instrument(debug=False):
    """
    Attempt to auto-instrument supported libraries.
    """
    # 1. Flask
    try:
        if importlib.util.find_spec("flask"):
            from opentelemetry.instrumentation.flask import FlaskInstrumentor
            FlaskInstrumentor().instrument()
            logger.info("Auto-instrumented Flask.")
    except Exception as e:
        if debug:
            logger.debug(f"Failed to instrument Flask: {e}")

    # 2. Django
    try:
        if importlib.util.find_spec("django"):
            from opentelemetry.instrumentation.django import DjangoInstrumentor
            DjangoInstrumentor().instrument()
            logger.info("Auto-instrumented Django.")
    except Exception as e:
        if debug:
            logger.debug(f"Failed to instrument Django: {e}")
            
    # 3. FastAPI
    try:
        if importlib.util.find_spec("fastapi"):
            from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
            FastAPIInstrumentor().instrument()
            logger.info("Auto-instrumented FastAPI.")
    except Exception as e:
        if debug:
            logger.debug(f"Failed to instrument FastAPI: {e}")
            
    # 4. Requests
    try:
        if importlib.util.find_spec("requests"):
            from opentelemetry.instrumentation.requests import RequestsInstrumentor
            RequestsInstrumentor().instrument()
            logger.info("Auto-instrumented Requests.")
    except Exception as e:
        if debug:
            logger.debug(f"Failed to instrument Requests: {e}")
