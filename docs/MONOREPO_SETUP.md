# Monorepo Setup & Maintenance Guide

This guide documents how to create, configure, and maintain the `errordebugger-sdks` monorepo.

## 1. Monorepo Creation (Historical Steps)

If you need to recreate this repository from scratch:

1.  **Initialize Repository**:
    ```bash
    mkdir errordebugger-sdks
    cd errordebugger-sdks
    git init
    ```

2.  **Create Directories**:
    Create a folder for each language:
    ```bash
    mkdir python javascript go rust java csharp php ruby
    ```

3.  **Populate SDKs**:
    Copy the SDK code into the respective folders. Ensure each folder is a valid package root (e.g., `python/setup.py` exists, `javascript/package.json` exists).

4.  **Create GitHub Action**:
    Create `.github/workflows/publish-sdks.yml` (see current file for content). This workflow orchestrates the build and publish process.

## 2. GitHub Configuration

### 2.1 Repository Settings
- **Visibility**: Public (for registry access).
- **Actions**: Enabled.

### 2.2 Secrets
You must configure the following secrets in **Settings > Secrets and variables > Actions**:

| Secret | Description |
|--------|-------------|
| `PYPI_API_TOKEN` | PyPI token (Project scope: `errordebugger` or `errordebugger-python`) |
| `NPM_TOKEN` | npm Automation Token (Read/Write, Bypass 2FA) |
| `CRATES_IO_TOKEN` | Crates.io API Token |
| `NUGET_API_KEY` | NuGet API Key |
| `RUBYGEMS_API_KEY` | RubyGems API Key |
| `OSSRH_USERNAME` | Sonatype (Maven Central) Username |
| `OSSRH_PASSWORD` | Sonatype (Maven Central) Password |
| `GPG_PRIVATE_KEY` | GPG Private Key (Ascii Armored) for signing Java artifacts |
| `GPG_PASSPHRASE` | Passphrase for the GPG key |

## 3. Maintenance

- **Release Process**: See [RELEASE.md](./RELEASE.md).
- **Architecture**: See [ARCHITECTURE.md](./ARCHITECTURE.md).
- **Workflow Updates**: Edit `.github/workflows/publish-sdks.yml`.
