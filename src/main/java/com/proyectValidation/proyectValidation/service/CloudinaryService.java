package com.proyectValidation.proyectValidation.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service
public class CloudinaryService {
    private Cloudinary cloudinary;

    private Map<String, String> valuesMap = new HashMap<>();

    @Value("${app.cloudinary.cloud_name}")
    private String cloud_name;

    @Value("${app.cloudinary.api_key}")
    private String api_key;

    @Value("${app.cloudinary.api_secret}")
    private String api_secret;

    public String getCloud_name() {
        return cloud_name;
    }

    public String getApi_key() {
        return api_key;
    }

    public String getApi_secret() {
        return api_secret;
    }

    public CloudinaryService(){
        valuesMap.put("cloud_name", getCloud_name());
        valuesMap.put("api_key", getApi_key());
        valuesMap.put("api_secret", getApi_secret());
        cloudinary = new Cloudinary(valuesMap);
    }

    /**
     * Upload Image
     * @param multipartFile
     * @return Map result
     * @throws IOException
     */
    public Map<?,?> upload(MultipartFile multipartFile) throws IOException {
        //Convertimos el multipart
        File file = convert(multipartFile);
        //lo subimos a cloudinary
        Map<?,?> result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        //Borramos el FileOutputStream
        file.delete();

        return result;
    }

    /**
     * Delete Image
     * @param id
     * @throws IOException
     */
    public void delete(String id) throws IOException {
        cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    /**
     * Convert MultipartFile on File
     * @param multipartFile
     * @return file
     * @throws IOException
     */
    private File convert(MultipartFile multipartFile) throws IOException {
        //Creamos un archivo para alojar el nombre del multipart que llega por props
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        //Creamos un objeto FileOutputStream para poder guardar el archivo en forma file
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }

    //GETTER Y SETTER

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    public void setCloudinary(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, String> getValuesMap() {
        return valuesMap;
    }

    public void setValuesMap(Map<String, String> valuesMap) {
        this.valuesMap = valuesMap;
    }
}
