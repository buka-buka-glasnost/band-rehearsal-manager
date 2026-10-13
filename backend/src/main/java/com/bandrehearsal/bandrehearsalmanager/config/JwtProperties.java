package com.bandrehearsal.bandrehearsalmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Settings under "app.jwt" in application.properties.
 *
 * @param secret          Base64-encoded HMAC-SHA key used to sign access tokens
 * @param accessTokenTtl  lifetime of an access token (short, e.g. 15m)
 * @param refreshTokenTtl lifetime of a refresh token (long, e.g. 7d)
 */
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String secret, Duration accessTokenTtl, Duration refreshTokenTtl) {
}
