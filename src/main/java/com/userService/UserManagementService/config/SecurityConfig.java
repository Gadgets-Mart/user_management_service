package com.userService.UserManagementService.config;


import com.userService.UserManagementService.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {



    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;



    private final UserDetailsService userDetailsService;

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(s->s.disable())
                .httpBasic(Customizer.withDefaults())   //Basic Auth Enabled
                .authenticationProvider(dbAuthenticationProvider())
                .authenticationProvider(inMemoryAuthenticationProvider())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/user_management_service/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    //Auth by comparing username and password-Customer
    @Bean
    public AuthenticationProvider dbAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



    //admin authentication
    @Bean
    public AuthenticationProvider inMemoryAuthenticationProvider(){

        UserDetails admin= User.builder()
                .username(email)
                .password(bCryptPasswordEncoder().encode(password))
                .roles("ADMIN")
                .build();

        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(new InMemoryUserDetailsManager(admin));
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
         return daoAuthenticationProvider;
    }




}
