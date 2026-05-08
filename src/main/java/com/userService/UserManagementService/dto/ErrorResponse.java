package com.userService.UserManagementService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {


    private int status;
    private String message;
    private String details;
    private String timeStamp;
}

