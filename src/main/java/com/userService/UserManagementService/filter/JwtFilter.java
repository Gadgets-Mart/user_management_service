package com.userService.UserManagementService.filter;

import com.userService.UserManagementService.service.JwtService;
import com.userService.UserManagementService.service.MyUserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter  extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final MyUserDetailsServiceImpl myUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public  boolean shouldNotFilter(HttpServletRequest request){
        //skip jwt token for public endpoints
        String requestPath=request.getServletPath();
        return requestPath.equals("/api/user_management_service/register") ||
                requestPath.equals("/api/auth/login");
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }



        //skip 6 characters("Bearer ")
        String token = authHeader.substring(7);
        String email = null;

        try {
            email = jwtService.extractEmail(token);
        } catch (Exception e) {
            //invalid token
            filterChain.doFilter(request, response);
            return;
        }



        // If email found and no auth set yet in context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);

                if(!userDetails.getUsername().equals(email)){
                    filterChain.doFilter(request,response);
                    return;
                }

                if (jwtService.validateToken(token, userDetails)) {

                    // Set authentication in Security Context
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (UsernameNotFoundException e) {

                String role = jwtService.extractRole(token);
                if ("ADMIN".equals(role)) {
                    UserDetails adminDetails = User.builder()
                            .username(email)
                            .password("")
                            .roles("ADMIN")
                            .build();


                    // Set authentication in Security Context
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    adminDetails,
                                    null,
                                    adminDetails.getAuthorities()
                            );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }
        }

            filterChain.doFilter(request, response);
    }
}
