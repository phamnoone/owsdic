package com.example.hongb_000.dictionaryows.PII;

/**
 * Created by USER on 6/25/2015.
 */
public class User {
    private String email;
    private String name;
    private String pass;
    private String phone;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public User() {
    }

    public User(String email, String name, String pass,String phone) {
        this.name = name;
        this.pass = pass;
        this.email=email;
        this.phone=phone;
    }

    public String getPass() {
        return pass;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
