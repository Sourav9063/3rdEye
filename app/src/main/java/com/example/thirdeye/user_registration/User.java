package com.example.thirdeye.user_registration;

public class User {
    public String fullName,role,email;
    public  User(){

    }
    public User(String fullName,String role, String email){
        this.fullName=fullName;
        this.email=email;
        this.role=role;
    }
    public String getRole(){
        return role;
    }
    public void setRole(){
        this.role=role;
    }
    public String getFullName(){
        return fullName;
    }
    public void setFullName(){
        this.fullName=fullName;
    }
}
