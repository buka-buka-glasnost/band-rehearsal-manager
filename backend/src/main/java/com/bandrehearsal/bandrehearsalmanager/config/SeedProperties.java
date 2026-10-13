package com.bandrehearsal.bandrehearsalmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Settings under "app.seed": demo accounts created on startup when they do not exist yet. */
@ConfigurationProperties(prefix = "app.seed")
public record SeedProperties(boolean enabled,
                             String adminUsername,
                             String adminPassword,
                             String userUsername,
                             String userPassword) {
}
