package com.ricardolovato.usermanagement.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ricardolovato.usermanagement.dto.ErrorResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

        // 400 - erros de regra de negócio (ex.: dados inválidos, fluxo inválido etc.)
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(
                        BusinessException ex,
                        HttpServletRequest request) {
                HttpStatus status = ex.getStatus();

                ErrorResponse body = new ErrorResponse(
                                LocalDateTime.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(status).body(body);
        }

        // 400 - validação do @Valid (erros de campos)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                String message = ValidationMessageFormatter.format(ex);

                ErrorResponse body = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                message,
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        // 400 - JSON inválido / malformado
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleBadJson(
                        HttpMessageNotReadableException ex,
                        HttpServletRequest request) {

                ErrorResponse body = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "Malformed JSON request",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        // 409 - conflitos de unicidade (email/username duplicados etc.)
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
                        DataIntegrityViolationException ex,
                        HttpServletRequest request) {

                String message = UniqueConstraintMessageResolver.resolve(ex);

                ErrorResponse body = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                message,
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        // 404 - entidade não encontrada (se você usa JPA EntityNotFoundException)
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(
                        EntityNotFoundException ex,
                        HttpServletRequest request) {

                ErrorResponse body = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                "Resource not found",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        // 500 - fallback (não vazar stacktrace pro cliente)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneric(
                        Exception ex,
                        HttpServletRequest request) {

                ErrorResponse body = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                "Unexpected error",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
}