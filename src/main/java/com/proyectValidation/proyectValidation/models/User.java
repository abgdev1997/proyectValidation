package com.proyectValidation.proyectValidation.models;

import com.proyectValidation.proyectValidation.dto.RolDto;

import javax.persistence.*;

@Entity
@Table(name="app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String dni;
    private String dniReverse;
    private Boolean verified;
    private RolDto role;

    public User() {
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

    public Boolean getVerified() { return verified; }

    public void setVerified(Boolean verified) { this.verified = verified; }

    public RolDto getRole() { return role; }

    public void setRole(RolDto role) { this.role = role; }
}
