using System;
using Xunit;
using ErrorDebugger;
using WireMock.Server;
using WireMock.RequestBuilders;
using WireMock.ResponseBuilders;
using WireMock.Matchers;

namespace ErrorDebugger.Tests
{
    public class ErrorDebuggerTests : IDisposable
    {
        private readonly WireMockServer _wireMockServer;

        public ErrorDebuggerTests()
        {
            _wireMockServer = WireMockServer.Start();
        }

        public void Dispose()
        {
            _wireMockServer?.Stop();
            _wireMockServer?.Dispose();
        }

        [Fact]
        public void Config_Validate_ThrowsOnMissingFields()
        {
            var config = new Config();
            Assert.Throws<ArgumentException>(() => config.Validate());

            config.Dsn = "http://localhost";
            Assert.Throws<ArgumentException>(() => config.Validate());
        }

        [Fact]
        public void Init_And_Capture_WithMockServer()
        {
            // Setup WireMock stub
            _wireMockServer
                .Given(Request.Create()
                    .WithPath("/v1/otlp/traces")
                    .WithHeader("x-project-token", new ExactMatcher("test-token"))
                    .UsingPost())
                .RespondWith(Response.Create()
                    .WithStatusCode(200));

            // Initialize with mock server URL
            ErrorDebugger.Init(new Config
            {
                Dsn = _wireMockServer.Url!,
                Token = "test-token",
                Debug = true,
                ServiceName = "test-service",
                Environment = "test"
            });
            
            // Re-init (should be ignored)
            ErrorDebugger.Init(new Config { Dsn = "ignore", Token = "ignore" });

            // Capture Exception
            var ex = new Exception("Test exception");
            ErrorDebugger.CaptureException(ex);

            // Capture Message
            ErrorDebugger.CaptureMessage("Test message");

            // Capture null (should not crash)
            ErrorDebugger.CaptureException(null!);
            
            // Give time for async export
            System.Threading.Thread.Sleep(500);

            // Shutdown and flush
            ErrorDebugger.Shutdown();

            // Verify at least one request was made
            var logEntries = _wireMockServer.LogEntries;
            Assert.True(logEntries.Count > 0, "Expected at least one HTTP request to the mock server");
            
            // Verify header
            var firstRequest = logEntries[0].RequestMessage;
            Assert.True(firstRequest.Headers.ContainsKey("x-project-token"));
            Assert.Equal("test-token", firstRequest.Headers["x-project-token"].ToString());
        }

        [Fact]
        public void Shutdown_DoesNotCrash()
        {
            ErrorDebugger.Shutdown();
            // Double shutdown should be safe
            ErrorDebugger.Shutdown();
        }
    }
}
