package com.userService.UserManagementService.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestDto {

    private String name;

    private String email;

    //address field

    private String phone;

    private  String addressLine;

    private  String city;

    private String state;

    private String pincode;
}
