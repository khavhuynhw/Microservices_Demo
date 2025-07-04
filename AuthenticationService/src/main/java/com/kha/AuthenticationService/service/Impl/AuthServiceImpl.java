package com.kha.AuthenticationService.service.Impl;

import com.kha.AuthenticationService.dto.JwtResponseDTO;
import com.kha.AuthenticationService.dto.LoginRequestDTO;
import com.kha.AuthenticationService.dto.RegisterRequestDTO;
import com.kha.AuthenticationService.model.Role;
import com.kha.AuthenticationService.model.User;
import com.kha.AuthenticationService.repo.UserRepository;
import com.kha.AuthenticationService.security.JwtTokenProvider;
import com.kha.AuthenticationService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;


    @Override
    @Transactional
    public Integer registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address already in use!");
        }

        User user = new User(
                null, // ID generated by DB
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole() != null ? Role.valueOf(request.getRole().toUpperCase()) : Role.CUSTOMER,
                User.Status.ACTIVE
        );

        User savedUser = userRepository.save(user);
        return savedUser.getUserId();
    }

    @Override
    @Transactional
    public JwtResponseDTO authenticateUser(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        User user = (User) authentication.getPrincipal();

        return new JwtResponseDTO(jwt, user.getUserId(), user.getEmail(), user.getRole().name());
    }

    @Override
    @Transactional
    public User createUser(String email, String password, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email address already in use!");
        }
        User user = new User(null, email, passwordEncoder.encode(password), role, User.Status.ACTIVE);
        return userRepository.save(user);
    }
}
