package io.errordebugger.integration;

import io.errordebugger.Config;
import io.errordebugger.ErrorDebugger;

/**
 * Integration test app for Java SDK
 */
public class TestApp {
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.err.println("Usage: TestApp <dsn> <token>");
            System.exit(1);
        }
        
        String dsn = args[0];
        String token = args[1];
        
        // Initialize SDK
        Config config = new Config()
            .dsn(dsn)
            .token(token)
            .serviceName("java-integration-test")
            .debug(true);
            
        ErrorDebugger.init(config);
        
        // Trigger a test error
        Exception error = new RuntimeException("Integration test error from Java SDK");
        ErrorDebugger.captureException(error);
        
        // Allow time for export
        Thread.sleep(2000);
        ErrorDebugger.shutdown();
        System.out.println("Java SDK test completed");
    }
}
