package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.models.Image;
import com.proyectValidation.proyectValidation.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    public Optional<Image> getOne(Long id){
        return imageRepository.findById(id);
    }

    /**
     * Find a list with all the images
     * @return
     */
    public List<Image> list(){
        return imageRepository.findByOrderById();
    }

    /**
     * Save a image
     * @param image
     */
    public void save(Image image){
        imageRepository.save(image);
    }

    public void delete(Long id){
        imageRepository.deleteById(id);
    }

    public boolean exists(Long id){
        return imageRepository.existsById(id);
    }
}