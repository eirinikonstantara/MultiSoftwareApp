package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Image;

public interface ImageService {
    
    Image store(MultipartFile file) throws IOException;
    
    Optional<Image> getImage(Long id);
    
    byte[] getImageData(Long id) throws IOException;
    
    List<Image> getAllImages();
} 