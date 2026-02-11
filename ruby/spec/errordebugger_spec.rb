require "errordebugger"

RSpec.describe ErrorDebugger do
  before(:each) do
    ErrorDebugger.reset
  end

  it "validates config" do
    expect { ErrorDebugger::Config.new(dsn: "", token: "token") }.to raise_error(ArgumentError)
    expect { ErrorDebugger::Config.new(dsn: "dsn", token: "") }.to raise_error(ArgumentError)
  end

  it "initializes without error" do
    config = ErrorDebugger::Config.new(dsn: "http://localhost:8000", token: "test-token")
    expect { ErrorDebugger.init(config) }.not_to raise_error
  end

  it "captures exception without error" do
    config = ErrorDebugger::Config.new(dsn: "http://localhost:8000", token: "test-token")
    ErrorDebugger.init(config)
    
    expect {
      begin
        raise StandardError, "Test error"
      rescue => e
        ErrorDebugger.capture_exception(e)
      end
    }.not_to raise_error
  end
end
