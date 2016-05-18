package com.example.alumne.changebook;

/**
 * Created by alumne on 29/04/16.
 */
public class Usuario {
    Integer id;
    String name;
    String password;
    String email;
    String user;


    public Usuario() {
    }

    public Usuario(String name, String password, String email, String user) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.user = user;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
