package com.example.shopspherebackend.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.shopspherebackend.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JwtService {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;
    private final byte[] secretKey;
    private final long accessTokenExpirationSeconds;

    public JwtService(
            ObjectMapper objectMapper,
            @Value("${app.security.jwt.secret:shopsphere-dev-jwt-secret-key-change-me}") String secret,
            @Value("${app.security.jwt.access-token-expiration-seconds:900}") long accessTokenExpirationSeconds) {
        this.objectMapper = objectMapper;
        this.secretKey = secret.getBytes(StandardCharsets.UTF_8);
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(accessTokenExpirationSeconds);

        Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", String.valueOf(user.getId()));
        payload.put("email", user.getEmail());
        payload.put("role", user.getRole().name());
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", expiresAt.getEpochSecond());

        String encodedHeader = encodeJson(header);
        String encodedPayload = encodeJson(payload);
        String unsignedToken = encodedHeader + "." + encodedPayload;

        return unsignedToken + "." + sign(unsignedToken);
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    public Map<String, Object> validateAccessToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new SecurityException("Invalid JWT format");
            }

            String unsignedToken = parts[0] + "." + parts[1];
            String expectedSignature = sign(unsignedToken);
            if (!expectedSignature.equals(parts[2])) {
                throw new SecurityException("Invalid JWT signature");
            }

            Map<String, Object> claims = decodeJson(parts[1]);
            long exp = ((Number) claims.getOrDefault("exp", 0)).longValue();
            if (exp <= Instant.now().getEpochSecond()) {
                throw new SecurityException("JWT expired");
            }

            return claims;
        } catch (IllegalArgumentException ex) {
            throw new SecurityException("Invalid JWT token", ex);
        }
    }

    public String extractEmail(String token) {
        return String.valueOf(validateAccessToken(token).getOrDefault("email", ""));
    }

    public String extractRole(String token) {
        return String.valueOf(validateAccessToken(token).getOrDefault("role", ""));
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            return URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize JWT payload", ex);
        }
    }

    private Map<String, Object> decodeJson(String value) {
        try {
            byte[] decoded = URL_DECODER.decode(value);
            return objectMapper.readValue(decoded, MAP_TYPE);
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            return URL_ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to sign JWT", ex);
        }
    }
}
