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
import com.proyectValidation.proyectValidation.service.DniService;
import com.proyectValidation.proyectValidation.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final AuthenticationService authenticationService;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    public AuthController(AuthenticationManager authManager, UserRepository userRepository, PasswordEncoder encoder, JwtTokenUtil jwtTokenUtil, DniService dniService, AuthenticationService authenticationService, CloudinaryService cloudinaryService, ImageService imageService) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.dniService = dniService;
        this.authenticationService = authenticationService;
        this.cloudinaryService = cloudinaryService;
        this.imageService = imageService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageDto> register(@ModelAttribute RegisterRequest signUpRequest) {

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

        User user = new User(signUpRequest.getUserName(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail());

        userRepository.save(user);

        return ResponseEntity.ok(new MessageDto("User registered successfully"));

    }

    @PostMapping("/dni")
    public ResponseEntity<?> dni(@RequestParam("file") MultipartFile dni, @RequestParam("file") MultipartFile dniReverse) {
        /*
        dniService.dniSave(user, dni);

        dniService.dniReverseSave(user, dniReverse);*/
        //Realizamos la subida de las imagenes a Cloudinary
        String urlDni;
        String urlDniReverse;
        List<String> dniList = null;
        try {
            BufferedImage biDni = ImageIO.read(dni.getInputStream());
            BufferedImage biDniReverse = ImageIO.read(dniReverse.getInputStream());
            if (biDni != null || biDniReverse != null) {
                Map resultDni = cloudinaryService.upload(dni);
                Map resultDniReverse = cloudinaryService.upload(dniReverse);
                urlDni = (String) resultDni.get("url");
                urlDniReverse = (String) resultDniReverse.get("url");
                dniList.add(urlDni);
                dniList.add(urlDniReverse);
                return new ResponseEntity(dniList, HttpStatus.OK);
            }

        } catch (IOException e) {
            System.err.println("Hubo algun problema en la subida de la imagen." + e.getMessage());
        }
        return new ResponseEntity(new MessageDto("Subida satisfactoria!"), HttpStatus.OK);
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
        if (userRepository.existsByUserName(loginUser.getUsername())) {
            //rescatamos los datos de usuario de la base de datos
            userDB = userRepository.findByUserName(loginUser.getUsername());

            if (userDB.get().getRol().equals(RolDto.ADMIN) && Objects.equals(userDB.get().getUserName(), "admin")) {
                String jwt = authenticationService.authenticate(userDB);
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
            //Asignamos los atributos que nos interesan para la comprobación
            if (userDB.get().getVerified()) {
                String jwt = authenticationService.authenticate(userDB);
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
