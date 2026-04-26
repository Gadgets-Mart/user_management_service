package com.userService.UserManagementService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {


    @Bean
    public WebMvcConfigurer corsConfiguer(){
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**") //to allow all the backend api
                        .allowedOrigins("http://localhost:9900")  //to allow frontend
                        .allowedMethods("GET","PUT","POST","DELETE","PATCH") //to allow the request methods
                        .allowedHeaders("*")//to allow all headers Authorazation,Client_name,type ect
                         .allowCredentials(true);  //to allow JWt /Auth headers
            }
        };
    }
}
