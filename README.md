# ErrorDebugger SDKs

Official SDK packages for ErrorDebugger error tracking platform.

## 📦 Available SDKs

| Language | Package | Installation |
|----------|---------|--------------|
| Python | errordebugger | `pip install errordebugger` |
| JavaScript | errordebugger | `npm install errordebugger` |
| Go | errordebugger-go | `go get github.com/usama8199/errordebugger-sdks/go` |
| Rust | errordebugger | `cargo add errordebugger` |
| Java | errordebugger | Maven/Gradle |
| C# | ErrorDebugger | `dotnet add package ErrorDebugger` |
| PHP | errordebugger | `composer require errordebugger/errordebugger` |
| Ruby | errordebugger | `gem install errordebugger` |

## 🚀 Quick Start

Each SDK provides 3-line setup:

**Python:**
```python
from errordebugger import init
init(dsn="https://api.yourdomain.com", token="YOUR_TOKEN")
```

**JavaScript:**
```javascript
const { init } = require('errordebugger');
init({ dsn: 'https://api.yourdomain.com', token: 'YOUR_TOKEN' });
```

**Go:**
```go
import "github.com/usama8199/errordebugger-sdks/go"
errordebugger.Init(&errordebugger.Config{DSN: "https://api.yourdomain.com", Token: "YOUR_TOKEN"})
```

**Rust:**
```rust
use errordebugger::{Config, init};
init(Config { dsn: "https://api.yourdomain.com".to_string(), token: "YOUR_TOKEN".to_string(), ..Default::default() }).await.unwrap();
```

See individual SDK directories for detailed documentation.

## 📖 Documentation

- [Python SDK](./python/) - Django, Flask, FastAPI
- [JavaScript SDK](./javascript/) - Express, Next.js, Fastify
- [Go SDK](./go/) - Gin, Echo, Chi
- [Rust SDK](./rust/) - Actix, Rocket, Axum
- [Java SDK](./java/) - Spring Boot, Micronaut, Quarkus
- [C# SDK](./csharp/) - ASP.NET Core, Blazor
- [PHP SDK](./php/) - Laravel, Symfony, WordPress
- [Ruby SDK](./ruby/) - Rails, Sinatra

## 🔧 How It Works

All SDKs use **OpenTelemetry Protocol (OTLP)** internally for production-grade distributed tracing:

1. SDK wraps OpenTelemetry instrumentation
2. Captures exceptions and request context automatically
3. Sends telemetry via OTLP HTTP to backend: `POST /v1/otlp/traces`
4. Backend processes traces and provides AI-powered error analysis

## 🔗 Links

- Main Repository: https://github.com/usama8199/error-debugger
- Documentation: https://github.com/usama8199/error-debugger/tree/main/docs
- Backend API: Built with FastAPI + PostgreSQL + pgvector

## 📄 License

MIT License - see individual SDK LICENSE files

## 🛠️ Development

Each SDK is independently versioned and tested:

- **Python**: pytest, published to PyPI
- **JavaScript**: npm test, published to npm
- **Go**: go test, importable via GitHub
- **Rust**: cargo test, published to crates.io
- **Java**: Maven/Gradle, published to Maven Central
- **C#**: dotnet test, published to NuGet
- **PHP**: Composer, published to Packagist
- **Ruby**: rspec, published to RubyGems

## 🤝 Contributing

See individual SDK directories for contribution guidelines.
