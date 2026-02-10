<?php
namespace ErrorDebugger;

class Config
{
    public string $dsn;
    public string $token;
    public string $serviceName;
    public string $environment;

    public function __construct(
        string $dsn,
        string $token,
        string $serviceName = 'php-app',
        string $environment = 'production'
    ) {
        if (empty($dsn)) throw new \InvalidArgumentException("DSN is required");
        if (empty($token)) throw new \InvalidArgumentException("Token is required");

        $this->dsn = $dsn;
        $this->token = $token;
        $this->serviceName = $serviceName;
        $this->environment = $environment;
    }
}
