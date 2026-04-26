package com.userService.UserManagementService.service;


import com.userService.UserManagementService.model.UserDetailsImpl;
import com.userService.UserManagementService.model.UserModel;
import com.userService.UserManagementService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl  implements UserDetailsService {


    private  final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user=repo.findByEmail(username);
        if(user.isPresent()){
               return  new UserDetailsImpl(user.get());
        }

        throw new UsernameNotFoundException("There is no user with these username");
    }
}
