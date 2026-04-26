package com.userService.UserManagementService.dto.loginDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String status;
    private String token;
    private String email;
    private String role;
}
