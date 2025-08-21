package com.npq.auth_service.controller;


import com.npq.auth_service.dto.request.LoginRequestDTO;
import com.npq.auth_service.dto.request.RegisterRequestDTO;
import com.npq.auth_service.dto.response.AuthenticationResponseDTO;
import com.npq.auth_service.dto.response.RegisterResponseDTO;
import com.npq.auth_service.exception.ValidationException;
import com.npq.auth_service.user.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {
    public final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequest) throws Exception {
        RegisterResponseDTO registerResponse = authenticationService.register(registerRequest);
        return ResponseEntity
                .status(registerResponse.getStatus())
                .body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) throws Exception {
        log.info("Password: {}", loginRequest.getPassword());
        log.info("Username: {}", loginRequest.getUsername());
        AuthenticationResponseDTO authenticationResponse = authenticationService.login(loginRequest);
        return ResponseEntity
                .status(authenticationResponse.getStatus())
                .body(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public AuthenticationResponseDTO
    refreshToken(@RequestHeader("Authorization") String authHeader) throws ValidationException {
        return authenticationService.refreshToken(authHeader);
    }
}
