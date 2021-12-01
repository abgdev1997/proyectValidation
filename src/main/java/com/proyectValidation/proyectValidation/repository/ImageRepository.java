package com.proyectValidation.proyectValidation.repository;

import com.proyectValidation.proyectValidation.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByOrderById();

    void deleteById(Long id);

    Optional<Image> findByImageUrl(String imageUrl);
}
