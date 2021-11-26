package com.proyectValidation.proyectValidation.service;

import com.proyectValidation.proyectValidation.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface DniService {
    void dniSave(User user, MultipartFile dni);

    void dniReverseSave(User user, MultipartFile dni);
}
