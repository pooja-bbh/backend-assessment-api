package com.backend.assessment.api.exception;

import com.backend.assessment.api.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global @RestControllerAdvice handler that converts uncaught exceptions
 * into a standard {@link ErrorResponse}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String CORRELATION_ID_KEY = "correlationId";
    private static final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        String correlationId = MDC.get(CORRELATION_ID_KEY);
        LOGGER.error("Unhandled exception. correlationId={} type={} message={}",
                correlationId, exception.getClass().getSimpleName(), exception.getMessage(), exception);
        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                GENERIC_ERROR_MESSAGE,
                correlationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
