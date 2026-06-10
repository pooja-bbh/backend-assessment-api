package com.backend.assessment.api.dto.response;

/**
 * Standard error payload returned by the API on failures.
 */
public record ErrorResponse(int status, String message, String correlationId) {
}
