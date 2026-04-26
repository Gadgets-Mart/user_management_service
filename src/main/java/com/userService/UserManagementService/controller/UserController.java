package com.userService.UserManagementService.controller;


import com.userService.UserManagementService.dto.StatusResponse;
import com.userService.UserManagementService.dto.UpdateRequestDto;
import com.userService.UserManagementService.model.UserModel;
import com.userService.UserManagementService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user_management_service")
public class UserController {


      private final UserService service;

    @PostMapping(value = "/register",consumes ="application/json")
    public ResponseEntity<StatusResponse> registerController(
            @RequestBody UserModel userDetails
    ){
            StatusResponse response=service.registerService(userDetails);
            if("failure".equalsIgnoreCase(response.getStatus())){
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok(response);
    }


    @PatchMapping("/update_user_details")
    public ResponseEntity<StatusResponse> updateUserDetailsController(
            @RequestBody UpdateRequestDto userDetails
    ){
        StatusResponse response=service.updateUserDetailsService(userDetails);
        if("failure".equalsIgnoreCase(response.getStatus())){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/get_user_details")
    public ResponseEntity<StatusResponse> getUserDetailsController(
            @RequestParam String email
    ){

        StatusResponse response=service.getUserDetailsService(email);
        if("failure".equalsIgnoreCase(response.getStatus())){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }




}

