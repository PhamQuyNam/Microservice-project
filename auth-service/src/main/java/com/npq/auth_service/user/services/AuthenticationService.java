package com.npq.auth_service.user.services;


import com.npq.auth_service.dto.request.LoginRequestDTO;
import com.npq.auth_service.dto.request.RegisterRequestDTO;
import com.npq.auth_service.dto.response.AuthenticationResponseDTO;
import com.npq.auth_service.dto.response.RegisterResponseDTO;
import com.npq.auth_service.exception.ValidationException;
import com.npq.auth_service.model.Role;
import com.npq.auth_service.model.User;
import com.npq.auth_service.user.repo.UserRepository;
import jakarta.validation.ConstraintViolationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final int TOKEN_INDEX = 7;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDTO register(RegisterRequestDTO registerRequest) {
        //Check email, username is existed in db
        String email = registerRequest.getEmail();
        String userName = registerRequest.getUsername();

        Optional<User> userFoundByEmail = userRepository.findByEmail(email);
        Optional<User> userFoundByUsername = userRepository.findByUsername(userName);

        if (userFoundByEmail.isPresent() || userFoundByUsername.isPresent()) {
            throw new ConstraintViolationException("User already exists!", null);
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.toEnum(registerRequest.getRole()))
                .build();

        userRepository.save(user);

        return RegisterResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("User created")
                .build();
    }

    public AuthenticationResponseDTO login(LoginRequestDTO loginRequest) {
        //Authenticate username and password
        log.info("User request body login:");
        log.info("Login request dto: {}", loginRequest);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        //Generate access token and refresh token
        Optional<User> userFoundByUsername = userRepository.findByUsername(loginRequest.getUsername());
        if (userFoundByUsername.isPresent()) {
            User user = userFoundByUsername.get();
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            //Save access token and refresh token into user in database
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            //Response access token and refresh token to client
            return AuthenticationResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .message("Login successfully")
                    .status(HttpStatus.OK.value())
                    .build();
        } else {
            return AuthenticationResponseDTO.builder()
                    .message("Login failed")
                    .status(HttpStatus.FORBIDDEN.value())
                    .build();
        }
    }

    public AuthenticationResponseDTO refreshToken(String authHeader) throws ValidationException {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return AuthenticationResponseDTO.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthorized!")
                    .build();
        }

        String refreshToken = authHeader.substring(TOKEN_INDEX);

        //Get userName from refreshToken
        String userName = jwtService.extractUsername(refreshToken);
        if (!StringUtils.hasText(userName)) {
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Username is empty");
        }

        //Get User's data from database
        Optional<User> userFoundByUsername = userRepository.findByUsername(userName);
        if (userFoundByUsername.isEmpty()) {
            throw new UsernameNotFoundException(userName);
        }

        User user = userFoundByUsername.get();
        if (!StringUtils.hasText(user.getAccessToken()) || !StringUtils.hasText(user.getRefreshToken())) {
            throw new ValidationException(HttpStatus.UNAUTHORIZED, "Token of the user revoked");
        }

        //Generate access token and refresh token
        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        user.setAccessToken(accessToken);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        //Response access token and refresh token to client
        return AuthenticationResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .message("Refresh token successfully")
                .status(HttpStatus.OK.value())
                .build();
    }
}
