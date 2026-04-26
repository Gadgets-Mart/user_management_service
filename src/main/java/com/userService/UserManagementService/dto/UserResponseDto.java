package com.userService.UserManagementService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String id;
    private String name;

    private String email;

    //address field

    private String phone;

    private  String addressLine;

    private  String city;

    private String state;

    private String pincode;

    private Instant createdAt;

    private Instant updatedAt;

}
