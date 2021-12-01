package com.proyectValidation.proyectValidation.dto;

public class RegisterRequest {

    private String userName;
    private String password;
    private String email;
    private String dni;
    private String dniReverse;

    public RegisterRequest() {
    }

    public RegisterRequest(String userName, String password, String email, String dni, String dniReverse) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.dni = dni;
        this.dniReverse = dniReverse;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getDniReverse() {
        return dniReverse;
    }

    public void setDniReverse(String dniReverse) {
        this.dniReverse = dniReverse;
    }
}

