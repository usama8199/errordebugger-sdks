Gem::Specification.new do |spec|
  spec.name          = "errordebugger"
  spec.version       = "1.0.0"
  spec.authors       = ["ErrorDebugger Team"]
  spec.email         = ["support@errordebugger.com"]

  spec.summary       = "ErrorDebugger SDK for Ruby"
  spec.description   = "A simplified Ruby SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration."
  spec.homepage      = "https://github.com/usama8199/errordebugger-sdks"
  spec.license       = "MIT"

  spec.files         = Dir["lib/**/*", "README.md", "LICENSE"]
  spec.require_paths = ["lib"]

  spec.add_dependency "opentelemetry-sdk", "~> 1.3"
  spec.add_dependency "opentelemetry-exporter-otlp", "~> 0.26"
  spec.add_dependency "opentelemetry-api", "~> 1.2"

  spec.add_development_dependency "bundler", "~> 2.0"
  spec.add_development_dependency "rake", "~> 13.0"
  spec.add_development_dependency "rspec", "~> 3.0"
end
