package com.ricardolovato.usermanagement.exception;

import java.util.stream.Collectors;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

public final class ValidationMessageFormatter {

    private ValidationMessageFormatter() {}

    public static String format(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidationMessageFormatter::formatFieldError)
                .collect(Collectors.joining("; "));

        if (details.isBlank()) return "Validation failed";
        return "Validation failed: " + details;
    }

    private static String formatFieldError(FieldError fe) {
        return fe.getField() + ": " + fe.getDefaultMessage();
    }
}