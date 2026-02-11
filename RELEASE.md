# SDK Release Process

To release a new version of the SDKs (e.g., `v0.3.0`), you must bump the version numbers in the respective configuration files **before** pushing the Git tag.

## 1. Bump Versions
Update the version string in the following files:

| Language | File Path | Field/Line |
|----------|-----------|------------|
| **Python** | `python/errordebugger/_version.py` | `__version__ = "0.3.0"` |
| **JavaScript** | `javascript/package.json` | `"version": "0.3.0"` |
| **Go** | (No file change needed) | Reference Git tag |
| **Rust** | `rust/Cargo.toml` | `version = "0.3.0"` |
| **Java** | `java/pom.xml` | `<version>0.3.0</version>` |
| **C#** | `csharp/src/ErrorDebugger.csproj` | `<Version>0.3.0</Version>` |
| **PHP** | (No file change needed) | Reference Git tag |
| **Ruby** | `ruby/lib/errordebugger/version.rb` | `VERSION = "0.3.0"` |

## 2. Commit and Push
```bash
git add .
git commit -m "Bump version to 0.3.0"
git push origin main
```

## 3. Trigger Release
Push the tag to trigger the GitHub Actions workflow.

```bash
git tag v0.3.0
git push origin v0.3.0
```

## 4. Verification
Check the [Actions tab](https://github.com/usama8199/errordebugger-sdks/actions) for the "Publish SDKs" workflow.
