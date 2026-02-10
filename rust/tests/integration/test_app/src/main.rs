/// Integration test app for Rust SDK
use errordebugger::{Config, init, capture_error, shutdown};
use std::env;
use std::io;

#[tokio::main]
async fn main() {
    let args: Vec<String> = env::args().collect();
    
    if args.len() < 3 {
        eprintln!("Usage: test_app <dsn> <token>");
        std::process::exit(1);
    }
    
    let dsn = &args[1];
    let token = &args[2];
    
    // Initialize SDK
    let config = Config {
        dsn: dsn.to_string(),
        token: token.to_string(),
        service_name: Some("rust-integration-test".to_string()),
        debug: true,
        ..Default::default()
    };
    
    if let Err(e) = init(config) {
        eprintln!("Init failed: {}", e);
        std::process::exit(1);
    }
    
    // Trigger a test error
    let err = io::Error::new(io::ErrorKind::Other, "Integration test error from Rust SDK");
    capture_error(&err);
    
    // Allow time for export
    tokio::time::sleep(tokio::time::Duration::from_secs(2)).await;
    shutdown();
    println!("Rust SDK test completed");
}
