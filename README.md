# ErrorDebugger SDKs

Multi-language SDK monorepo for [ErrorDebugger](https://github.com/usama8199/errordebugger-sdks) — an AI-powered error debugging platform.

![Publish SDKs](https://github.com/usama8199/errordebugger-sdks/actions/workflows/publish-sdks.yml/badge.svg)

## SDKs

| Language | Package | Install |
|----------|---------|---------|
| Python | [errordebugger-python](https://pypi.org/project/errordebugger-python/) | `pip install errordebugger-python` |
| JavaScript | [errordebugger-js](https://www.npmjs.com/package/errordebugger-js) | `npm install errordebugger-js` |
| Go | [errordebugger-go](https://pkg.go.dev/github.com/usama8199/errordebugger-go) | `go get github.com/usama8199/errordebugger-go` |
| Rust | [errordebugger](https://crates.io/crates/errordebugger) | `cargo add errordebugger` |
| Java | [errordebugger-java](https://central.sonatype.com/artifact/io.github.usama8199/errordebugger-java) | See Maven snippet below |
| C# | [ErrorDebugger](https://www.nuget.org/packages/ErrorDebugger/) | `dotnet add package ErrorDebugger` |
| PHP | [errordebugger-php](https://packagist.org/packages/errordebugger/errordebugger-php) | `composer require errordebugger/errordebugger-php` |
| Ruby | [errordebugger](https://rubygems.org/gems/errordebugger) | `gem install errordebugger` |

### Java (Maven)

```xml
<dependency>
    <groupId>io.github.usama8199</groupId>
    <artifactId>errordebugger-java</artifactId>
    <version>0.2.3</version>
</dependency>
```

## CI/CD

All SDKs auto-publish via GitHub Actions when you push a version tag:

```bash
git tag v0.2.4
git push origin v0.2.4
```

Or manually trigger from the **Actions** tab → **Publish SDKs** → **Run workflow**.

## License

MIT
