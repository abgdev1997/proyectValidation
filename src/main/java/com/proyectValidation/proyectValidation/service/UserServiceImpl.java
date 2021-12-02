package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.dto.RegisterRequest;
import com.proyectValidation.proyectValidation.dto.RolDto;
import com.proyectValidation.proyectValidation.models.Image;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PasswordEncoder encoder;

    @Override
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

    @Override
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
