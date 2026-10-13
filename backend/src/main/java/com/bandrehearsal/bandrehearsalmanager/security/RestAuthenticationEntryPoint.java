package com.bandrehearsal.bandrehearsalmanager.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** Answers 401 Unauthorized when a protected endpoint is called without a valid access token. */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        Object jwtError = request.getAttribute(JwtAuthenticationFilter.JWT_ERROR_ATTRIBUTE);
        if (jwtError != null) {
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                    "Bearer error=\"invalid_token\", error_description=\"" + jwtError + "\"");
            ProblemResponses.write(request, response, HttpStatus.UNAUTHORIZED, jwtError.toString());
        } else {
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
            ProblemResponses.write(request, response, HttpStatus.UNAUTHORIZED,
                    "Authentication is required to access this resource");
        }
    }
}
