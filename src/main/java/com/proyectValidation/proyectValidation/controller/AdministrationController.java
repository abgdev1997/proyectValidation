package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.dto.MessageDto;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import com.proyectValidation.proyectValidation.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AdministrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unverifiedUsers")
    public ResponseEntity<List<User>> findAllUnverifiedUsers() {
        List<User> users = userRepository.findAll();
        users.removeIf(User::getVerified);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/verified/{userName}")
    public ResponseEntity<MessageDto> verified(@PathVariable("userName")String userName) {
        if (userRepository.existsByUserName(userName)) {
            Optional<User> userDB = userRepository.findByUserName(userName);
            userDB.get().setVerified(true);
            userRepository.save(userDB.get());
            return ResponseEntity.ok().body(new MessageDto("User: "+ userName +" is verified!"));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete user
     * @param id
     * @return Map result and HttpStatus.OK
     * @throws IOException
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<MessageDto> delete(@PathVariable("id") Long id) throws IOException {
        if(!userRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        userDetailsService.deleteUser(id);
        return ResponseEntity.ok().body(new MessageDto("Delete user!"));
    }
}