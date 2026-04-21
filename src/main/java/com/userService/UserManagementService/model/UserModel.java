package com.userService.UserManagementService.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class UserModel {

    @Id
    private String id;
    private String name;
}
