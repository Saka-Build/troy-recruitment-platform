package com.troy.ats.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Centralised error handling for every @RestController.
 *
 * Rule of thumb: client mistakes (4xx) are logged at WARN with the message only,
 * server faults (5xx) at ERROR with the full stacktrace, and the response body
 * never carries the raw exception text for 5xx - that could leak SQL, file paths
 * or connection strings to the caller.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------- application exceptions ----------

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return clientError(HttpStatus.NOT_FOUND, ex.getMessage(), request, ex);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {
        return clientError(HttpStatus.CONFLICT, ex.getMessage(), request, ex);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(ServiceException ex, HttpServletRequest request) {
        return clientError(ex.getStatus(), ex.getMessage(), request, ex);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorage(FileStorageException ex, HttpServletRequest request) {
        return serverError("Could not process the uploaded file", request, ex);
    }

    // ---------- validation ----------

    /** @Valid on a @RequestBody / @RequestPart failed. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyValidation(MethodArgumentNotValidException ex,
                                                              HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldError(
                        fe.getField(), redact(fe.getField(), fe.getRejectedValue()), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        log.warn("Validation failed on {} {} - {}", request.getMethod(), request.getRequestURI(), fieldErrors);

        ErrorResponse body = base(HttpStatus.BAD_REQUEST, "Validation failed", request);
        body.setFieldErrors(fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** @Validated on @RequestParam / @PathVariable failed. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                   HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(v -> new ErrorResponse.FieldError(
                        v.getPropertyPath().toString(),
                        redact(v.getPropertyPath().toString(), v.getInvalidValue()),
                        v.getMessage()))
                .collect(Collectors.toList());

        log.warn("Constraint violation on {} {} - {}", request.getMethod(), request.getRequestURI(), fieldErrors);

        ErrorResponse body = base(HttpStatus.BAD_REQUEST, "Validation failed", request);
        body.setFieldErrors(fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    /** e.g. a malformed UUID in /candidates/{id}. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        String required = ex.getRequiredType() == null ? "expected type" : ex.getRequiredType().getSimpleName();
        String message = "Parameter '" + ex.getName() + "' has invalid value '" + ex.getValue() + "', expected " + required;
        return clientError(HttpStatus.BAD_REQUEST, message, request, ex);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex,
                                                            HttpServletRequest request) {
        return clientError(HttpStatus.BAD_REQUEST, "Missing required parameter: " + ex.getParameterName(), request, ex);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingPart(MissingServletRequestPartException ex,
                                                           HttpServletRequest request) {
        return clientError(HttpStatus.BAD_REQUEST, "Missing required file/part: " + ex.getRequestPartName(), request, ex);
    }

    /** Unparseable or absent JSON body. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex,
                                                              HttpServletRequest request) {
        return clientError(HttpStatus.BAD_REQUEST, "Malformed or missing request body", request, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                               HttpServletRequest request) {
        return clientError(HttpStatus.BAD_REQUEST, ex.getMessage(), request, ex);
    }

    // ---------- uploads ----------

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleUploadTooLarge(MaxUploadSizeExceededException ex,
                                                              HttpServletRequest request) {
        return clientError(HttpStatus.PAYLOAD_TOO_LARGE,
                "File is larger than the 10MB upload limit", request, ex);
    }

    // ---------- security ----------

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex,
                                                              HttpServletRequest request) {
        // Generic message on purpose - do not tell the caller which half was wrong.
        return clientError(HttpStatus.UNAUTHORIZED, "Authentication required or credentials invalid", request, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex,
                                                            HttpServletRequest request) {
        return clientError(HttpStatus.FORBIDDEN, "You do not have permission to perform this action", request, ex);
    }

    // ---------- persistence / routing ----------

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
                                                             HttpServletRequest request) {
        // The root cause usually names the constraint and sometimes column values - log it, never return it.
        log.error("Data integrity violation on {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(base(HttpStatus.CONFLICT, "Request conflicts with existing data", request));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                  HttpServletRequest request) {
        return clientError(HttpStatus.METHOD_NOT_ALLOWED,
                "Method " + ex.getMethod() + " is not supported for this endpoint", request, ex);
    }

    /** Unmapped URL. Boot 3.2+ raises NoResourceFoundException for paths the static handler also misses. */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoHandler(Exception ex, HttpServletRequest request) {
        return clientError(HttpStatus.NOT_FOUND,
                "No endpoint " + request.getMethod() + " " + request.getRequestURI(), request, ex);
    }

    // ---------- catch-all ----------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return serverError("An unexpected error occurred. Quote the traceId when reporting this.", request, ex);
    }

    // ---------- helpers ----------

    /** Field-name fragments whose submitted value must never reach a response body or a log file. */
    private static final List<String> SENSITIVE_FIELDS =
            List.of("password", "secret", "token", "credential", "otp");

    /**
     * Echoing the rejected value is useful for debugging ("you sent 'abc' for age"),
     * but for a password field it writes the caller's actual password into the rolling
     * log files and hands it back in the response. Mask those.
     */
    private static Object redact(String field, Object rejectedValue) {
        if (rejectedValue == null || field == null) {
            return rejectedValue;
        }
        String lower = field.toLowerCase(java.util.Locale.ROOT);
        return SENSITIVE_FIELDS.stream().anyMatch(lower::contains) ? "***" : rejectedValue;
    }

    private ResponseEntity<ErrorResponse> clientError(HttpStatus status, String message,
                                                      HttpServletRequest request, Exception ex) {
        log.warn("{} on {} {} - {}", status.value(), request.getMethod(), request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(status).body(base(status, message, request));
    }

    private ResponseEntity<ErrorResponse> serverError(String message, HttpServletRequest request, Exception ex) {
        log.error("500 on {} {}", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(base(HttpStatus.INTERNAL_SERVER_ERROR, message, request));
    }

    private ErrorResponse base(HttpStatus status, String message, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .traceId(MDC.get("traceId"))
                .build();
    }
}
