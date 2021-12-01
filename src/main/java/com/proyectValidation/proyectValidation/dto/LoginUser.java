package com.proyectValidation.proyectValidation.dto;

/**
 * Clase que define el login de usuario para acceder al sistema
 * @author Josema & Adrian Blanco
 * @version 1.0
 */
public class LoginUser {
    //ATRIBUTOS

    private String userName;
    private String password;

    //CONTRUCTORES

    /**
     * Constructor sin atributos
     */
    public LoginUser() {
    }



    /**
     * Constructor con todos los parametros
     * @param userName nombre de usuario
     * @param password contraseña de usuario
     */
    public LoginUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    //GETTER Y SETTER


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
}
