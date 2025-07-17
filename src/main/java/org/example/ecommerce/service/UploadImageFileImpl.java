package org.example.ecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@Service

public class UploadImageFileImpl implements UploadImageFile {
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) throws IOException{
        assert file.getOriginalFilename() != null;
        String publicValue = generatePublicValue(file.getOriginalFilename());
        String extension = getFileName(file.getOriginalFilename())[1];

        File fileUpload = convert(file);

        String contentType = file.getContentType();
        Map<String, Object> uploadParams = new HashMap<>();
        uploadParams.put("public_id", publicValue);

        if (contentType != null && contentType.startsWith("video/")) {
            uploadParams.put("resource_type", "video");
        } else if (contentType != null && !contentType.startsWith("image/")) {
            uploadParams.put("resource_type", "auto");
        }

        Map uploadResult = cloudinary.uploader().upload(fileUpload, uploadParams);
        cleanDisk(fileUpload);
        // Lấy URL trả về từ Cloudinary (chuẩn cho cả ảnh và video)
        return (String) uploadResult.get("secure_url");
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try(InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {

            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {

        }
    }

    public String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }

    }