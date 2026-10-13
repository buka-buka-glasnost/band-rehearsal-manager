package com.bandrehearsal.bandrehearsalmanager.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * Writes 401/403 responses from the security layer in the same "problem details" JSON format
 * that GlobalExceptionHandler uses. These errors happen before a controller is reached,
 * so the controller advice cannot handle them.
 */
final class ProblemResponses {

    private ProblemResponses() {
    }

    static void write(HttpServletRequest request, HttpServletResponse response,
                      HttpStatus status, String detail) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/problem+json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{"
                + "\"type\":\"about:blank\","
                + "\"title\":\"" + escape(status.getReasonPhrase()) + "\","
                + "\"status\":" + status.value() + ","
                + "\"detail\":\"" + escape(detail) + "\","
                + "\"instance\":\"" + escape(request.getRequestURI()) + "\""
                + "}");
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
