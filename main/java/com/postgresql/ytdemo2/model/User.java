    package com.postgresql.ytdemo2.model;


import com.postgresql.ytdemo2.Enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;


    @Entity//another entity class, called a entity class because its used/relates to the database.
@Data
@Table(name="users")



public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Add this line to auto-generate IDs
    private Long id;

    public String username;

    private String password;

    private Role role;

    private LocalDate expiryDate;




















}
