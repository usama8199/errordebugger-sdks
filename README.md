# ErrorDebugger SDKs Monorepo

![CI/CD Status](https://github.com/usama8199/errordebugger-sdks/actions/workflows/publish-sdks.yml/badge.svg)

This monorepo houses the official SDKs for **ErrorDebugger**, a platform for real-time error tracking and debugging. All SDKs are versioned and published automatically via GitHub Actions.

## 📦 Supported Languages

| Language | SDK Package | Registry | Version |
|----------|-------------|----------|---------|
| 🐍 **Python** | `errordebugger` | [PyPI](https://pypi.org/project/errordebugger/) | [![PyPI](https://img.shields.io/pypi/v/errordebugger)](https://pypi.org/project/errordebugger/) |
| 🟨 **JavaScript** | `errordebugger-js` | [npm](https://www.npmjs.com/package/errordebugger-js) | [![npm](https://img.shields.io/npm/v/errordebugger-js)](https://www.npmjs.com/package/errordebugger-js) |
| 🦀 **Rust** | `errordebugger` | [crates.io](https://crates.io/crates/errordebugger) | [![Crates.io](https://img.shields.io/crates/v/errordebugger)](https://crates.io/crates/errordebugger) |
| 🐹 **Go** | `github.com/usama8199/errordebugger-go` | [pkg.go.dev](https://pkg.go.dev/github.com/usama8199/errordebugger-go) | [![Go Reference](https://pkg.go.dev/badge/github.com/usama8199/errordebugger-go.svg)](https://pkg.go.dev/github.com/usama8199/errordebugger-go) |
| 🐘 **PHP** | `errordebugger/errordebugger-php` | [Packagist](https://packagist.org/packages/errordebugger/errordebugger-php) | [![Packagist](https://img.shields.io/packagist/v/errordebugger/errordebugger-php)](https://packagist.org/packages/errordebugger/errordebugger-php) |
| ☕ **Java** | `io.github.usama8199:errordebugger-java` | [Maven Central](https://central.sonatype.com/) | [![Maven Central](https://img.shields.io/maven-central/v/io.github.usama8199/errordebugger-java)](https://central.sonatype.com/) |
| #️⃣ **C#** | `ErrorDebugger` | [NuGet](https://www.nuget.org/packages/ErrorDebugger) | [![NuGet](https://img.shields.io/nuget/v/ErrorDebugger)](https://www.nuget.org/packages/ErrorDebugger) |
| 💎 **Ruby** | `errordebugger` | [RubyGems](https://rubygems.org/gems/errordebugger) | [![Gem](https://img.shields.io/gem/v/errordebugger)](https://rubygems.org/gems/errordebugger) |

## 🚀 Installation

### Python
```bash
pip install errordebugger
```

### JavaScript
```bash
npm install errordebugger-js
```

### Go
```bash
go get github.com/usama8199/errordebugger-go
```

### Rust
```toml
# Cargo.toml
[dependencies]
errordebugger = "0.2.3"
```

### PHP
```bash
composer require errordebugger/errordebugger-php
```

### Java
```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.github.usama8199</groupId>
    <artifactId>errordebugger-java</artifactId>
    <version>0.2.3</version>
</dependency>
```

### C#
```bash
dotnet add package ErrorDebugger
```

### Ruby
```ruby
# Gemfile
gem 'errordebugger'
```

## 🛠️ Development & Contributing

This repository is organized as a monorepo. Each language SDK resides in its own top-level directory.

### Directory Structure
- `python/`: Python SDK source & tests
- `javascript/`: Node.js SDK
- `go/`: Go module
- `rust/`: Rust crate
- ... (etc)
- `.github/workflows/`: CI/CD definitions

### Workflow
1.  **Clone**: `git clone https://github.com/usama8199/errordebugger-sdks.git`
2.  **Edit**: Modify the source code in the respective language folder.
3.  **Test**: Run local tests (e.g., `cd python && pytest`, `cd rust && cargo test`).
4.  **Release**:
    - Update the version number in the config file (e.g., `package.json`, `setup.py`).
    - Commit and Push.
    - Create a Git Tag (e.g., `v0.2.5`) to trigger auto-publishing.

## 📚 Documentation

Detailed guides for maintaining and scaling this repository:

- [**Setup & Maintenance**](docs/MONOREPO_SETUP.md): How to recreate this repo, configure secrets, and manage the pipeline.
- [**Architecture & CI/CD**](docs/ARCHITECTURE.md): Deep dive into the GitHub Actions workflow and internal structure.
- [**Release Process**](docs/RELEASE.md): Step-by-step checklist for releasing new versions.

## 📄 License

MIT License. See [LICENSE](./LICENSE) for details.
