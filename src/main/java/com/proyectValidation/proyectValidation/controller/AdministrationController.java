package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
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

    public AdministrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> users = userRepository.findAll();
        users.removeIf(u -> !u.getVerified());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/verified/{userName}")
    public ResponseEntity<User> verified(@PathParam("userName") String userName){
        if(userRepository.existsByUserName(userName)){
            Optional<User> userDB = userRepository.findByUserName(userName);
            userDB.get().setVerified(true);
            return ResponseEntity.ok(userRepository.save(userDB.get()));
        }
        return ResponseEntity.notFound().build();
    }

}
