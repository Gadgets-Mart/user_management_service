package com.userService.UserManagementService.controller;


import com.userService.UserManagementService.dto.LoginRequestDto;
import com.userService.UserManagementService.dto.LoginResponseDto;
import com.userService.UserManagementService.service.AuthService;
import com.userService.UserManagementService.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService service;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto>  loginController(
            @RequestBody LoginRequestDto request
    ){
          LoginResponseDto responseDto=service.loginService(request);
          if("failure".equalsIgnoreCase(responseDto.getStatus())){
              return ResponseEntity.badRequest().body(responseDto);
          }
          return  ResponseEntity.ok(responseDto);
    }

}
