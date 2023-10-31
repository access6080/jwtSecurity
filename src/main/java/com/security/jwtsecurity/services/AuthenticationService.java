package com.security.jwtsecurity.services;

import com.security.jwtsecurity.config.security.JwtService;
import com.security.jwtsecurity.dtos.AuthenticationRequest;
import com.security.jwtsecurity.dtos.AuthenticationResponse;
import com.security.jwtsecurity.dtos.RegisterRequest;
import com.security.jwtsecurity.models.UserEntity;
import com.security.jwtsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        UserEntity user = UserEntity.builder()
                .firstname(request.getFirstname())
                .id(1L)
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        repo.save(user);

        String token = jwtService.generateToken(user);

        return AuthenticationResponse
                .builder()
                .accessToken(token)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserEntity user = repo.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return AuthenticationResponse
                .builder()
                .accessToken(token)
                .build();
    }
}
