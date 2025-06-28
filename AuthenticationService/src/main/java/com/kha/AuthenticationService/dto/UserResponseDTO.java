package com.kha.AuthenticationService.dto;

import com.kha.AuthenticationService.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Integer userId;
    private String email;
    private Role role;
}
