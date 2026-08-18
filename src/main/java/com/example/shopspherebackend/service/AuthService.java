package com.example.shopspherebackend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopspherebackend.dto.AuthResponseDTO;
import com.example.shopspherebackend.dto.LoginRequestDTO;
import com.example.shopspherebackend.dto.RefreshTokenResponseDTO;
import com.example.shopspherebackend.dto.UserRequestDTO;
import com.example.shopspherebackend.dto.UserResponseDTO;
import com.example.shopspherebackend.entity.Role;
import com.example.shopspherebackend.entity.User;
import com.example.shopspherebackend.repository.UserRepository;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserService userService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public UserResponseDTO register(UserRequestDTO request) {
        Role role = request.role() == null ? Role.CUSTOMER : request.role();

        return userService.createUser(new UserRequestDTO(
                request.name(),
                request.email(),
                request.password(),
                request.phone(),
                role));
    }

    @Transactional
    public LoginResult login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new SecurityException("Invalid email or password"));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new SecurityException("User account is disabled");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new SecurityException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.issueToken(user);

        return new LoginResult(
                new AuthResponseDTO(
                        accessToken,
                        "Bearer",
                        jwtService.getAccessTokenExpirationSeconds(),
                        toResponseDTO(user)),
                refreshToken);
    }

    @Transactional
    public RefreshResult refresh(String refreshToken) {
        User user = refreshTokenService.validate(refreshToken);
        String newRefreshToken = refreshTokenService.rotate(refreshToken);
        String accessToken = jwtService.generateAccessToken(user);

        return new RefreshResult(
                new RefreshTokenResponseDTO(
                        accessToken,
                        "Bearer",
                        jwtService.getAccessTokenExpirationSeconds()),
                newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }

    public long getRefreshTokenMaxAgeSeconds() {
        return refreshTokenService.getRefreshTokenMaxAgeSeconds();
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public record LoginResult(AuthResponseDTO response, String refreshToken) {
    }

    public record RefreshResult(RefreshTokenResponseDTO response, String refreshToken) {
    }
}
