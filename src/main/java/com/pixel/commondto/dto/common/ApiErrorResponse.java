package com.pixel.commondto.dto.common;

import org.slf4j.MDC;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public final class ApiErrorResponse {

    private static final String MDC_REQUEST_ID_KEY = "requestId";

    private ApiErrorResponse() {
    }

    public static ErrorResponse create(Throwable e, HttpStatusCode status, String detail) {
        return create(e, status, detail, null);
    }

    public static ErrorResponse create(Throwable e, HttpStatusCode status, String detail, Map<String, Object> extras) {
        ErrorResponse response = ErrorResponse.create(e, status, detail);
        ProblemDetail pd = response.getBody();

        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("traceId", resolveTraceId());

        if (extras != null) extras.forEach(pd::setProperty);

        return response;
    }

    /**
     * Pulls requestId from MDC (set by RequestIdFilter); fallback to a new UUID.
     */
    private static String resolveTraceId() {
        String id = MDC.get(MDC_REQUEST_ID_KEY);
        return id != null ? id : UUID.randomUUID().toString();
    }
}