package com.gestionfournisseurs.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Corps JSON standard pour les erreurs renvoyées par l'API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private final Instant timestamp = Instant.now();
    private final int status;
    private final String error;
    private final String path;
    private final List<FieldViolation> violations;

    public ApiErrorResponse(int status, String error, String path, List<FieldViolation> violations) {
        this.status = status;
        this.error = error;
        this.path = path;
        this.violations = violations == null || violations.isEmpty() ? null : violations;
    }

    public static ApiErrorResponse validation(String path, List<FieldViolation> violations) {
        return new ApiErrorResponse(400, "Erreur de validation", path, violations);
    }

    public static ApiErrorResponse badRequest(String path, String message) {
        return new ApiErrorResponse(400, message, path, Collections.emptyList());
    }

    public static ApiErrorResponse unauthorized(String path, String message) {
        return new ApiErrorResponse(401, message, path, Collections.emptyList());
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }

    public List<FieldViolation> getViolations() {
        return violations;
    }

    public static class FieldViolation {
        private final String field;
        private final Object rejectedValue;
        private final String message;

        public FieldViolation(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public String getMessage() {
            return message;
        }
    }
}
