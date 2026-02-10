package com.errordebugger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorDebuggerTest {

    @BeforeEach
    void setup() {
        ErrorDebugger.reset();
    }

    @Test
    void testConfigValidation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Config(null, "token");
        });
        assertTrue(exception.getMessage().contains("DSN required"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Config("dsn", "");
        });
        assertTrue(exception.getMessage().contains("Token required"));
    }

    @Test
    void testInit() {
        Config config = new Config("http://localhost:8000", "test-token");
        // Just verify it doesn't throw.
        // Real connection test requires integration setup or extensive mocking which
        // OTel makes tricky in simple units.
        assertDoesNotThrow(() -> ErrorDebugger.init(config));
    }

    @Test
    void testCapture() {
        Config config = new Config("http://localhost:8000", "test-token");
        ErrorDebugger.init(config);

        assertDoesNotThrow(() -> ErrorDebugger.captureException(new RuntimeException("Test")));
    }
}
