package com.userService.UserManagementService.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service

//generated and validates jwtToken
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    //Generate token-email+role as claims
    public String generateToken(String email,String role){
        Map<String, Object> claims=new HashMap<>();
        claims.put("role",role);
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    //to extract email from token
    public  String extractEmail(String token){
           return  extractAllClaims(token).getSubject();
    }

    //to get role from token
    public String extractRole(String token){
        return extractAllClaims(token).get("role",String.class);
    }


    public boolean validateToken(String token, UserDetails userDetails){
        String email=extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
            return extractAllClaims(token).getExpiration().before(new Date());
    }


    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }


    private Key getSignKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
