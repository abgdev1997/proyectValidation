package com.proyectValidation.proyectValidation.models;


import javax.persistence.*;
import java.awt.*;

@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String dni;
    private String dniReverse;
    private Boolean verified;

    public User(String userName, String encode, String email) {
    }

    public User(Long id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.verified=false;
    }

    public User(Long id, String userName, String password, String email, String dni, String dniReverse) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.dni = dni;
        this.dniReverse = dniReverse;
        this.verified=false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDniReverse() {
        return dniReverse;
    }

    public void setDniReverse(String dniReverse) {
        this.dniReverse = dniReverse;
    }

    public Boolean getVerified() {return verified;}

    public void setVerified(Boolean verified) {this.verified = verified;}
}
