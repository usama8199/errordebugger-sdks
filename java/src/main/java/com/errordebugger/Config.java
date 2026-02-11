package com.errordebugger;

public class Config {
    private String dsn;
    private String token;
    private String serviceName = "java-app";
    private String environment = "production";

    public Config(String dsn, String token) {
        if (dsn == null || dsn.isEmpty())
            throw new IllegalArgumentException("DSN required");
        if (token == null || token.isEmpty())
            throw new IllegalArgumentException("Token required");
        this.dsn = dsn;
        this.token = token;
    }

    public String getDsn() {
        return dsn;
    }

    public String getToken() {
        return token;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
