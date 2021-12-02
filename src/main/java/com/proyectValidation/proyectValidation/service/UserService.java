package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.dto.RegisterRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface UserService {
    void saveUser(@RequestBody RegisterRequest signUpRequest);

    void deleteUser(Long id) throws IOException;
}
