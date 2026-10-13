package com.bandrehearsal.bandrehearsalmanager.config;

import com.bandrehearsal.bandrehearsalmanager.models.AppUser;
import com.bandrehearsal.bandrehearsalmanager.models.Role;
import com.bandrehearsal.bandrehearsalmanager.repositories.IAppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Creates the demo ADMIN and USER accounts on startup if they do not exist yet.
 * Passwords are hashed here, so no password hashes have to live in the SQL script.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final SeedProperties seed;
    private final IAppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(SeedProperties seed, IAppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.seed = seed;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!seed.enabled()) {
            return;
        }
        createIfMissing(seed.adminUsername(), seed.adminPassword(), Role.ADMIN);
        createIfMissing(seed.userUsername(), seed.userPassword(), Role.USER);
    }

    private void createIfMissing(String username, String password, Role role) {
        if (username == null || username.isBlank() || userRepository.existsByUsername(username)) {
            return;
        }
        userRepository.save(new AppUser(username, passwordEncoder.encode(password), role));
        log.info("Created demo account '{}' with role {}", username, role);
    }
}
