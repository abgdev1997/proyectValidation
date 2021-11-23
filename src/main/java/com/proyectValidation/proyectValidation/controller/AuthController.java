package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.dto.MessageDto;
import com.proyectValidation.proyectValidation.dto.RegisterRequest;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository userRepository, PasswordEncoder encode) {
        this.userRepository = userRepository;
        this.encoder = encode;
    }

    @GetMapping("/users")
    public Iterable<User> findAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/access")
    public ResponseEntity<MessageDto> register(@RequestBody RegisterRequest signUpRequest, @RequestParam("file") MultipartFile dni, @RequestParam("file") MultipartFile dniReverse){

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageDto("ERROR: The username exists"));
        }

        // Check 2: email
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageDto("ERROR: The email exists"));
        }

        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail());

        if(!dni.isEmpty()){

            Path imageDirectory = Paths.get("src//main//resources//static/images");
            String absolutePath = imageDirectory.toFile().getAbsolutePath();

            try {
                byte[] bytesDni = dni.getBytes();
                Path completedPath = Paths.get(absolutePath + "//" + dni.getOriginalFilename());
                Files.write(completedPath, bytesDni);
                user.setDni(dni.getOriginalFilename());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageDto("User registered successfully"));

    }
}
