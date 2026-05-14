package com.gestionfournisseurs.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Réponses JSON homogènes pour les erreurs de validation et les requêtes mal formées.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<ApiErrorResponse.FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toViolation)
                .collect(Collectors.toList());

        for (var ge : ex.getBindingResult().getGlobalErrors()) {
            violations.add(new ApiErrorResponse.FieldViolation(
                    ge.getObjectName(),
                    null,
                    ge.getDefaultMessage()));
        }

        ApiErrorResponse body = ApiErrorResponse.validation(request.getRequestURI(), violations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private ApiErrorResponse.FieldViolation toViolation(FieldError fe) {
        return new ApiErrorResponse.FieldViolation(
                fe.getField(),
                fe.getRejectedValue(),
                fe.getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {
        List<ApiErrorResponse.FieldViolation> violations = new ArrayList<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            String path = v.getPropertyPath() != null ? v.getPropertyPath().toString() : "paramètre";
            violations.add(new ApiErrorResponse.FieldViolation(
                    path,
                    v.getInvalidValue(),
                    v.getMessage()));
        }
        ApiErrorResponse body = ApiErrorResponse.validation(request.getRequestURI(), violations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        String detail = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : "Corps de requête illisible";
        ApiErrorResponse body = ApiErrorResponse.badRequest(
                request.getRequestURI(),
                "JSON invalide ou incompatible : " + detail);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String msg = String.format(
                "Paramètre '%s' invalide : valeur '%s'",
                ex.getName(),
                ex.getValue());
        ApiErrorResponse body = ApiErrorResponse.badRequest(request.getRequestURI(), msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        ApiErrorResponse body = ApiErrorResponse.badRequest(request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        String detail = ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();
        String msg = "Contrainte base de données (référence invalide ou doublon)."
                + (detail != null ? " Détail : " + detail : "");
        ApiErrorResponse body = ApiErrorResponse.badRequest(request.getRequestURI(), msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
