package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.dto.LoginUser;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import com.proyectValidation.proyectValidation.security.jwt.JwtTokenUtil;
import com.proyectValidation.proyectValidation.security.payload.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationServiceImpl(AuthenticationManager authManager, UserRepository userRepository, PasswordEncoder encoder, JwtTokenUtil jwtTokenUtil) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String authenticate(LoginUser user) {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenUtil.generateJwtToken(authentication);

            return jwt;
    }

}
