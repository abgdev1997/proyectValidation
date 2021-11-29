package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DniServiceImpl implements DniService {

    @Override
    public void dniSave(User user, MultipartFile dni){
        if(!dni.isEmpty()){

            Path imageDirectory = Paths.get("src//main//resources//static/images");
            String absolutePath = imageDirectory.toFile().getAbsolutePath();

            try {
                byte[] bytesDni = dni.getBytes();
                Path completedPath = Paths.get(absolutePath + "//" + dni.getOriginalFilename());
                Files.write(completedPath, bytesDni);
                user.setDni(dni.getOriginalFilename());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dniReverseSave(User user, MultipartFile dniReverse){
        if(!dniReverse.isEmpty()){

            Path imageDirectory = Paths.get("src//main//resources//static/images");
            String absolutePath = imageDirectory.toFile().getAbsolutePath();

            try {
                byte[] bytesDniReverse = dniReverse.getBytes();
                Path completedPath = Paths.get(absolutePath + "//" + dniReverse.getOriginalFilename());
                Files.write(completedPath, bytesDniReverse);
                user.setDniReverse(dniReverse.getOriginalFilename());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
