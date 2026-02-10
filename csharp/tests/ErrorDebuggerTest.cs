using ErrorDebugger;
using Xunit;

namespace ErrorDebugger.Tests;

public class ErrorDebuggerTest
{
    [Fact]
    public void TestInit_MissingConfig_Throws()
    {
        var config = new Config { Dsn = "", Token = "token" };
        Assert.Throws<ArgumentException>(() => ErrorDebugger.Init(config));
        
        config = new Config { Dsn = "dsn", Token = "" };
        Assert.Throws<ArgumentException>(() => ErrorDebugger.Init(config));
    }

    [Fact]
    public void TestInit_ValidConfig_Succeeds()
    {
        var config = new Config { Dsn = "http://localhost:8000", Token = "test-token" };
        // Just verify it doesn't throw.
        var exception = Record.Exception(() => ErrorDebugger.Init(config));
        Assert.Null(exception);
    }
    
    [Fact]
    public void TestCapture_Succeeds()
    {
        // Even if not initialized or initialized (singleton state might persist in test runner), should be safe
        var exception = Record.Exception(() => ErrorDebugger.CaptureException(new Exception("Test")));
        Assert.Null(exception);
    }
}
