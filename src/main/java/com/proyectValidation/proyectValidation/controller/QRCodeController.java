package com.proyectValidation.proyectValidation.controller;

import com.proyectValidation.proyectValidation.service.QRCodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QRCodeController {

    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/temp/QRCode.png";
    Integer width = 200;
    Integer height = 200;

    @GetMapping(value = "/generateAndDownloadQRCode/{codeText}")
    public void download(@PathVariable("codeText") String codeText)
            throws Exception {
        QRCodeGenerator.generateQRCodeImage(codeText, width, height, QR_CODE_IMAGE_PATH);
    }

    @GetMapping(value = "/generateQRCode/{codeText}/{width}/{height}")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable("codeText") String codeText)
            throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImage(codeText, width, height));
    }
}
