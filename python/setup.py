from setuptools import setup, find_packages
import os

# Read version from _version.py
version_ns = {}
with open(os.path.join("errordebugger", "_version.py")) as f:
    exec(f.read(), version_ns)

with open("README.md", "r", encoding="utf-8") as fh:
    long_description = fh.read()

setup(
    name="errordebugger",
    version=version_ns["__version__"],
    author="ErrorDebugger Team",
    author_email="support@errordebugger.com",
    description="A simplified Python SDK for ErrorDebugger via OpenTelemetry",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/usama8199/errordebugger-sdks",
    packages=find_packages(),
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
        "Development Status :: 3 - Alpha",
        "Intended Audience :: Developers",
    ],
    python_requires=">=3.7",
    install_requires=[
        "opentelemetry-api>=1.21.0",
        "opentelemetry-sdk>=1.21.0",
        "opentelemetry-exporter-otlp-proto-http>=1.21.0",
        "opentelemetry-instrumentation>=0.42b0",
    ],
    extras_require={
        "flask": ["opentelemetry-instrumentation-flask>=0.42b0"],
        "django": ["opentelemetry-instrumentation-django>=0.42b0"],
        "fastapi": ["opentelemetry-instrumentation-fastapi>=0.42b0"],
        "requests": ["opentelemetry-instrumentation-requests>=0.42b0"],
    },
)
