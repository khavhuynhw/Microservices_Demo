package com.kha.AuthenticationService.service;

import com.kha.AuthenticationService.dto.JwtResponseDTO;
import com.kha.AuthenticationService.dto.LoginRequestDTO;
import com.kha.AuthenticationService.dto.RegisterRequestDTO;
import com.kha.AuthenticationService.model.Role;
import com.kha.AuthenticationService.model.User;

public interface AuthService {

    Integer registerUser(RegisterRequestDTO request);
    JwtResponseDTO authenticateUser(LoginRequestDTO request);
    User createUser(String email, String password, Role role);
}
