package com.fzx.drivertrainingmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "real_name")
    private String realName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String status;
}