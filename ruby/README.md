# ErrorDebugger Ruby SDK

A simplified Ruby SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration.

## Installation

Add to your Gemfile:

```ruby
gem 'errordebugger'
```

And execute:

    $ bundle install

## Usage

```ruby
require 'errordebugger'

# Initialize SDK
config = ErrorDebugger::Config.new(
  dsn: 'http://localhost:8000',
  token: 'your-project-token',
  service_name: 'ruby-app'
)

ErrorDebugger.init(config)

# Manual capture
begin
  raise StandardError, "Something went wrong"
rescue => e
  ErrorDebugger.capture_exception(e)
end
```
