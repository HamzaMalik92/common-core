# common-core

Common DTO and utility classes for Java / Spring Boot projects.

## What's inside

- Standardized API error responses (RFC 7807 `ProblemDetail`) with request tracing.
- Reusable Bean Validation constraints.
- Logging-safe helpers for masking sensitive values (PII).

## Usage

```xml
<dependency>
    <groupId>com.pixel</groupId>
    <artifactId>common-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Build

```bash
mvn install
```

Tests run with `mvn test` (JUnit 5).

## License

MIT — see [LICENSE](LICENSE).
