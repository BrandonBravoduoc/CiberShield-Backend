package com.cibershield.cibershield.service.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadUserImage(MultipartFile file) {
        validateImage(file);

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "cibershield/users"));

            return (String) result.get("secure_url");

        } catch (Exception e) {
            throw new RuntimeException("Error al subir la imagen.");
        }
    }

    public String uploadProductImage(MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;
        validateImage(file);

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "cibershield/products"));

            return (String) result.get("secure_url");

        } catch (Exception e) {
            throw new RuntimeException("Error al subir la imagen del producto.");
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty())
            return;

        if (file.getSize() > 8 * 1024 * 1024) {
            throw new RuntimeException("La imagen no puede pesar más de 8MB");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Solo se permiten imágenes");
        }
    }
}
