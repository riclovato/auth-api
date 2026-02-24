package com.ricardolovato.usermanagement.exception;

import org.springframework.dao.DataIntegrityViolationException;

public final class UniqueConstraintMessageResolver {

    private UniqueConstraintMessageResolver() {}

    public static String resolve(DataIntegrityViolationException ex) {
        String rootMsg = getRootMessage(ex);

        
        if (containsIgnoreCase(rootMsg, "uk_users_email")) {
            return "Email already registered";
        }
        if (containsIgnoreCase(rootMsg, "uk_users_username")) {
            return "Username already registered";
        }

        if (containsIgnoreCase(rootMsg, "email")) {
            return "Email already registered";
        }
        if (containsIgnoreCase(rootMsg, "username")) {
            return "Username already registered";
        }

        return "Resource already exists";
    }

    private static String getRootMessage(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null && cur.getCause() != cur) {
            cur = cur.getCause();
        }
        return cur.getMessage() == null ? "" : cur.getMessage();
    }

    private static boolean containsIgnoreCase(String text, String target) {
        return text != null && target != null && text.toLowerCase().contains(target.toLowerCase());
    }
}