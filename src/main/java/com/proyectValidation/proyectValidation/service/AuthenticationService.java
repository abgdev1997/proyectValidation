package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.dto.LoginUser;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.security.payload.JwtResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AuthenticationService {

    String authenticate(LoginUser user);

}
