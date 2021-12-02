package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.dto.RegisterRequest;
import com.proyectValidation.proyectValidation.dto.RolDto;
import com.proyectValidation.proyectValidation.models.Image;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Autentica un usuario de la base de datos
 *
 * Authentication Manager llama al método loadUserByUsername de esta clase
 * para obtener los detalles del usuario de la base de datos cuando
 * se intente autenticar un usuario
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PasswordEncoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),user.getPassword(),getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getRole() == RolDto.USER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (user.getRole() == RolDto.ADMIN){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }

    public void saveUser(@RequestBody RegisterRequest signUpRequest){
        User user = new User();
        user.setId(null);
        user.setUserName(signUpRequest.getUserName());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setDni(signUpRequest.getDni());
        user.setDniReverse(signUpRequest.getDniReverse());
        user.setVerified(false);
        user.setRole(RolDto.USER);

        if(signUpRequest.getEmail().split("@")[1].equals("admin.com")){
            user.setVerified(true);
            user.setRole(RolDto.ADMIN);
        }

        userRepository.save(user);
    }

    public void deleteUser(Long id) throws IOException {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            String imageUrlDni = (String) user.get().getDni();
            String imageUrlDniReverse = (String) user.get().getDniReverse();


            Optional<Image> image = imageService.getOne(imageUrlDni);
            Optional<Image> imageReverse = imageService.getOne(imageUrlDniReverse);

            if(image.isPresent() && imageReverse.isPresent()) {
                cloudinaryService.delete(image.get().getImageId());
                cloudinaryService.delete(imageReverse.get().getImageId());

                imageService.delete(image.get().getId());
                imageService.delete(imageReverse.get().getId());

                userRepository.deleteById(id);
            }
        }
    }
}
