# ErrorDebugger PHP SDK

A simplified PHP SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration.

## Installation

Add via Composer:

```bash
composer require errordebugger/errordebugger-php
```

## Usage

```php
use ErrorDebugger\ErrorDebugger;
use ErrorDebugger\Config;

// Initialize SDK
$config = new Config(
    dsn: 'http://localhost:8000',
    token: 'your-project-token',
    serviceName: 'php-app'
);

ErrorDebugger::init($config);

// Manual capture
try {
    throw new \Exception("Something went wrong");
} catch (\Throwable $e) {
    ErrorDebugger::captureException($e);
}
```
