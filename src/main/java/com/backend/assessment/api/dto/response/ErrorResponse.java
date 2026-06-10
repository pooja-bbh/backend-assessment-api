package com.backend.assessment.api.dto.response;

public record ErrorResponse(int status, String message, String correlationId) {
}
