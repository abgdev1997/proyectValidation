package com.proyectValidation.proyectValidation.security.payload;

/**
 * Clase que define el login de usuario para acceder al sistema
 * @author Josema & Adrian Blanco
 * @version 1.0
 */
public class LoginUser {
    //ATRIBUTOS

    private String username;
    private String password;

    //CONTRUCTORES

    /**
     * Constructor sin atributos
     */
    public LoginUser() {
    }

    /**
     * Constructor con todos los parametros
     * @param username nombre de usuario
     * @param password contraseña de usuario
     */
    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //GETTER Y SETTER


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
}
