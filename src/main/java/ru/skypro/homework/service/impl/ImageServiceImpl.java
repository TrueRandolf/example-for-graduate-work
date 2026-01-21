package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    public void deleteImage(String filePath){
    log.info("Delete image by path: {}",filePath);
    }

    public String saveImage(MultipartFile file){
      log.info("Save image be path: {}", file.getOriginalFilename());
        return "ads/image_000.png";
    };


}
