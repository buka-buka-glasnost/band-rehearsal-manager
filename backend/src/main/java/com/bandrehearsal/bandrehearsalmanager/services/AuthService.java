package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.config.JwtProperties;
import com.bandrehearsal.bandrehearsalmanager.dto.AuthResponse;
import com.bandrehearsal.bandrehearsalmanager.dto.LoginRequest;
import com.bandrehearsal.bandrehearsalmanager.dto.RegisterRequest;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ConflictException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.InvalidTokenException;
import com.bandrehearsal.bandrehearsalmanager.models.AppUser;
import com.bandrehearsal.bandrehearsalmanager.models.RefreshToken;
import com.bandrehearsal.bandrehearsalmanager.models.Role;
import com.bandrehearsal.bandrehearsalmanager.repositories.IAppUserRepository;
import com.bandrehearsal.bandrehearsalmanager.repositories.IRefreshTokenRepository;
import com.bandrehearsal.bandrehearsalmanager.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class AuthService implements IAuthService {

    private static final String TOKEN_TYPE = "Bearer";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final IAppUserRepository userRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthService(IAppUserRepository userRepository,
                       IRefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    /** New accounts always get the USER role; ADMIN accounts are created by the seeder. */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username '" + request.username() + "' is already taken");
        }
        AppUser user = userRepository.save(
                new AppUser(request.username(), passwordEncoder.encode(request.password()), Role.USER));
        return issueTokens(user);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.username())
                .filter(candidate -> passwordEncoder.matches(request.password(), candidate.getPassword()))
                // Same message for unknown user and wrong password, so usernames cannot be probed
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        return issueTokens(user);
    }

    /**
     * Exchanges a refresh token for a new access token and a new refresh token (rotation).
     * The used refresh token is revoked. If a token that was already revoked is used again,
     * someone may have stolen it, so every session of that user is revoked.
     *
     * noRollbackFor keeps the revocations in the database even though an exception is thrown.
     */
    @Override
    @Transactional(noRollbackFor = InvalidTokenException.class)
    public AuthResponse refresh(String rawRefreshToken) {
        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .orElseThrow(() -> new InvalidTokenException("Refresh token is invalid"));

        if (stored.isRevoked()) {
            refreshTokenRepository.revokeAllForUser(stored.getUser());
            throw new InvalidTokenException("Refresh token has already been used or revoked");
        }
        if (stored.isExpired(Instant.now())) {
            stored.setRevoked(true);
            throw new InvalidTokenException("Refresh token has expired");
        }

        stored.setRevoked(true);
        return issueTokens(stored.getUser());
    }

    /** Revokes the refresh token. Unknown tokens are ignored, so logging out twice is harmless. */
    @Override
    @Transactional
    public void logout(String rawRefreshToken) {
        refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .ifPresent(token -> token.setRevoked(true));
    }

    private AuthResponse issueTokens(AppUser user) {
        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = newRandomToken();
        Instant now = Instant.now();
        refreshTokenRepository.save(new RefreshToken(
                hash(refreshToken), user, now, now.plus(jwtProperties.refreshTokenTtl())));

        return new AuthResponse(accessToken, refreshToken, TOKEN_TYPE,
                jwtService.accessTokenTtlSeconds(), user.getUsername(), user.getRole().name());
    }

    /** 256 random bits, URL-safe Base64. The refresh token is opaque, not a JWT. */
    private static String newRandomToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
