package com.bandrehearsal.bandrehearsalmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/** Settings under "app.cors": the front-end origins allowed to call the API. */
@ConfigurationProperties(prefix = "app.cors")
public record CorsProperties(List<String> allowedOrigins) {
}
