package com.userService.UserManagementService.ExceptionHandler;


import com.userService.UserManagementService.dto.ErrorResponse;
import com.userService.UserManagementService.dto.TokenValidationResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleException(
            MissingServletRequestParameterException e
    ){
          ErrorResponse response=new ErrorResponse(
                  HttpStatus.BAD_REQUEST.value(),
                  e.getMessage(),
                  "Missing Param "+e.getParameterName(),
                  LocalDateTime.now().toString()
          );
          return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> missingPathException(
            MissingPathVariableException e
    ){
        ErrorResponse response=new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                e.getParameter().getParameterName(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataIntegrityException(
            DataIntegrityViolationException e
    ){
        ErrorResponse response=new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "dublicate entry of primary key in userDetails",
                e.getClass().toString(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }



}