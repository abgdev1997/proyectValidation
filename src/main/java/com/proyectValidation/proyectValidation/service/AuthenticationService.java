package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.dto.LoginUser;

public interface AuthenticationService {

    String authenticate(LoginUser user);

}
