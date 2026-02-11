# ErrorDebugger C# SDK

A simplified C# SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration.

## Installation

Add to your `.csproj`:

```xml
<PackageReference Include="ErrorDebugger" Version="1.0.0" />
```

## Usage

```csharp
using ErrorDebugger;

// Initialize SDK
var config = new Config 
{ 
    Dsn = "http://localhost:8000", 
    Token = "your-project-token",
    ServiceName = "csharp-app"
};
ErrorDebugger.ErrorDebugger.Init(config);

// Manual capture
try 
{
    throw new Exception("Something went wrong");
} 
catch (Exception e) 
{
    ErrorDebugger.ErrorDebugger.CaptureException(e);
}
```
