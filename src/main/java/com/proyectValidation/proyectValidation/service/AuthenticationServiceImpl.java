package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.dto.LoginUser;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String authenticate(LoginUser user) {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenUtil.generateJwtToken(authentication);
    }

}
