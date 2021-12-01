package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.dto.MessageDto;
import com.proyectValidation.proyectValidation.models.Image;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import com.proyectValidation.proyectValidation.service.CloudinaryService;
import com.proyectValidation.proyectValidation.service.ImageService;
import com.sun.istack.NotNull;
import org.apache.http.annotation.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
public class CloudinaryController {

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ImageService imagenService;

    @Autowired
    UserRepository userRepository;

    /**
     * List of images
     * @return list
     */
    @GetMapping("/api/cloudinary/list")
    public ResponseEntity<List<Image>> list(){
        List<Image> list = imagenService.list();
        return ResponseEntity.ok().body(list);
    }

    /**
     * Upload image
     * @param multipartFile
     * @return Json and HttpStatus.OK
     * @throws IOException
     */
    @PostMapping("/api/auth/cloudinary/upload")
    public ResponseEntity<Image> upload(@RequestParam MultipartFile multipartFile) throws IOException {

        Map result = cloudinaryService.upload(multipartFile);
        Image image = new Image(
                (String)result.get("original_filename"),
                (String)result.get("url"),
                (String)result.get("public_id"));
        imagenService.save(image);
        return ResponseEntity.ok().body(image);
    }

    /**
     * Delete image
     * @param id
     * @return Map result and HttpStatus.OK
     * @throws IOException
     */
    @DeleteMapping("/api/users/delete/{id}")
    public ResponseEntity<MessageDto> delete(@PathVariable("id") Long id) throws IOException {
        if(!userRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Optional<User> user = userRepository.findById(id);

        String imageUrlDni = user.get().getDni();
        String imageUrlDniReverse = user.get().getDniReverse();

        Image image = imagenService.getOne(imageUrlDni).get();
        Image imageReverse = imagenService.getOne(imageUrlDniReverse).get();

        Map result = cloudinaryService.delete(image.getImageId());
        Map resultReverse = cloudinaryService.delete(imageReverse.getImageId());

        imagenService.delete(image.getId());
        imagenService.delete(imageReverse.getId());

        userRepository.deleteById(id);

        return ResponseEntity.ok().body(new MessageDto("Delete user!"));
    }


}
