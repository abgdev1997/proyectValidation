package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.dto.MessageDto;
import com.proyectValidation.proyectValidation.dto.RegisterRequest;
import com.proyectValidation.proyectValidation.dto.RolDto;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import com.proyectValidation.proyectValidation.dto.LoginUser;
import com.proyectValidation.proyectValidation.security.jwt.JwtTokenUtil;
import com.proyectValidation.proyectValidation.security.payload.JwtResponse;
import com.proyectValidation.proyectValidation.service.AuthenticationService;
import com.proyectValidation.proyectValidation.service.CloudinaryService;
import com.proyectValidation.proyectValidation.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder encoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageService imageService;

    public AuthController(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<MessageDto> register(@RequestBody RegisterRequest signUpRequest) {

        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
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

        User user = new User();
        user.setId(null);
        user.setUserName(signUpRequest.getUserName());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setDni(signUpRequest.getDni());
        user.setDniReverse(signUpRequest.getDniReverse());
        user.setVerified(false);
        user.setRol(RolDto.USER);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageDto("User registered successfully"));

    }


    /**
     * Metodo de login de usuario que comprueba que el usuario existe en la base de datos
     * que la contraseña introducida es la correcta y que el usuario esta validado en el sistema
     *
     * @param loginUser Usuario y contrañeña pasado en el body para su comprobación
     * @return Response Entity OK si el login es correcto y BAD REQUEST si el login es incorrecto
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginUser loginUser) {
        boolean verified;
        Optional<User> userDB;
        //Comprobacion de que el usuario existe en la base de datos
        if (userRepository.existsByUserName(loginUser.getUserName())) {
            //rescatamos los datos de usuario de la base de datos
            userDB = userRepository.findByUserName(loginUser.getUserName());
            //Asignamos los atributos que nos interesan para la comprobación
            if (userDB.get().getVerified()) {
                    if (userDB.get().getRol()== RolDto.ADMIN) {
                        String jwt = authenticationService.authenticate(loginUser);
                        return ResponseEntity.ok(new JwtResponse(jwt));
                    }
                }
                String jwt = authenticationService.authenticate(loginUser);
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        return ResponseEntity.badRequest().build();
    }
}
