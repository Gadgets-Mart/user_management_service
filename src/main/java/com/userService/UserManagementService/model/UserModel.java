package com.userService.UserManagementService.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="userDetails")
public class UserModel{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
    private String id;

    @Column(name = "name",nullable = false,length = 100)
    private String name;

    @Column(name = "email",nullable = false,unique = true,length = 150)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false)
    private Role role=Role.customer;  //default value

    //address field
    @Column(name = "phone",length = 12)
    private String phone;

    @Column(name = "address_line")
    private  String addressLine;

    @Column(name = "city",length = 100)
    private  String city;

    @Column(name = "state",length = 100)
    private String state;

    @Column(name = "pincode",length = 10)
    private String pincode;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt;

}