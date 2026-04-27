package com.userService.UserManagementService.service;


import com.userService.UserManagementService.dto.loginDto.LoginRequestDto;
import com.userService.UserManagementService.dto.loginDto.LoginResponseDto;
import com.userService.UserManagementService.model.UserModel;
import com.userService.UserManagementService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    private final UserRepository repo;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;


    public LoginResponseDto loginService(LoginRequestDto request) {


        //admin login logic

        if(request.getEmail().equals(email)){
             if(request.getPassword().equals(password)){
                 String token=jwtService.generateToken(email,"ADMIN");
                 return new LoginResponseDto("success",token,email,"ADMIN");
             }
        }



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
