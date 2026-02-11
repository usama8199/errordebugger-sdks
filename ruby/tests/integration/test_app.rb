#!/usr/bin/env ruby
# Integration test app for Ruby SDK

$LOAD_PATH.unshift File.expand_path('../../lib', __dir__)
require 'errordebugger'

if ARGV.length < 2
  warn 'Usage: test_app.rb <dsn> <token>'
  exit 1
end

dsn = ARGV[0]
token = ARGV[1]

# Initialize SDK
ErrorDebugger.init(
  dsn: dsn,
  token: token,
  service_name: 'ruby-integration-test',
  debug: true
)

# Trigger a test error
error = StandardError.new('Integration test error from Ruby SDK')
ErrorDebugger.capture_exception(error, test: 'integration', language: 'ruby')

# Allow time for export
sleep 2
ErrorDebugger.shutdown
puts 'Ruby SDK test completed'
