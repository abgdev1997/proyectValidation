package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.dto.LoginUser;
import com.proyectValidation.proyectValidation.dto.RolDto;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import com.proyectValidation.proyectValidation.security.payload.JwtResponse;
import com.proyectValidation.proyectValidation.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AdministrationController {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public AdministrationController(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userRepository.findAll();
        users.removeIf(u -> !u.getVerified());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/verified/{userName}")
    public ResponseEntity<User> verified(@PathParam("userName") String userName) {
        if (userRepository.existsByUserName(userName)) {
            Optional<User> userDB = userRepository.findByUserName(userName);
            userDB.get().setVerified(true);
            return ResponseEntity.ok(userRepository.save(userDB.get()));
        }
        return ResponseEntity.notFound().build();
    }
}