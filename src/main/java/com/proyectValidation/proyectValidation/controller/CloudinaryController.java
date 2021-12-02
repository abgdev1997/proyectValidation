package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.models.Image;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import com.proyectValidation.proyectValidation.service.CloudinaryService;
import com.proyectValidation.proyectValidation.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<Image>> upload(@RequestBody MultipartFile multipartFile, @RequestBody MultipartFile multipartFileReverse) throws IOException {
        List<Image> images = new ArrayList<>();

        Image dni = uploadOne(multipartFile);
        images.add(dni);
        Image dniReverse = uploadOne(multipartFileReverse);
        images.add(dniReverse);

        return ResponseEntity.ok().body(images);
    }

    private Image uploadOne(MultipartFile multipartFile) throws IOException {
        Map result = cloudinaryService.upload(multipartFile);
        Image image = new Image();
        image.setImageId((String) result.get("public_id"));
        image.setImageUrl((String) result.get("url"));
        image.setName((String) result.get("original_filename"));
        imagenService.save(image);
        return image;
    }
}