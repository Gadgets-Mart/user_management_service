package com.userService.UserManagementService.service;


import com.userService.UserManagementService.dto.StatusResponse;
import com.userService.UserManagementService.dto.UpdateRequestDto;
import com.userService.UserManagementService.dto.UserResponseDto;
import com.userService.UserManagementService.model.Role;
import com.userService.UserManagementService.model.UserModel;
import com.userService.UserManagementService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository repo;

    private final PasswordEncoder bCryptPasswordEncoder; //for encoding of password


    //Post Service
    public StatusResponse registerService(UserModel userDetails) {
        userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        if(userDetails.getRole()==null){
            userDetails.setRole(Role.customer);
        }
        repo.save(userDetails);
        return new StatusResponse("success","user details successfully saved to DB",userDetails.getId());
    }

    //update service
    public StatusResponse updateUserDetailsService(UpdateRequestDto userDetails) {


        // ✅ Get current logged in user's role from Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isCustomer = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

        if (!isCustomer) {
            return new StatusResponse("failure", "only customers can update details", null);
        }

        Optional<UserModel> users = repo.findByEmail(userDetails.getEmail());

        if (users.isPresent()) {

            UserModel existingUser = users.get();         // get existing record from DB

            // update only if value is sent (not null)
            if (userDetails.getName() != null)
                existingUser.setName(userDetails.getName());

            if (userDetails.getPhone() != null)
                existingUser.setPhone(userDetails.getPhone());

            if (userDetails.getAddressLine() != null)
                existingUser.setAddressLine(userDetails.getAddressLine());

            if (userDetails.getCity() != null)
                existingUser.setCity(userDetails.getCity());

            if (userDetails.getState() != null)
                existingUser.setState(userDetails.getState());

            if (userDetails.getPincode() != null)
                existingUser.setPincode(userDetails.getPincode());
            existingUser.setUpdatedAt(Instant.now());
            repo.save(existingUser);                      // save the existing object with updated fields

            return new StatusResponse("success", "user details updated", existingUser.getId());
        }

        return new StatusResponse("failure", "no user found with this email", null);
    }





    //Get Service
    public StatusResponse getUserDetailsService(String email) {

        Optional<UserModel> usersDetails=repo.findByEmail(email);
        if(usersDetails.isPresent()){
             UserModel u=usersDetails.get();
            UserResponseDto user= new UserResponseDto(
                    u.getId(),
                    u.getName(),
                    u.getEmail(),
                    u.getPhone(),
                    u.getAddressLine(),
                    u.getCity(),
                    u.getState(),
                    u.getPincode(),
                    u.getCreatedAt(),
                    u.getUpdatedAt()
            );
            return new StatusResponse("success", "user details ",user);
        }
        return new StatusResponse("failure", "no user found with this email", null);
    }
}
