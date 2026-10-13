package com.bandrehearsal.bandrehearsalmanager.dto;

/**
 * Returned by register, login and refresh.
 *
 * @param accessToken  short-lived JWT, sent as "Authorization: Bearer ..." on every API call
 * @param refreshToken long-lived opaque token, used only to get a new pair from /auth/refresh
 * @param tokenType    always "Bearer"
 * @param expiresIn    access token lifetime in seconds
 * @param username     the logged-in user
 * @param role         USER or ADMIN
 */
public record AuthResponse(String accessToken,
                           String refreshToken,
                           String tokenType,
                           long expiresIn,
                           String username,
                           String role) {
}
