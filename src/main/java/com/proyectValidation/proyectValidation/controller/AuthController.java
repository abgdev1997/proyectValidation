package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.dto.MessageDto;
import com.proyectValidation.proyectValidation.dto.RegisterRequest;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import com.proyectValidation.proyectValidation.dto.LoginUser;
import com.proyectValidation.proyectValidation.security.jwt.JwtTokenUtil;
import com.proyectValidation.proyectValidation.security.payload.JwtResponse;
import com.proyectValidation.proyectValidation.service.DniService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final DniService dniService;

    public AuthController(AuthenticationManager authManager, UserRepository userRepository, PasswordEncoder encoder, JwtTokenUtil jwtTokenUtil, DniService dniService) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.dniService = dniService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageDto> register(@RequestBody RegisterRequest signUpRequest, @RequestParam("file") MultipartFile dni, @RequestParam("file") MultipartFile dniReverse){

        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
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

        dniService.dniSave(user, dni);

        dniService.dniReverseSave(user, dniReverse);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageDto("User registered successfully"));

    }

    /**
     * Metodo de login de usuario que comprueba que el usuario existe en la base de datos
     * que la contraseña introducida es la correcta y que el usuario esta validado en el sistema
     * @param loginUser Usuario y contrañeña pasado en el body para su comprobación
     * @return Response Entity OK si el login es correcto y BAD REQUEST si el login es incorrecto
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginUser loginUser) {
        boolean verified;
        Optional<User> userDB;
        //Comprobacion de que el usuario existe en la base de datos
        if(userRepository.existsByUserName(loginUser.getUsername())) {
            //rescatamos los datos de usuario de la base de datos
            userDB=userRepository.findByUserName(loginUser.getUsername());
            //Asignamos los atributos que nos interesan para la comprobación
            if(userDB.get().getVerified()){
                Authentication authentication = authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userDB.get().getUserName(), userDB.get().getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtTokenUtil.generateJwtToken(authentication);

                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
