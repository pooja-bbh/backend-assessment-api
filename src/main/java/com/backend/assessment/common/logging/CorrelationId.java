package com.backend.assessment.common.logging;

public final class CorrelationId {

    public static final String MDC_KEY = "correlationId";
    public static final String HEADER_NAME = "X-Correlation-Id";

    private CorrelationId() {
    }
}
