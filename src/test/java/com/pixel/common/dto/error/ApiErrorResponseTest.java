package com.pixel.common.dto.error;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ApiErrorResponse")
class ApiErrorResponseTest {

    private static final String MDC_REQUEST_ID_KEY = "requestId";

    @BeforeEach
    void setUp() {
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Nested
    @DisplayName("create(...) factory")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Create {

        @Test
        @Order(1)
        @DisplayName("create_withoutExtras_populatesStatusDetailTimestampAndTraceId")
        void create_withoutExtras_populatesStatusDetailTimestampAndTraceId() {
            HttpStatusCode status = HttpStatus.BAD_REQUEST;
            Throwable cause = new IllegalArgumentException("bad input");

            ErrorResponse response = ApiErrorResponse.create(cause, status, "invalid request");

            ProblemDetail body = response.getBody();
            assertNotNull(body);
            assertEquals(status.value(), body.getStatus());
            assertEquals("invalid request", body.getDetail());

            Map<String, Object> props = body.getProperties();
            assertNotNull(props);
            assertTrue(props.containsKey("timestamp"));
            assertTrue(props.containsKey("traceId"));

            assertDoesNotThrow(() -> Instant.parse((String) props.get("timestamp")));
        }

        @Test
        @Order(2)
        @DisplayName("create_whenMdcRequestIdPresent_usesItAsTraceId")
        void create_whenMdcRequestIdPresent_usesItAsTraceId() {
            String requestId = "req-12345";
            MDC.put(MDC_REQUEST_ID_KEY, requestId);

            ErrorResponse response = ApiErrorResponse.create(
                    new RuntimeException("boom"), HttpStatus.INTERNAL_SERVER_ERROR, "oops");

            assertEquals(requestId, response.getBody().getProperties().get("traceId"));
        }

        @Test
        @Order(3)
        @DisplayName("create_whenMdcRequestIdAbsent_fallsBackToGeneratedUuid")
        void create_whenMdcRequestIdAbsent_fallsBackToGeneratedUuid() {
            assertNull(MDC.get(MDC_REQUEST_ID_KEY));

            ErrorResponse response = ApiErrorResponse.create(
                    new RuntimeException("boom"), HttpStatus.INTERNAL_SERVER_ERROR, "oops");

            String traceId = (String) response.getBody().getProperties().get("traceId");
            assertNotNull(traceId);
            assertDoesNotThrow(() -> UUID.fromString(traceId));
        }

        @Test
        @Order(4)
        @DisplayName("create_withExtrasMap_copiesEveryEntryIntoProblemDetailProperties")
        void create_withExtrasMap_copiesEveryEntryIntoProblemDetailProperties() {
            Map<String, Object> extras = new HashMap<>();
            extras.put("field", "email");
            extras.put("rejectedValue", "not-an-email");

            ErrorResponse response = ApiErrorResponse.create(
                    new IllegalStateException("x"), HttpStatus.UNPROCESSABLE_ENTITY, "validation failed", extras);

            Map<String, Object> props = response.getBody().getProperties();
            assertEquals("email", props.get("field"));
            assertEquals("not-an-email", props.get("rejectedValue"));
            assertTrue(props.containsKey("timestamp"));
            assertTrue(props.containsKey("traceId"));
        }

        @Test
        @Order(5)
        @DisplayName("create_withNullExtras_doesNotThrowAndStillSetsTimestampAndTraceId")
        void create_withNullExtras_doesNotThrowAndStillSetsTimestampAndTraceId() {
            ErrorResponse response = assertDoesNotThrow(() -> ApiErrorResponse.create(
                    new RuntimeException("x"), HttpStatus.NOT_FOUND, "missing", null));

            Map<String, Object> props = response.getBody().getProperties();
            assertTrue(props.containsKey("timestamp"));
            assertTrue(props.containsKey("traceId"));
        }

        @Test
        @Order(6)
        @DisplayName("create_extraEntryNamedTraceId_overridesResolvedTraceId")
        void create_extraEntryNamedTraceId_overridesResolvedTraceId() {
            MDC.put(MDC_REQUEST_ID_KEY, "from-mdc");
            Map<String, Object> extras = new HashMap<>();
            extras.put("traceId", "from-extras");

            ErrorResponse response = ApiErrorResponse.create(
                    new RuntimeException("x"), HttpStatus.BAD_GATEWAY, "down", extras);

            assertEquals("from-extras", response.getBody().getProperties().get("traceId"));
        }

        @Test
        @Order(7)
        @DisplayName("create_nullThrowable_throwsException")
        void create_nullThrowable_throwsException() {
            assertThrows(Exception.class,
                    () -> ApiErrorResponse.create(null, HttpStatus.BAD_REQUEST, "detail"));
        }
    }
}
