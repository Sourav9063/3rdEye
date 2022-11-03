package com.example.thirdeye.user_registration;

import java.io.Serializable;

public class User implements Serializable {
    public String fullName,role,email,token;
    public  User(){

    }
    public User(String fullName,String role, String email, String token){
        this.fullName=fullName;
        this.email=email;
        this.role=role;
        this.token = token;
    }
}
