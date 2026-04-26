package com.userService.UserManagementService.service;


import com.userService.UserManagementService.dto.LoginRequestDto;
import com.userService.UserManagementService.dto.LoginResponseDto;
import com.userService.UserManagementService.model.UserModel;
import com.userService.UserManagementService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    private final UserRepository repo;

    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto loginService(LoginRequestDto request) {

        Optional<UserModel> userModel=repo.findByEmail(request.getEmail());
        if(userModel.isPresent()){
            UserModel userModel1=userModel.get();
            boolean isMatch=passwordEncoder.matches(
                    request.getPassword(),
                    userModel1.getPassword()
            );
            if(isMatch){
                String role=userModel1.getRole().name().toUpperCase();
                String token= jwtService.generateToken(
                        request.getEmail(),
                        role

                );
                return new LoginResponseDto("success",token,userModel1.getEmail(),role);
            }
            return new LoginResponseDto("failure", null, null, null);

        }
        return new LoginResponseDto("failure", null, null, null);
    }
}
