package com.nendejan.lc101FinalProject.models;


import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "user")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15, message = "Username must be between 3 and 15 characters long.")
    private String username;

    @NotNull
    @Size(min=8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotNull

    private String passwordConfirm;

    @NotNull

    private String email;


    @ManyToOne

    public workplace workplace;



    public User() {}

    public User(String username, String password, String passwordConfirm, String email){
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.email = email;


    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public com.nendejan.lc101FinalProject.models.workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(com.nendejan.lc101FinalProject.models.workplace workplace) {
        this.workplace = workplace;
    }



}


/* TODO create a function that gets a user's workplace by its ID*/