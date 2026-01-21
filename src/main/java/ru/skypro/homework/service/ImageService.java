package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void deleteImage(String filePath);
    String saveImage(MultipartFile file);
}
