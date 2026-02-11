namespace ErrorDebugger;

public class Config
{
    public string Dsn { get; set; } = string.Empty;
    public string Token { get; set; } = string.Empty;
    public string? ServiceName { get; set; } = "csharp-app";
    public string Environment { get; set; } = "production";
}
