package com.userService.UserManagementService.controller;


import com.userService.UserManagementService.dto.TokenValidationResponse;
import com.userService.UserManagementService.dto.loginDto.LoginRequestDto;
import com.userService.UserManagementService.dto.loginDto.LoginResponseDto;
import com.userService.UserManagementService.service.AuthService;
import com.userService.UserManagementService.service.JwtService;
import com.userService.UserManagementService.service.MyUserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService service;

    private final JwtService jwtService;

    private final MyUserDetailsServiceImpl myUserDetailsService;

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


//    @GetMapping(value="/validate_token",
//          produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<TokenValidationResponse> validateTokenController(
//            @RequestHeader("Authorization") String authToken
//    ){
//        if(authToken==null || authToken.isEmpty()){
//            return ResponseEntity.badRequest()
//                    .body(new TokenValidationResponse(false,null,
//                            null,"token is empty or null")
//                    );
//        }
//        try{
//
//            String token=authToken.substring(7);
//            String email=jwtService.extractEmail(token);
//
//            UserDetails userDetails= myUserDetailsService.loadUserByUsername(email);
//            boolean isValid= jwtService.validateToken(token,userDetails);
//            if(isValid){
//                String role = userDetails.getAuthorities().stream()
//                        .findFirst()
//                        .map(a -> a.getAuthority().replace("ROLE_", ""))
//                        .orElse("UNKNOWN");
//                return ResponseEntity.ok(new TokenValidationResponse(true, email, role,"Token is valid"));
//            }
//             return ResponseEntity.ok(new TokenValidationResponse(false, null, null,"Token is Invalid"));
//
//        }catch (ExpiredJwtException e){
//            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(
//                    new TokenValidationResponse(false,null,
//                            null,e.getMessage())
//            );
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(
//                    new TokenValidationResponse(false,null,
//                            null,e.getMessage())
//            );
//        }
//    }


    @GetMapping(value="/validate_token",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenValidationResponse> validateTokenController(
            @RequestHeader("Authorization") String authToken
    ){
        if(authToken==null || authToken.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new TokenValidationResponse(false,null,
                            null,"token is empty or null")
                    );
        }
        try{

            String token=authToken.substring(7);
            String email=jwtService.extractEmail(token);

//            UserDetails userDetails= myUserDetailsService.loadUserByUsername(email);
            boolean isValid= jwtService.validateToken(token);
            if(isValid){
//                String role = userDetails.getAuthorities().stream()
//                        .findFirst()
//                        .map(a -> a.getAuthority().replace("ROLE_", ""))
//                        .orElse("UNKNOWN");
                String role=jwtService.extractRole(token);
                return ResponseEntity.ok(new TokenValidationResponse(true, email, role,"Token is valid"));
            }
            return ResponseEntity.ok(new TokenValidationResponse(false, null, null,"Token is Invalid"));

        }catch (ExpiredJwtException e){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(
                    new TokenValidationResponse(false,null,
                            null,e.getMessage())
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(
                    new TokenValidationResponse(false,null,
                            null,e.getMessage())
            );
        }
    }


}
