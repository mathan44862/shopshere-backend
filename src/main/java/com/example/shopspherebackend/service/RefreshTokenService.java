package com.example.shopspherebackend.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopspherebackend.entity.RefreshToken;
import com.example.shopspherebackend.entity.User;
import com.example.shopspherebackend.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    private final RefreshTokenRepository repository;
    private final long refreshTokenExpirationDays;

    public RefreshTokenService(
            RefreshTokenRepository repository,
            @Value("${app.security.refresh-token-expiration-days:7}") long refreshTokenExpirationDays) {
        this.repository = repository;
        this.refreshTokenExpirationDays = refreshTokenExpirationDays;
    }

    @Transactional
    public String issueToken(User user) {
        repository.deleteByExpiresAtBefore(LocalDateTime.now());

        String rawToken = generateOpaqueToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hash(rawToken));
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(refreshTokenExpirationDays));
        refreshToken.setRevoked(false);
        repository.save(refreshToken);
        return rawToken;
    }

    @Transactional
    public User validate(String rawToken) {
        RefreshToken refreshToken = repository.findByTokenHashAndRevokedFalse(hash(rawToken))
                .orElseThrow(() -> new SecurityException("Invalid refresh token"));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshToken.setRevoked(true);
            repository.save(refreshToken);
            throw new SecurityException("Refresh token expired");
        }

        return refreshToken.getUser();
    }

    @Transactional
    public String rotate(String rawToken) {
        RefreshToken refreshToken = repository.findByTokenHashAndRevokedFalse(hash(rawToken))
                .orElseThrow(() -> new SecurityException("Invalid refresh token"));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshToken.setRevoked(true);
            repository.save(refreshToken);
            throw new SecurityException("Refresh token expired");
        }

        refreshToken.setRevoked(true);
        repository.save(refreshToken);
        return issueToken(refreshToken.getUser());
    }

    @Transactional
    public void revoke(String rawToken) {
        repository.findByTokenHashAndRevokedFalse(hash(rawToken))
                .ifPresent(token -> {
                    token.setRevoked(true);
                    repository.save(token);
                });
    }

    public long getRefreshTokenMaxAgeSeconds() {
        return refreshTokenExpirationDays * 24 * 60 * 60;
    }

    private String generateOpaqueToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return HEX_FORMAT.formatHex(bytes);
    }

    private String hash(String rawToken) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return HEX_FORMAT.formatHex(messageDigest.digest(rawToken.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to hash refresh token", ex);
        }
    }
}
