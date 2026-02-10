import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Minimal Java test that sends OTLP-like trace data via HTTP
 */
public class SimpleHttpOtlpTest {
   public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java SimpleHttpOtlpTest <otlp-endpoint> <token>");
            System.exit(1);
        }

        String endpoint = args[0] + "/v1/otlp/traces";
        String token = args[1];

        // Minimal OTLP JSON payload
        String jsonPayload = "{"
            + "\"resourceSpans\":[{"
            + "\"resource\":{"
            + "\"attributes\":[{"
            + "\"key\":\"service.name\","
            + "\"value\":{\"stringValue\":\"java-integration-test\"}"
            + "},{"
            + "\"key\":\"telemetry.sdk.language\","
            + "\"value\":{\"stringValue\":\"java\"}"
            + "}]"
            + "},"
            + "\"scopeSpans\":[{"
            + "\"scope\":{"
            + "\"name\":\"java-integration-test\","
            + "\"version\":\"1.0.0\""
            + "},"
            + "\"spans\":[{"
            + "\"traceId\":\"" + generateHex(16) + "\","
            + "\"spanId\":\"" + generateHex(8) + "\","
            + "\"name\":\"integration-test-operation\","
            + "\"kind\":1,"
            + "\"startTimeUnixNano\":\"" + System.nanoTime() + "\","
            + "\"endTimeUnixNano\":\"" + (System.nanoTime() + 1000000) + "\","
            + "\"status\":{\"code\":2,\"message\":\"Integration test error from Java SDK\"},"
            + "\"attributes\":[{"
            + "\"key\":\"exception.type\","
            + "\"value\":{\"stringValue\":\"RuntimeException\"}"
            + "},{"
            + "\"key\":\"exception.message\","
            + "\"value\":{\"stringValue\":\"Integration test error from Java SDK\"}"
            + "},{"
            + "\"key\":\"level\","
            + "\"value\":{\"stringValue\":\"error\"}"
            + "}]"
            + "}]"
            + "}]"
            + "}]"
            + "}";

        // Send HTTP POST
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Project-Token", token);
            conn.setDoOutput(true);

            // Write payload
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Java SDK test completed");
            } else {
                System.err.println("Request failed with code: " + responseCode);
                System.exit(1);
            }
        } finally {
            conn.disconnect();
        }
    }

    private static String generateHex(int bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes; i++) {
            sb.append(String.format("%02x", (int)(Math.random() * 256)));
        }
        return sb.toString();
    }
}
