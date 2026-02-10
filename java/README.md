# ErrorDebugger Java SDK

A simplified Java SDK for ErrorDebugger, wrapping OpenTelemetry for easy integration.

## Installation

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.errordebugger</groupId>
    <artifactId>errordebugger-java</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

```java
import com.errordebugger.ErrorDebugger;
import com.errordebugger.Config;

public class Main {
    public static void main(String[] args) {
        // Initialize SDK
        Config config = new Config("http://localhost:8000", "your-project-token");
        ErrorDebugger.init(config);
        
        // Manual capture
        try {
            throw new RuntimeException("Something went wrong");
        } catch (Exception e) {
            ErrorDebugger.captureException(e);
        }
    }
}
```
