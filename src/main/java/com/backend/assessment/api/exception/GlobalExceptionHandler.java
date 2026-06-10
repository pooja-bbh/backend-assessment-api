package com.backend.assessment.api.exception;

import com.backend.assessment.api.dto.response.ErrorResponse;
import com.backend.assessment.common.logging.CorrelationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred.";
    private static final String NOT_FOUND_MESSAGE = "The requested resource was not found.";
    private static final String SERVICE_UNAVAILABLE_MESSAGE =
            "The policy service is temporarily unavailable. Please try again later.";

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException exception) {
        String correlationId = MDC.get(CorrelationId.MDC_KEY);
        LOGGER.error("Data access failure. correlationId={} type={} message={}",
                correlationId, exception.getClass().getSimpleName(), exception.getMessage(), exception);
        ErrorResponse body = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                SERVICE_UNAVAILABLE_MESSAGE,
                correlationId);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(NoResourceFoundException exception) {
        String correlationId = MDC.get(CorrelationId.MDC_KEY);
        LOGGER.warn("Resource not found. correlationId={} path={}", correlationId, exception.getResourcePath());
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                NOT_FOUND_MESSAGE,
                correlationId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        String correlationId = MDC.get(CorrelationId.MDC_KEY);
        LOGGER.error("Unhandled exception. correlationId={} type={} message={}",
                correlationId, exception.getClass().getSimpleName(), exception.getMessage(), exception);
        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                GENERIC_ERROR_MESSAGE,
                correlationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
