use errordebugger::{init, Config};

#[tokio::test]
async fn test_init() {
    let config = Config {
        dsn: "http://localhost:8000".to_string(),
        token: "test-token".to_string(),
        service_name: Some("test-service".to_string()),
        environment: "test".to_string(),
    };

    let result = init(config).await;
    assert!(result.is_ok());
}

#[tokio::test]
async fn test_init_missing_config() {
    let config = Config {
        dsn: "".to_string(),
        token: "test-token".to_string(),
        service_name: None,
        environment: "test".to_string(),
    };

    let result = init(config).await;
    assert!(result.is_err());
}
