using System;

namespace ErrorDebugger
{
    public class Config
    {
        public string Dsn { get; set; } = string.Empty;
        public string Token { get; set; } = string.Empty;
        public string ServiceName { get; set; } = "dotnet-service";
        public string Environment { get; set; } = "production";
        public string? Release { get; set; }
        public bool Debug { get; set; } = false;

        public void Validate()
        {
            if (string.IsNullOrWhiteSpace(Dsn))
                throw new ArgumentException("DSN is required", nameof(Dsn));
            
            if (string.IsNullOrWhiteSpace(Token))
                throw new ArgumentException("Token is required", nameof(Token));
        }
    }
}
